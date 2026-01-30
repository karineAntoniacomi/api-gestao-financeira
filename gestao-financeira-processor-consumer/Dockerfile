FROM eclipse-temurin:17-jdk

WORKDIR /app

ARG JAR_FILE=target/gestao-financeira-processor-consumer.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8083

ENTRYPOINT ["java","-jar","app.jar"]
