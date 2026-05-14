#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
BACKUP_DIR="${1:-$PROJECT_DIR/backups}"
TIMESTAMP="$(date +%Y-%m-%d_%H-%M-%S)"
COMPOSE_FILE="$PROJECT_DIR/docker-compose.yml"
STARTED_BY_SCRIPT=0

if ! command -v docker >/dev/null 2>&1; then
  echo "docker is not installed or not in PATH" >&2
  exit 1
fi

mkdir -p "$BACKUP_DIR"

if [ ! -f "$PROJECT_DIR/.env" ]; then
  echo ".env not found in $PROJECT_DIR" >&2
  exit 1
fi

set -a
# shellcheck disable=SC1091
. "$PROJECT_DIR/.env"
set +a

DB_NAME="${POSTGRES_DB:-geobudget}"
DB_USER="${SPRING_DATASOURCE_USERNAME:-geouser}"
BACKUP_FILE="$BACKUP_DIR/${DB_NAME}_${TIMESTAMP}.sql.gz"
TMP_FILE="$BACKUP_FILE.tmp"

compose() {
  docker compose --project-directory "$PROJECT_DIR" -f "$COMPOSE_FILE" "$@"
}

cleanup() {
  rm -f "$TMP_FILE"

  if [ "$STARTED_BY_SCRIPT" = "1" ] && [ "${KEEP_POSTGRES_RUNNING:-false}" != "true" ]; then
    compose stop postgres >/dev/null 2>&1 || true
  fi
}

trap cleanup EXIT

if [ ! -f "$COMPOSE_FILE" ]; then
  echo "docker-compose.yml not found in $PROJECT_DIR" >&2
  exit 1
fi

if ! compose config --services >/dev/null 2>&1; then
  echo "docker compose configuration is invalid" >&2
  exit 1
fi

if ! compose exec -T postgres pg_isready -U "$DB_USER" -d "$DB_NAME" >/dev/null 2>&1; then
  echo "Starting postgres service..."
  compose up -d postgres
  STARTED_BY_SCRIPT=1
fi

ready=0
for _ in {1..60}; do
  if compose exec -T postgres pg_isready -U "$DB_USER" -d "$DB_NAME" >/dev/null 2>&1; then
    ready=1
    break
  fi

  sleep 2
done

if [ "$ready" != "1" ]; then
  echo "postgres did not become ready in time" >&2
  exit 1
fi

echo "Creating backup: $BACKUP_FILE"

compose exec -T postgres pg_dump -U "$DB_USER" -d "$DB_NAME" --clean --if-exists --no-owner --no-privileges | gzip > "$TMP_FILE"

mv "$TMP_FILE" "$BACKUP_FILE"

echo "Backup completed: $BACKUP_FILE"
