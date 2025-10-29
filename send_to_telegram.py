from datetime import timedelta, datetime
from minio import Minio
from minio.error import S3Error
import requests
import os
import sys
import pytz

# === Настройки MinIO ===
MINIO_ENDPOINT = "minio-api.xire.ru"  # публичный домен через Nginx
MINIO_ACCESS_KEY = "admin"
MINIO_SECRET_KEY = "Angel1801"
MINIO_BUCKET = "backend"

# === Настройки Telegram ===
BOT_TOKEN = "7757079506:AAFSgH2M2EoUXM_avFHknClFd9u_fTqV0sU"
CHAT_ID = "-1003089402236"

# === Инициализация клиента ===
client = Minio(
    MINIO_ENDPOINT,
    access_key=MINIO_ACCESS_KEY,
    secret_key=MINIO_SECRET_KEY,
    secure=True,
)


def get_public_url(object_name: str) -> str | None:
    """Создать временную публичную ссылку на скачивание файла."""
    try:
        url = client.get_presigned_url(
            "GET",
            MINIO_BUCKET,
            object_name,
            expires=timedelta(hours=12)
        )
        print(f"🔗 Ссылка на скачивание: {url}")
        return url
    except S3Error as e:
        print(f"❌ Ошибка генерации ссылки: {e}")
        return None


def send_telegram_message(message: str):
    """Отправить сообщение в Telegram."""
    try:
        telegram_url = f"https://api.telegram.org/bot{BOT_TOKEN}/sendMessage"
        r = requests.post(telegram_url, data={
            "chat_id": CHAT_ID,
            "text": message,
            "parse_mode": "HTML",
            "disable_web_page_preview": False
        })
        if r.status_code == 200:
            print("✅ Сообщение отправлено в Telegram")
        else:
            print(f"❌ Ошибка Telegram ({r.status_code}): {r.text}")
    except Exception as e:
        print(f"❌ Ошибка при отправке в Telegram: {e}")


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("❌ Использование: python send_to_telegram.py <имя_файла>")
        sys.exit(1)

    object_name = sys.argv[1]
    object_name = object_name.split("/")[-1]
    download_url = get_public_url(object_name)

    if not download_url:
        print("❌ Не удалось получить ссылку на файл в MinIO.")
        sys.exit(1)

    # ==== Переменные окружения ====
    VERSION = object_name.split("-")[-1].split(".jar")[0]

    # Формат даты: "13 октября 2025, 15:29 (МСК)"
    BUILD_DATE = datetime.now(pytz.timezone("Europe/Moscow")).strftime("%d.%m.%Y, %H:%M (МСК)")

    CI_COMMIT_BRANCH = os.getenv("CI_COMMIT_BRANCH", "unknown")
    CI_COMMIT_SHA = os.getenv("CI_COMMIT_SHA", "")[:8]
    CI_PIPELINE_URL = os.getenv("CI_PIPELINE_URL", "").replace("http://10.8.0.2", "https://gitlab.xire.ru")
    CI_PROJECT_URL = os.getenv("CI_PROJECT_URL", "")
    CI_COMMIT_URL = f"https://gitlab.xire.ru/-/commit/{CI_COMMIT_SHA}" if CI_PROJECT_URL and CI_COMMIT_SHA else ""

    # Корректное вычисление времени пайплайна
    try:
        duration_sec = int(float(os.getenv("PIPELINE_DURATION_SEC", "0")))
    except ValueError:
        duration_sec = 0

    if duration_sec > 0:
        duration = timedelta(seconds=duration_sec)
        hours, remainder = divmod(duration_sec, 3600)
        minutes, seconds = divmod(remainder, 60)
        duration_str = f"{hours} ч {minutes} мин {seconds} сек"
    else:
        duration_str = "меньше минуты"

    # ==== Тип файла ====
    file_type = "JAR"

    caption = (
        f"🚀 <b>GeoBudget Backend — новая сборка!</b>\n\n"
        f"📦 <b>Версия:</b> {VERSION}\n"
        f"🕓 <b>Дата сборки:</b> {BUILD_DATE}\n"
        f"⏱ <b>Длительность pipeline:</b> {duration_str}\n"
        f"🌿 <b>Ветка:</b> {CI_COMMIT_BRANCH}\n"
        f"🔖 <b>Тип файла:</b> {file_type}\n\n"
        f"📥 <b>Скачать:</b> <a href=\"{download_url}\">{object_name}</a>\n\n"
        f"🔗 <b>Commit:</b> <a href=\"{CI_COMMIT_URL}\">{CI_COMMIT_SHA}</a>\n"
        f"⚙️ <b>Pipeline:</b> <a href=\"{CI_PIPELINE_URL}\">Открыть в GitLab</a>\n"
        f"🏷 <b>Тег GitLab:</b> <a href=\"https://gitlab.xire.ru/geobudget/backend/-/tags/v{VERSION}\">v{VERSION}</a>\n"
        f"🏷 <b>Тег GitHub:</b> <a href=\"https://github.com/YTXIRE/geobudget_backend/releases/tag/v{VERSION}\">v{VERSION}</a>\n"
        f"🐙 <b>GitHub репозиторий:</b> <a href=\"https://github.com/YTXIRE/geobudget_backend/tree/main\">Код на GitHub</a>\n"
        f"🐙 <b>GitLab репозиторий:</b> <a href=\"https://gitlab.xire.ru/geobudget/backend\">Код на GitLab</a>\n"
    )

    send_telegram_message(caption)
