# Сборка
FROM eclipse-temurin:24-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

# Финальный образ
FROM eclipse-temurin:24-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ARG JWT_SECRET
ARG DADATA_API_KEY
ARG DADATA_X_SECRET
ARG PROVERKACHECKA_API_KEY
RUN printf '%s\n' \
  '# Generated at build time' \
  '' \
  '# Application secrets' \
  "JWT_SECRET=${JWT_SECRET}" \
  '' \
  '# Dadata' \
  "DADATA_API_KEY=${DADATA_API_KEY}" \
  "DADATA_X_SECRET=${DADATA_X_SECRET}" \
  '' \
  '# Proverkacheka' \
  "PROVERKACHECKA_API_KEY=${PROVERKACHECKA_API_KEY}" \
  > .env
EXPOSE 8180
ENTRYPOINT ["java", "-jar", "app.jar"]
