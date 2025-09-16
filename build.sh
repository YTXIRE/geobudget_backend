#!/bin/bash
APP_NAME=geobudget-backend
REGISTRY=localhost:5000

echo "🧹 Удаляем старый образ..."
docker rmi -f $REGISTRY/$APP_NAME:latest || true

echo "📦 Собираем образ..."
docker build -t $REGISTRY/$APP_NAME:latest .

echo "📤 Пушим в реестр..."
docker push $REGISTRY/$APP_NAME:latest
