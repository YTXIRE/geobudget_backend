#!/bin/sh
set -e

# -------------------------
# Настройка времени сборки
# -------------------------
export TZ="Europe/Moscow"
BUILD_DATE=$(date +"%Y-%m-%d %H:%M:%S %Z")
echo "🕒 Время сборки: $BUILD_DATE"

PIPELINE_START_SEC=$(date +%s)
echo "PIPELINE_START_SEC=$PIPELINE_START_SEC"

# -------------------------
# Настройки Git
# -------------------------
apk add --no-cache git curl jq >/dev/null 2>&1 || true
git config user.name "GitLab CI"
git config user.email "ci@$CI_SERVER_HOST"

APP_NAME="backend"
VERSION_SERVICE_URL="http://10.8.0.2:4632"

# -------------------------
# Получаем текущую версию из Version Service
# -------------------------
RESPONSE=$(curl -s "$VERSION_SERVICE_URL/version/$APP_NAME" || true)
if [ -z "$RESPONSE" ] || [ "$RESPONSE" = "null" ]; then
  OLD_VERSION="1.0.0"
else
  OLD_VERSION=$(echo "$RESPONSE" | jq -r '.version')
fi

echo "📦 Текущая версия: $OLD_VERSION"

# -------------------------
# Увеличиваем patch
# -------------------------
major=$(echo "$OLD_VERSION" | cut -d. -f1)
minor=$(echo "$OLD_VERSION" | cut -d. -f2)
patch=$(echo "$OLD_VERSION" | cut -d. -f3)
NEW_VERSION="$major.$minor.$((patch+1))"
echo "✅ Новая версия: $NEW_VERSION"

# -------------------------
# Отправляем новую версию в Version Service
# -------------------------
curl -s -X POST "$VERSION_SERVICE_URL/version" \
  -H "Content-Type: application/json" \
  -d "{\"app\": \"$APP_NAME\", \"version\": \"$NEW_VERSION\"}"

# -------------------------
# Создаем артефакт окружения для CI
# -------------------------
echo "VERSION=$NEW_VERSION" > version.env
echo "BUILD_DATE='$BUILD_DATE'" >> version.env
echo "CI_COMMITTER='$GITLAB_USER_NAME'" >> version.env
echo "PIPELINE_START_SEC=$PIPELINE_START_SEC" >> version.env

echo "🎯 Версия обновлена до $NEW_VERSION"
