#!/bin/sh
set -e

# -------------------------
# Настройка времени сборки
# -------------------------
export TZ="Europe/Moscow"
BUILD_DATE=$(date +"%Y-%m-%d %H:%M:%S %Z")
echo "🕒 Время сборки: $BUILD_DATE"

# -------------------------
# Используем время сборки как старт pipeline
# -------------------------
PIPELINE_START_SEC=$(date +%s)
echo "PIPELINE_START_SEC=$PIPELINE_START_SEC"

# -------------------------
# Настройки Git
# -------------------------
apk add --no-cache git >/dev/null 2>&1 || true
git config user.name "GitLab CI"
git config user.email "ci@$CI_SERVER_HOST"

# Безопасный переход на main
git fetch origin main --tags
git checkout main
git pull origin main || true

# -------------------------
# Работа с версией
# -------------------------
[ ! -f version.txt ] && echo "1.0.0" > version.txt
OLD_VERSION=$(cat version.txt)
echo "📦 Текущая версия: $OLD_VERSION"

major=$(echo "$OLD_VERSION" | cut -d. -f1)
minor=$(echo "$OLD_VERSION" | cut -d. -f2)
patch=$(echo "$OLD_VERSION" | cut -d. -f3)

NEW_VERSION="$major.$minor.$((patch+1))"
echo "$NEW_VERSION" > version.txt
echo "✅ Новая версия: $NEW_VERSION"

# -------------------------
# Коммит и тег
# -------------------------
git add version.txt
if git diff --cached --quiet; then
  echo "ℹ️ Нет изменений для коммита"
else
  git commit -m "🔖 release $NEW_VERSION"
fi

if git rev-parse "v$NEW_VERSION" >/dev/null 2>&1; then
  echo "ℹ️ Тег v$NEW_VERSION уже существует"
else
  git tag -a "v$NEW_VERSION" -m "Release $NEW_VERSION ($BUILD_DATE)"
fi

# -------------------------
# Артефакт окружения для CI
# -------------------------
echo "VERSION=$NEW_VERSION" > version.env
echo "BUILD_DATE='$BUILD_DATE'" >> version.env
echo "CI_COMMITTER='$GITLAB_USER_NAME'" >> version.env
echo "PIPELINE_START_SEC=$PIPELINE_START_SEC" >> version.env

echo "🎯 Версия обновлена до $NEW_VERSION"
