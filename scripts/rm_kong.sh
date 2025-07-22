#!/bin/bash

kubectl get all,ingress -A | grep -i "kong"
helm list -A | grep -i "kong"

helm uninstall kong -n mcshop

pkill -f "minikube tunnel"

kubectl get all,ingress,pvc -A | grep -i "kong"

minikube delete
minikube start