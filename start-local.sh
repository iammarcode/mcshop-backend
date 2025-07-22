#!/usr/bin/env bash

set -e

# cp .env.template .env

chmod -R 777 ./data/mysql/init

docker compose down -v

docker compose up mcshop-db redis rabbitmq -d