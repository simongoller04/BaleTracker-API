FROM openjdk:17-jdk-alpine
MAINTAINER simon.goller4@gmail.com
COPY build/libs/baleTrackerAPI-1.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
