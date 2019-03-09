FROM openjdk:8-jdk-alpine

WORKDIR /tourapp

COPY target/tour-0.0.1-SNAPSHOT.jar tour.jar

WORKDIR data

RUN mkdir conf
RUN mkdir user-data

RUN echo $'spring:\n\
    application:\n\
        name: Tour Application\n' \
> conf/application.yml

ENTRYPOINT ["java", "-jar", "../tour.jar"]

EXPOSE 8822
