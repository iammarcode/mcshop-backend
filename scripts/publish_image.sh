#!/bin/bash

# build docker
docker build -f auth-service/Dockerfile -t ismarcochow/mcshop-auth-service:latest .
docker build -f user-service/Dockerfile -t ismarcochow/mcshop-user-service:latest .

# login docker hub
# docker login -u ismarcochow

# push image
docker push ismarcochow/mcshop-auth-service:latest
docker push ismarcochow/mcshop-user-service:latest