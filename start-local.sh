#!/usr/bin/env bash

set -e

# Stop and remove containers and volumes
docker compose down -v

chmod -R 777 ./data/localstack/init
chmod -R 777 ./data/mysql/init

docker compose up localstack auth-db product-db redis -d