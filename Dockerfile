FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/recap.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8081