FROM maven:3.9-eclipse-temurin-22-alpine AS build

WORKDIR /app

COPY pom.xml .

COPY bootstrap/pom.xml bootstrap/
COPY ELO/pom.xml ELO/
COPY game/pom.xml game/
COPY library/pom.xml library/
COPY location/pom.xml location/
COPY tournament/pom.xml tournament/
COPY user/pom.xml user/

RUN mvn dependency:resolve-plugins

COPY bootstrap/src/main bootstrap/src/main
COPY ELO/src/main ELO/src/main
COPY game/src/main game/src/main
COPY library/src/main library/src/main
COPY location/src/main location/src/main
COPY tournament/src/main tournament/src/main
COPY user/src/main user/src/main

RUN mvn package -DskipUnitTests -DskipIT -DskipPitest -DskipPi=true

FROM eclipse-temurin:22-jre-alpine

WORKDIR /app

COPY --from=build /app/bootstrap/target/bootstrap-0.1.war .

RUN apk --no-cache add curl

EXPOSE 8082

CMD ["java", "-jar", "bootstrap-0.1.war"]
