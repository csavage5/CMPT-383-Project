FROM rocker/tidyverse:latest
#RUN curl -o /tmp/apache-maven-3.6.3-bin.tar.gz https://muug.ca/mirror/apache-dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
#RUN tar zxvf /tmp/apache-maven-3.6.3-bin.tar.gz -C /opt/ && ln -s /opt/apache-maven-3.6.3 /opt/apache-maven
WORKDIR /app

# from https://stackoverflow.com/questions/45289764/install-r-packages-using-docker-file
RUN install2.r --error \
    jsonlite

#WORKDIR /app/mq-demos/mq-demos
#RUN /opt/apache-maven/bin/mvn compile
#CMD /opt/apache-maven/bin/mvn exec:java -Dexec.mainClass="ca.sfu.cmpt383.RPCServer"

# FROM gradle:4.7.0-jdk8-alpine AS build
# COPY --chown=gradle:gradle . /home/gradle/src
# WORKDIR /home/gradle/src
# RUN gradle build --no-daemon 

# FROM gradle:jdk14 as builder

# COPY --chown=gradle:gradle . /home/gradle/src
# WORKDIR /home/gradle/src
# RUN gradle build


# https://stackoverflow.com/questions/61108021/gradle-and-docker-how-to-run-a-gradle-build-within-docker-container
# temp container to build using gradle
# FROM gradle:5.3.0-jdk-alpine AS TEMP_BUILD_IMAGE
# ENV APP_HOME=/usr/app/
# WORKDIR $APP_HOME
# COPY build.gradle settings.gradle $APP_HOME

# COPY gradle $APP_HOME/gradle
# COPY --chown=gradle:gradle . /home/gradle/src
# USER root
# RUN chown -R gradle /home/gradle/src

# RUN gradle build || return 0
# COPY . .
# RUN gradle clean build


# https://codefresh.io/docs/docs/learn-by-example/java/gradle/
FROM gradle:6.7.0-jdk14 AS build
COPY --chown=gradle:gradle ./spotifyCLI /home/gradle/src
WORKDIR /home/gradle/src
# RUN gradle build
RUN gradle build --no-daemon 
RUN ls -lR


FROM openjdk:14
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spotifyCLI.jar
# ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spotifyCLI.jar"]
CMD java -jar /app/spotifyCLI.jar

# WORKDIR /app
# COPY ./spotifyCLI .
# RUN pwd
# RUN ls -lR

# RUN javac -d . src/main/java/cmpt383/*.java

# #RUN javac ActionController.java AuthenticationController.java InputManager.java JSONUtility.java Main.java OutputController.java QueryManager.java

# CMD ["java", "Main"]

# WORKDIR /app/hello-world
# RUN javac Hello.java
# CMD java Hello
