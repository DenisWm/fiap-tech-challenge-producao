FROM gradle:7.5 AS build

WORKDIR /app

COPY . .

RUN gradle build --no-daemon

FROM openjdk:17.0.2-slim-bullseye

COPY --from=build /app/application/build/libs/application-1.0-SNAPSHOT.jar /app/application.jar
COPY --from=build /app/domain/build/libs/domain-1.0-SNAPSHOT.jar /app/domain.jar
COPY --from=build /app/build/libs/production-1.0-SNAPSHOT.jar /app/production.jar

RUN apt-get update && apt-get install -y curl

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar /app/application.jar & java -jar /app/domain.jar & java -jar /app/production.jar"]
