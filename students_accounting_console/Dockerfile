# Базовый образ, содержащий Java 17
FROM openjdk:17-oracle

# Директория приложения внутри контейнера
WORKDIR /app

# Копирование Jar-файла приложения в контейнер
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Определение переменой среды
ENV CREATE_ON_STARTUP=false

# Команда для запуска приложения
CMD ["java", "-jar", "app.jar"]

