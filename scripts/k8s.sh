#!/bin/bash

kubectl apply -f namespace.yaml

# Create the ConfigMap for the init sql
kubectl create configmap mysql-init \
  --namespace=mcshop \
  --from-file=../data/mysql/init/mcshop.sql

kubectl apply -f mysql.yaml

kubectl apply -f redis.yaml

# Create a ConfigMap for environment variables, a Deployment, and a Service for auth-service.
kubectl create secret generic auth-secrets \
  --from-literal=smtp-username="ismarcochow@gmail.com" \
  --from-literal=smtp-password="hjgx awcr iivl rejr" \
  -n mcshop

kubectl apply -f auth-service.yaml
kubectl apply -f user-service.yaml

kubectl get pods -n mcshop


# logs
kubectl logs -f -l app=auth-service -n mcshop

# rollout
kubectl rollout restart deployment/auth-service -n mcshop

# dashboard
kubectl -n kubernetes-dashboard create serviceaccount dashboard-admin

kubectl create clusterrolebinding dashboard-admin-binding \
  --clusterrole=cluster-admin \
  --serviceaccount=kubernetes-dashboard:dashboard-admin

kubectl -n kubernetes-dashboard create token dashboard-admin

