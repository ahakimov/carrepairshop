FROM maven:3.9.12-eclipse-temurin-21-alpine as MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/car-repair-shop-0.0.1-SNAPSHOT.jar /app/car-repair-shop.jar

ENTRYPOINT ["java", "-jar", "car-repair-shop.jar"]

EXPOSE 8080