FROM openjdk:17-jdk-slim AS build

WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY ./src ./src

RUN ./mvnw package -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim

WORKDIR /app

ENV APP_HOME app
ENV APP_NAME api.jar

COPY --from=build /app/target/poc.jar /${APP_HOME}/${APP_NAME}

ENTRYPOINT java -jar /${APP_HOME}/${APP_NAME}

EXPOSE 8080