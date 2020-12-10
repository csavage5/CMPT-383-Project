FROM rocker/tidyverse:latest
#RUN curl -o /tmp/apache-maven-3.6.3-bin.tar.gz https://muug.ca/mirror/apache-dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
#RUN tar zxvf /tmp/apache-maven-3.6.3-bin.tar.gz -C /opt/ && ln -s /opt/apache-maven-3.6.3 /opt/apache-maven
WORKDIR /app

# from https://stackoverflow.com/questions/45289764/install-r-packages-using-docker-file
RUN install2.r --error \
    jsonlite


# https://codefresh.io/docs/docs/learn-by-example/java/gradle/
FROM gradle:6.7.0-jdk14 AS build
COPY --chown=gradle:gradle ./spotifyCLI /home/gradle/src
WORKDIR /home/gradle/src
# RUN gradle build
RUN gradle build --no-daemon 
#RUN ls -lR


FROM openjdk:14
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spotifyCLI.jar
#RUN ls -lR

# ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spotifyCLI.jar"]
CMD java -jar /app/spotifyCLI.jar
