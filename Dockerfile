# Base Image for JDK
# Stage 1 - Build stage

FROM eclipse-temurin:21-jdk AS builder

# Set the working directory inside the container
WORKDIR /app

COPY mvnw pom.xml ./

COPY .mvn .mvn

RUN chmod +x mvnw

# Download all Maven dependencies
# This layer is cached unless pom.xml changes
RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw clean package -DskipTests

# ---------- Stage 2: Run the application ----------

FROM eclipse-temurin:21-jre

RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder app/target/*.jar app.jar
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]