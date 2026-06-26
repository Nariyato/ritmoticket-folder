FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="Triskeledu"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "app.jar"]
