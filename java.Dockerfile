
# https://codefresh.io/docs/docs/learn-by-example/java/gradle/
FROM gradle:6.7.0-jdk14 AS build
COPY --chown=gradle:gradle ./spotifyCLI /home/gradle/src
WORKDIR /home/gradle/src
# RUN gradle build
RUN gradle build --no-daemon 
#RUN ls -lR


FROM openjdk:14
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spotifyCLI.jar
# from https://stackoverflow.com/questions/51121875/how-to-run-docker-with-python-and-java
COPY --from=rocker/r-base:3.6.3 / /

WORKDIR /app
COPY ./spotifyCLI/CSVConverter.R /app
COPY ./spotifyCLI/install_packages.R /app

# from https://stackoverflow.com/questions/45289764/install-r-packages-using-docker-file
RUN Rscript /app/install_packages.R



# ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spotifyCLI.jar"]
CMD java -jar /app/spotifyCLI.jar
