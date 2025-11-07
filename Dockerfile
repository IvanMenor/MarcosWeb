# ================================
# Etapa 1: Construcción con Maven
# ================================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ================================
# Etapa 2: Imagen final para ejecutar
# ================================
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiamos el JAR generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Render usa la variable de entorno PORT, así que se debe respetar
ENV PORT=8080
EXPOSE 8080

# Comando para ejecutar la app
CMD ["java", "-jar", "app.jar"]
