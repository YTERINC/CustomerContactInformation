# Этап сборки
FROM maven:latest AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и загружаем зависимости
COPY pom.xml .
COPY src ./src

# Сборка приложения без тестов
RUN mvn clean package -DskipTests

# Этап выполнения
FROM openjdk:latest

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR файл из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]