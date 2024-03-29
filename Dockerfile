#FROM ubuntu:latest AS build
#RUN apt-get update
#RUN apt-get install openjdk-17-jdk -y
##FROM openjdk:17-jdk-alpine
#MAINTAINER simon.goller4@gmail.com
#COPY build/libs/baleTrackerAPI-1.jar /app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

#FROM ubuntu:latest AS build
#RUN apt-get update
#RUN apt-get install openjdk-17-jdk -y
#COPY build/libs/baleTrackerAPI-1.jar /app.jar
#RUN ./gradlew bootJar --no-deamon
#
#FROM openjdk:17-jdk-slim
#EXPOSE 8080
#COPY --from=build /build/libs/baleTrackerAPI-1.jar app.jar
#
#ENTRYPOINT [ "java", "-jar", "app.jar" ]

FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y

WORKDIR /baleTrackerAPI
COPY build/libs/baleTrackerAPI-1.jar app.jar

EXPOSE 8080

CMD [ "java", "-jar", "app.jar" ]
