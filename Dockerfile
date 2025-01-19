FROM openjdk:17-jdk-slim

WORKDIR /app

COPY application/build/libs/application-1.0-SNAPSHOT.jar /app/application.jar
COPY domain/build/libs/domain-1.0-SNAPSHOT.jar /app/domain.jar
COPY build/libs/production-1.0-SNAPSHOT.jar /app/production.jar

ENTRYPOINT ["sh", "-c", "java -jar /app/application.jar & java -jar /app/domain.jar & java -jar /app/production.jar"]