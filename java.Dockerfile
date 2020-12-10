
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
WORKDIR /app

COPY --from=r-base:4.0.3 / /


COPY ./spotifyCLI/CSVConverter.r /app
COPY ./spotifyCLI/install_packages.r /app

# from https://stackoverflow.com/questions/45289764/install-r-packages-using-docker-file
RUN Rscript /app/install_packages.r



# ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spotifyCLI.jar"]
CMD java -jar /app/spotifyCLI.jar
