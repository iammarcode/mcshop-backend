#!/bin/bash

kubectl apply -f namespace.yaml

# Create the ConfigMap for the init sql
kubectl create configmap mysql-init \
  --namespace=chatbot \
  --from-file=../data/mysql/init/chatbot.sql

kubectl apply -f mysql.yaml

kubectl apply -f redis.yaml

# Create a ConfigMap for environment variables, a Deployment, and a Service for auth-service.
kubectl create secret generic auth-secrets \
  --from-literal=smtp-username="ismarcochow@gmail.com" \
  --from-literal=smtp-password="hjgx awcr iivl rejr" \
  -n chatbot

kubectl apply -f auth-service.yaml
kubectl apply -f user-service.yaml

kubectl get pods -n chatbot


# logs
kubectl logs -f -l app=auth-service -n chatbot

# rollout
kubectl rollout restart deployment/auth-service -n chatbot

# dashboard
kubectl -n kubernetes-dashboard create serviceaccount dashboard-admin

kubectl create clusterrolebinding dashboard-admin-binding \
  --clusterrole=cluster-admin \
  --serviceaccount=kubernetes-dashboard:dashboard-admin

kubectl -n kubernetes-dashboard create token dashboard-admin

