#!/bin/sh
set -e

# -------------------------
# Настройки Git
# -------------------------
apk add --no-cache git >/dev/null 2>&1 || true
git config user.name "GitLab CI"
git config user.email "ci@$CI_SERVER_HOST"

# -------------------------
# Получаем версию из артефакта
# -------------------------
. version.env
echo "🔖 Создаём или перезаписываем тег для версии $VERSION"

# -------------------------
# Создаём или перезаписываем тег
# -------------------------
git tag -fa "v$VERSION" -m "Release $VERSION ($BUILD_DATE)"

# -------------------------
# Пушим тег на GitLab с перезаписью
# -------------------------
git remote set-url origin "http://root:glpat-IIgoeOxBmpkDPvjCJBWtHW86MQp1OjEH.01.0w0tszlsk@${CI_SERVER_HOST}/${CI_PROJECT_PATH}.git"
git push origin "v$VERSION" --force

echo "✅ Тег v$VERSION создан/перезаписан и отправлен"
