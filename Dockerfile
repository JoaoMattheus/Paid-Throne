FROM ubuntu:latest as build

RUN  apt-get update && apt-get install -y openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/calculator-0.0.1-SNAPSHOT.jar /app/calculator.jar

ENTRYPOINT [ "java", "-jar", "/app/calculator.jar" ]