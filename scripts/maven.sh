#!/bin/bash

# Create a sub module maven project
mvn archetype:generate \
  -DgroupId=com.marco \
  -DartifactId=mcshop-YOUR_SERVICE_NAME \
  -Dversion=1.0.0 \
  -DinteractiveMode=false \
  -Dpackage=com.marco.mcshop.YOUR_SERVICE_NAME
