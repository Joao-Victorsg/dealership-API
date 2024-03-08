FROM alpine:latest
ENV tz=gmt-3
RUN apk --update add openjdk17

RUN apk update && apk upgrade

COPY target/dealership-0.0.1-SNAPSHOT.jar dealership.jar

EXPOSE 8085

ENTRYPOINT [ "java","-jar","/dealership.jar"]