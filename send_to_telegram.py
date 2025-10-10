from datetime import timedelta
from minio import Minio
from minio.error import S3Error
import requests
import os

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

def delete_old_version(object_name: str):
    """Удалить старую версию файла, если она есть"""
    try:
        found = client.stat_object(MINIO_BUCKET, object_name)
        if found:
            client.remove_object(MINIO_BUCKET, object_name)
            print(f"🗑 Удалена старая версия {object_name}")
    except S3Error as e:
        if e.code != "NoSuchKey":
            print("Ошибка при проверке старой версии:", e)

def upload_file(file_path: str):
    """Загрузить новую версию файла GeoBudget_app-release.apk"""
    object_name = "GeoBudget_app.jar"
    try:
        delete_old_version(object_name)
        client.fput_object(MINIO_BUCKET, object_name, file_path)
        print(f"✅ Загружен новый файл: {object_name}")
        return object_name
    except S3Error as e:
        print("Ошибка загрузки:", e)
        return None

def get_public_url(object_name: str):
    """Создать временную публичную ссылку на скачивание"""
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
        print("Ошибка генерации ссылки:", e)
        return None

def send_telegram_message(message: str):
    """Отправить текстовое сообщение в Telegram"""
    try:
        telegram_url = f"https://api.telegram.org/bot{BOT_TOKEN}/sendMessage"
        r = requests.post(telegram_url, data={
            "chat_id": CHAT_ID,
            "text": message,
            "disable_web_page_preview": False,
            "parse_mode": "HTML"
        })
        if r.status_code == 200:
            print("✅ Сообщение отправлено в Telegram")
        else:
            print("❌ Ошибка Telegram:", r.status_code, r.text)
    except Exception as e:
        print("Ошибка при отправке в Telegram:", e)

# === Основной поток ===
if __name__ == "__main__":
    local_file = "build/libs/geobudget-1.0.0.jar"

    uploaded = upload_file(local_file)
    if uploaded:
        download_url = get_public_url(uploaded)
        if download_url:
            caption = (
                f"📱 Новая версия GeoBudget Backend доступна для скачивания:\n\n"
                f'<a href="{download_url}">Скачать GeoBudget Backend</a>'
            )
            send_telegram_message(caption)
