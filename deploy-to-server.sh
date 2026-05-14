#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${ENV_FILE:-$SCRIPT_DIR/.env}"

REMOTE_HOST="${REMOTE_HOST:-91.229.10.175}"
REMOTE_USER="${REMOTE_USER:-root}"
REMOTE_PASSWORD="${REMOTE_PASSWORD:-wKIMqPF1HDI9QHlN}"
REMOTE_DIR="${REMOTE_DIR:-/opt/geobudget-backend}"

CONTAINER_NAME="${CONTAINER_NAME:-geobudget-backend}"
IMAGE_NAME="${IMAGE_NAME:-geobudget-backend:deploy}"

DIST_DIR="$SCRIPT_DIR/dist/deploy"
ARCHIVE_BASENAME="geobudget-backend-image.tar.gz"
ARCHIVE_PATH="$DIST_DIR/$ARCHIVE_BASENAME"

SSH_OPTS=(
  -o StrictHostKeyChecking=no
  -o UserKnownHostsFile=/dev/null
  -o LogLevel=ERROR
)

require_cmd() {
  if ! command -v "$1" >/dev/null 2>&1; then
    printf 'Missing required command: %s\n' "$1" >&2
    exit 1
  fi
}

map_remote_arch_to_platform() {
  case "$1" in
    x86_64|amd64)
      printf 'linux/amd64\n'
      ;;
    aarch64|arm64)
      printf 'linux/arm64\n'
      ;;
    *)
      printf 'Unsupported remote architecture: %s\n' "$1" >&2
      exit 1
      ;;
  esac
}

require_cmd docker
require_cmd sshpass
require_cmd gzip
require_cmd tar
require_cmd shasum

if [ ! -f "$ENV_FILE" ]; then
  printf 'Env file not found: %s\n' "$ENV_FILE" >&2
  exit 1
fi

set -a
# shellcheck disable=SC1090
. "$ENV_FILE"
set +a

printf 'Detecting remote architecture...\n'
REMOTE_ARCH="$({
  sshpass -p "$REMOTE_PASSWORD" ssh "${SSH_OPTS[@]}" "$REMOTE_USER@$REMOTE_HOST" "uname -m"
} | tr -d '\r')"

TARGET_PLATFORM="${TARGET_PLATFORM:-$(map_remote_arch_to_platform "$REMOTE_ARCH") }"
TARGET_PLATFORM="${TARGET_PLATFORM// /}"

printf 'Remote architecture: %s\n' "$REMOTE_ARCH"
printf 'Build platform: %s\n' "$TARGET_PLATFORM"

mkdir -p "$DIST_DIR"

printf 'Building Docker image...\n'
docker buildx build \
  --platform "$TARGET_PLATFORM" \
  --tag "$IMAGE_NAME" \
  --build-arg "JWT_SECRET=${JWT_SECRET:-}" \
  --build-arg "DADATA_API_KEY=${DADATA_API_KEY:-}" \
  --build-arg "DADATA_X_SECRET=${DADATA_X_SECRET:-}" \
  --build-arg "PROVERKACHECKA_API_KEY=${PROVERKACHECKA_API_KEY:-}" \
  --load \
  "$SCRIPT_DIR"

printf 'Saving image archive...\n'
docker save "$IMAGE_NAME" | gzip > "$ARCHIVE_PATH"

printf 'Preparing remote directory...\n'
sshpass -p "$REMOTE_PASSWORD" ssh "${SSH_OPTS[@]}" "$REMOTE_USER@$REMOTE_HOST" \
  "mkdir -p '$REMOTE_DIR'"

printf 'Uploading image and env file...\n'
sshpass -p "$REMOTE_PASSWORD" scp "${SSH_OPTS[@]}" "$ARCHIVE_PATH" \
  "$REMOTE_USER@$REMOTE_HOST:$REMOTE_DIR/$ARCHIVE_BASENAME"
sshpass -p "$REMOTE_PASSWORD" scp "${SSH_OPTS[@]}" "$ENV_FILE" \
  "$REMOTE_USER@$REMOTE_HOST:$REMOTE_DIR/.env"

printf 'Deploying on server...\n'
sshpass -p "$REMOTE_PASSWORD" ssh "${SSH_OPTS[@]}" "$REMOTE_USER@$REMOTE_HOST" "
set -euo pipefail
cd '$REMOTE_DIR'

gunzip -c '$REMOTE_DIR/$ARCHIVE_BASENAME' | docker load

docker rm -f '$CONTAINER_NAME' >/dev/null 2>&1 || true

docker run -d \
  --name '$CONTAINER_NAME' \
  --restart unless-stopped \
  --network host \
  --env-file '$REMOTE_DIR/.env' \
  '$IMAGE_NAME'

if command -v curl >/dev/null 2>&1; then
  for i in \
    1 2 3 4 5 6 7 8 9 10 \
    11 12 13 14 15 16 17 18 19 20 \
    21 22 23 24 25 26 27 28 29 30; do
    if curl -fsS 'http://127.0.0.1:8180/actuator/health' >/dev/null 2>&1; then
      echo 'Backend is healthy'
      exit 0
    fi
    sleep 2
  done
fi

docker logs '$CONTAINER_NAME' --tail 200
"

printf 'Deployment archive: %s\n' "$ARCHIVE_PATH"
printf 'Archive SHA256: '
shasum -a 256 "$ARCHIVE_PATH"
