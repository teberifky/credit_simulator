# ---- build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package

# ---- runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /build/target/credit_simulator.jar /app/credit_simulator.jar
ENTRYPOINT ["java", "-jar", "/app/credit_simulator.jar"]
