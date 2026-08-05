FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY src/Order.Api/pom.xml src/Order.Api/
RUN mvn -f src/Order.Api/pom.xml dependency:go-offline
COPY src/Order.Api/src src/Order.Api/src
RUN mvn -f src/Order.Api/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /src/src/Order.Api/target/order-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
