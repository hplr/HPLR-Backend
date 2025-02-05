FROM maven:3.9-eclipse-temurin-22-alpine AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:resolve-plugins

COPY src/main src/main

RUN mvn package -DskipUnitTests -DskipIT -DskipPitest -DskipPi=true

FROM eclipse-temurin:22-jre-alpine

WORKDIR /app

COPY --from=build /app/target/ranking-0.1.jar .

EXPOSE 8082

CMD ["java", "-jar", "ranking-0.1.jar"]
