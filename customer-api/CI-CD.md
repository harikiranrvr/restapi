# CI/CD Pipeline Documentation

This document describes the recommended CI/CD pipeline for the Customer API.

## Pipeline Overview
1. **Build**: Compile the code and run unit tests.
2. **Containerize**: Build a Docker image for the application.
3. **Push Image**: Push the Docker image to a container registry (e.g., Docker Hub, ECR, GCR).
4. **Deploy to Kubernetes**: Apply Kubernetes manifests to the target cluster.
5. **Gates**: Manual and automated checks at key stages.

---

## Example Pipeline Steps

### 1. Build & Test
- Trigger: On push or pull request to main/dev branches
- Actions:
  - `mvn clean verify`
  - Fail pipeline if tests do not pass

### 2. Containerize
- Actions:
  - `docker build -t <registry>/customer-api:<tag> .`

### 3. Push Image
- Actions:
  - Authenticate to registry
  - `docker push <registry>/customer-api:<tag>`

### 4. Deploy to Kubernetes
- Actions:
  - Update image tag in `k8s-deployment.yaml` (or use `kubectl set image`)
  - `kubectl apply -f k8s-deployment.yaml`

### 5. Gates
- **Automated:**
  - Unit test pass/fail
  - Linting/static analysis
  - Image vulnerability scan
- **Manual:**
  - Approval required before deploying to production

---

## Example: GitHub Actions Workflow

```yaml
name: CI/CD Pipeline
on:
  push:
    branches: [ main, dev ]
  pull_request:
    branches: [ main, dev ]

jobs:
  build-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'
      - name: Build with Maven
        run: mvn clean verify

  docker:
    needs: build-test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Build Docker image
        run: docker build -t ${{ secrets.REGISTRY }}/customer-api:${{ github.sha }} .
      - name: Log in to registry
        run: echo ${{ secrets.REGISTRY_PASSWORD }} | docker login ${{ secrets.REGISTRY }} -u ${{ secrets.REGISTRY_USERNAME }} --password-stdin
      - name: Push Docker image
        run: docker push ${{ secrets.REGISTRY }}/customer-api:${{ github.sha }}

  deploy:
    needs: docker
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
      - uses: actions/checkout@v3
      - name: Set up kubectl
        uses: azure/setup-kubectl@v3
        with:
          version: 'latest'
      - name: Deploy to Kubernetes
        run: |
          kubectl set image deployment/customer-api customer-api=${{ secrets.REGISTRY }}/customer-api:${{ github.sha }}
          kubectl apply -f k8s-deployment.yaml
```

---

## Notes
- Store sensitive values (registry credentials, kubeconfig) as CI/CD secrets.
- Add additional jobs for code quality, security scanning, or integration tests as needed.
- Manual approval can be added before the `deploy` job for production environments. 