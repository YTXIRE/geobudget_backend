import os
import sys
from minio import Minio
from minio.error import S3Error

# === Константы MinIO ===
MINIO_ENDPOINT = "minio-api.xire.ru"  # через Nginx (https)
MINIO_ACCESS_KEY = "admin"
MINIO_SECRET_KEY = "Angel1801"
MINIO_BUCKET = "backend"

# === Инициализация клиента ===
client = Minio(
    MINIO_ENDPOINT,
    access_key=MINIO_ACCESS_KEY,
    secret_key=MINIO_SECRET_KEY,
    secure=True,
)


def delete_old_version(object_name: str):
    """Удалить старую версию файла, если она уже есть в бакете."""
    try:
        client.stat_object(MINIO_BUCKET, object_name)
        client.remove_object(MINIO_BUCKET, object_name)
        print(f"🗑 Удалена старая версия: {object_name}")
    except S3Error as e:
        if e.code != "NoSuchKey":
            print(f"⚠️ Ошибка при удалении старой версии {object_name}: {e}")


def upload_file(file_path: str, object_name: str):
    """Загрузить файл в MinIO с заменой старой версии."""
    if not os.path.exists(file_path):
        print(f"❌ Файл не найден: {file_path}")
        sys.exit(1)

    try:
        delete_old_version(object_name)
        client.fput_object(MINIO_BUCKET, object_name, file_path)
        print(f"✅ Файл загружен в MinIO: {object_name}")
    except S3Error as e:
        print(f"❌ Ошибка при загрузке {object_name}: {e}")
        sys.exit(1)


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("❌ Использование: python upload_minio.py <путь_к_файлу>")
        sys.exit(1)

    file_path = sys.argv[1]
    file_name = os.path.basename(file_path)
    upload_file(file_path, file_name)
