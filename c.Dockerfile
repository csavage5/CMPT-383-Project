FROM debian:10

RUN apt-get update \
  && apt-get install -y build-essential gcc g++ clang \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

RUN gcc -Wall -o hello hello-world/hello.c
#RUN clang -Wall -o hello hello-world/hello.c
#RUN g++ -Wall -o hello hello-world/hello.cpp
#RUN clang++ -Wall -o hello hello-world/hello.cpp

CMD ./hello
