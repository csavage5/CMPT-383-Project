FROM python:3.8

RUN pip3 install flask

WORKDIR /app
COPY ./flaskWebserver .

EXPOSE 8888
RUN ls -lR
#ENTRYPOINT [ "python3", "-d", "server.py" ]
CMD python3 server.py
