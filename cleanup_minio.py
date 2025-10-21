from minio import Minio
from datetime import datetime, timezone, timedelta

# Параметры MinIO из переменных окружения
MINIO_ENDPOINT = "minio-api.xire.ru"  # публичный домен через Nginx
MINIO_ACCESS_KEY = "admin"
MINIO_SECRET_KEY = "Angel1801"
MINIO_BUCKET = "backend"
RETENTION_DAYS = 3


client = Minio(
    MINIO_ENDPOINT,
    access_key=MINIO_ACCESS_KEY,
    secret_key=MINIO_SECRET_KEY,
    secure=True
)

cutoff = datetime.now(timezone.utc) - timedelta(days=RETENTION_DAYS)

for obj in client.list_objects(MINIO_BUCKET, recursive=True):
    print(obj.object_name)
    if obj.last_modified < cutoff:
        print(f"Удаляем {obj.object_name}, последний апдейт {obj.last_modified}")
        client.remove_object(MINIO_BUCKET, obj.object_name)
