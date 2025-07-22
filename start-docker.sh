#!/usr/bin/env bash

set -e

# cp .env.template .env

chmod -R 777 ./data/mysql/init

docker compose down -v

mvn clean package -DskipTests

docker compose build gateway-service auth-service user-service order-service

docker compose up -d