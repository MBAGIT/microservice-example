FROM openjdk:latest

COPY target/customer-rest-ws-micro-service-0.0.1-SNAPSHOT.jar /usr/src/customer-rest-ws-micro-service-0.0.1-SNAPSHOT.jar


CMD java -jar /usr/src/customer-rest-ws-micro-service-0.0.1-SNAPSHOT.jar