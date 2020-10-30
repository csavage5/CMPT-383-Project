FROM ruby:2.7

#RUN gem install bunny -v 2.17.0
#RUN gem install ezmq -v 0.4.12

# for ZeroMQ server
#EXPOSE 5555

WORKDIR /app
COPY . .

CMD ruby hello-world/hello.rb
