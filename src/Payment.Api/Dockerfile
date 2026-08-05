FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY src/Payment.Api/pom.xml src/Payment.Api/
RUN mvn -f src/Payment.Api/pom.xml dependency:go-offline
COPY src/Payment.Api/src src/Payment.Api/src
RUN mvn -f src/Payment.Api/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /src/src/Payment.Api/target/payment-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
