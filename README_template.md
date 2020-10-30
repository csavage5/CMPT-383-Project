This is the template for CMPT 383 projects using Docker, providing some basics to get started with various languages in a a consistent environment.


## Starting the Containers

This repository is for code that deploys in a series of containers, managed by [Docker Compose](https://docs.docker.com/compose/). You should be able to change into the repository directory, uncomment whatever pieces you want in `docker-compose.yml`, and type:
```sh
docker-compose build && docker-compose up
```
&hellip; to bring up the containers.

If you'd like to shut down or destroy the containers, you can do it with these commands:
```sh
docker-compose stop
docker-compose rm
```


## The Docker Compose Config

This repository contains a `docker-compose.yml` file that is mostly commented-out. For each programming language where sample code is provided, there is a block like this:

```yaml
  hostname:
    build:
      context: .
      dockerfile: python.Dockerfile
```

The first line is the name of the container: this is the hostname if you want to contact it over the Docker internal network, and how you can refer to it when using the `docker-compose` command.

The last line here refers to a Dockerfile that is used to describe the image you want to run for this component/language.


## The Dockerfiles

There are several `*.Dockerfile` files included here, one for each language where there is example code. They have a structure like this (annotated with comments):

```dockerfile
FROM python:3.8  # the "base image" from https://hub.docker.com/ we're starting with

# build commands to run when creating the image
RUN pip3 install pika==1.1.0  # run this Linux command
WORKDIR /app                  # change to this directory
COPY . .                      # copy the repository code into /app in the container

# the command to run when the container is started
CMD python3 mq-demos/amqp_server.py
```

If you would like to re-build container images (i.e. re-do any build commands like `RUN` or `COPY`), you can ask:
```sh
docker-compose build
```

## Hello Worlds

In `hello-world/`, there are &ldquo;hello world&rdquo; programs for all of the languages mentioned in this repo. Each Dockerfile here has a `CMD` that will run the &ldquo;hello world&rdquo; when the container is started

Hopefully this will at least get things started running code in various languages in this context.


## Messaging Demo Code

In `mq-demos/`, you will find several examples of working with message queues. All of the programs start with a comment that indicates the command to run them.

All of the RPC examples do the same basic operation: do a remote procedure call by passing a [JSON](https://en.wikipedia.org/wiki/JSON) string. For each demo, the request includes a string; we ask the remote code to calculate its length, and return the result as a JSON response. These should be easily extensible to have whatever information is needed in the request/response messages, and whatever logic in between.


### RabbitMQ Demo Code

Similarly, the `amqp_*` programs are examples of an RPC call using [RabbitMQ](https://www.rabbitmq.com/) (using its underlying [AMQP](https://en.wikipedia.org/wiki/Advanced_Message_Queuing_Protocol) protocol). All of these require the RabbitMQ server (the container `rabbitmq` from the `docker-compose.yml`) to be running, and assume they can connect with no username/password (which is the default).

The `amqp_client.*` programs create a request consisting of a single argument (called `"string"`), encode it as JSON, make a request through RabbitMQ in the `rpc_queue` queue, get the JSON response, decode the JSON, and print the result.

The `amqp_server.*` programs all listen on the `rpc_queue` queue. When a request comes in, they decode the JSON, extract the argument (`"string"`), calculate the length of the string, create a response (containing the `"length"`), encode it as JSON, and send it back to the requester.

All of these programs can interact with each other: you can start any of the `amqp_server.*` programs, and run any of the `amqp_client.*` programs to ask it to calculate a string length.


