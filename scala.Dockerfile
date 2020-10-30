FROM hseeberger/scala-sbt:11.0.8_1.4.0_2.13.3

WORKDIR /app
COPY . .

CMD scala hello-world/hello.scala
