FROM openjdk:17

WORKDIR /app

COPY ./target/api-saldo-transferencias-0.0.1-SNAPSHOT.jar api-saldo-transferencias-0.0.1-SNAPSHOT.jar

EXPOSE 8081

ENTRYPOINT java -jar api-saldo-transferencias-0.0.1-SNAPSHOT.jar