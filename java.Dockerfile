FROM openjdk:14

#RUN curl -o /tmp/apache-maven-3.6.3-bin.tar.gz https://muug.ca/mirror/apache-dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
#RUN tar zxvf /tmp/apache-maven-3.6.3-bin.tar.gz -C /opt/ && ln -s /opt/apache-maven-3.6.3 /opt/apache-maven

WORKDIR /app
COPY . .

#WORKDIR /app/mq-demos/mq-demos
#RUN /opt/apache-maven/bin/mvn compile
#CMD /opt/apache-maven/bin/mvn exec:java -Dexec.mainClass="ca.sfu.cmpt383.RPCServer"

WORKDIR /app/hello-world
RUN javac Hello.java
CMD java Hello
