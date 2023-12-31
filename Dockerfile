# Pobranie obrazu Javy z JDK
FROM maven:3-eclipse-temurin-20-alpine AS builder

# Utworzenie katalogu roboczego wewnątrz obrazu
WORKDIR /app

# Skopiowanie plików z kodem aplikacji do obrazu
COPY pom.xml .
COPY src ./src

# Zbudowanie aplikacji Java Spring Boot
RUN mvn package -DskipTests

# Obraz docelowy
FROM openjdk:20-jdk-slim

# Utworzenie katalogu roboczego wewnątrz obrazu
WORKDIR /app
# Skopiowanie skompilowanego pliku JAR z etapu poprzedniego
COPY --from=builder /app/target/measurement-0.0.1.jar .
COPY src/main/resources/data.sql .
RUN mkdir /app/printerImages
COPY src/main/java/com/filament/measurement/Printer/Images/ /app/printerImages

EXPOSE 8080
# Określenie punktu wejścia dla kontenera Docker
ENTRYPOINT ["java", "-jar", "measurement-0.0.1.jar"]
