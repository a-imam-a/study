# Базовый образ, содержащий Java 17
FROM openjdk:17-oracle

# Директория приложения внутри контейнера
WORKDIR /app

# Копирование Jar-файла приложения в контейнер
COPY build/libs/student_accounting_console-0.0.1-SNAPSHOT.jar app.jar

# Определение переменой среды
ENV CREATE_ON_STARTUP=false

# Команда для запуска приложения
CMD ["java", "-jar", "app.jar"]

