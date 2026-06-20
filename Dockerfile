# ── Dockerfile multi-stage para ms-turno (turno-service) ──────────────
# ETAPA 1: Compilar con Maven
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /build

# Cache de dependencias (esta capa solo se regenera si cambia pom.xml)
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

# ETAPA 2: Solo el runtime (imagen final mas pequeña)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar SOLO el JAR generado desde la etapa builder
COPY --from=builder /build/target/*.jar app.jar

# Puerto que expone el contenedor (documentacion)
EXPOSE 8086

# Variables de entorno con valores por defecto (se sobreescriben en cloud/compose)
ENV SPRING_PROFILES_ACTIVE=docker
ENV PORT=8086

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
