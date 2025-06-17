#!/bin/bash

# build docker
docker build -f auth-service/Dockerfile -t ismarcochow/chatbot-auth-service:latest .
docker build -f user-service/Dockerfile -t ismarcochow/chatbot-user-service:latest .

# login docker hub
#docker login -u ismarcochow

# push image
docker push ismarcochow/chatbot-auth-service:latest
docker push ismarcochow/chatbot-user-service:latest