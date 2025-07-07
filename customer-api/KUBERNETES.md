# Kubernetes Deployment Instructions

This document explains how to deploy the Customer API to a Kubernetes cluster (Minikube, Kind, or other).

## Prerequisites
- Docker image for the API built and available locally (for Minikube/Kind) or pushed to a registry (for remote clusters)
- Kubernetes cluster (Minikube, Kind, or other)
- `kubectl` installed and configured

## 1. Build and Load Docker Image

### For Minikube:
```sh
# Set Docker env to Minikube
eval $(minikube docker-env)
# Build the image inside Minikube's Docker
cd customer-api
docker build -t customer-api:latest .
```

### For Kind:
```sh
# Build the image locally
cd customer-api
docker build -t customer-api:latest .
# Load the image into Kind
kind load docker-image customer-api:latest
```

### For Remote Clusters:
- Push the image to a registry (e.g., Docker Hub, ECR, GCR) and update `k8s-deployment.yaml` with the correct image name.

## 2. Deploy to Kubernetes
```sh
kubectl apply -f k8s-deployment.yaml
```

## 3. Access the API
- For Minikube:
  ```sh
  minikube service customer-api-service
  ```
- For Kind or other clusters:
  - Access via `NodePort` on port 30080 of your node, or update the Service type as needed.

## 4. Customization
- Edit `k8s-deployment.yaml` to:
  - Change image name/tag
  - Set environment variables
  - Adjust resource requests/limits
  - Add ConfigMaps, Secrets, etc.

## 5. Extensibility
- This manifest is suitable for local testing and can be extended for production (add Ingress, persistent storage, autoscaling, etc.).
- For production, use a private registry and update the image reference accordingly. 