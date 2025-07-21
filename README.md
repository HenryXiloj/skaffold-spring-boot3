# Skaffold Spring Boot 3 Demo

A comprehensive example of deploying Spring Boot 3 applications locally using **Skaffold**, **Helm**, **Docker**, and **Kubernetes**.

📘 Blog Post: [Deploying Spring Boot Apps Locally with Skaffold, Helm and Docker for Kubernetes](https://jarmx.blogspot.com/2023/11/deploying-spring-boot-apps-locally-with.html)

## 🚀 Overview

This project demonstrates how to:
- Build and containerize Spring Boot 3 applications with Docker
- Deploy to local Kubernetes using Helm charts
- Automate the development workflow with Skaffold
- Set up local development environment with hot-reload capabilities

## 📋 Prerequisites

Before getting started, ensure you have the following tools installed:

### Required Tools

| Tool | Version | Installation Guide |
|------|---------|-------------------|
| **Skaffold** | v2.9.0+ | [Official Installation Guide](https://skaffold.dev/docs/install/) |
| **Helm** | v3.13.0+ | [Helm Installation Guide](https://helm.sh/docs/intro/install/) |
| **Docker Desktop** | Latest | [Docker Desktop](https://www.docker.com/products/docker-desktop/) |
| **Kubectl** | Latest | Included with Docker Desktop |

### Alternative Kubernetes Options
- **Minikube**: [Installation Guide](https://minikube.sigs.k8s.io/docs/start/)
- **Kind**: [Installation Guide](https://kind.sigs.k8s.io/docs/user/quick-start/)

## 🛠️ Environment Setup

### 1. Enable Kubernetes in Docker Desktop
1. Open Docker Desktop
2. Go to Settings → Kubernetes
3. Check "Enable Kubernetes"
4. Click "Apply & Restart"

### 2. Install Ingress-Nginx Controller
```bash
# Install ingress-nginx using kubectl
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v0.41.2/deploy/static/provider/cloud/deploy.yaml

# Or install using Helm
helm install nginx ingress-nginx --repo https://kubernetes.github.io/ingress-nginx --namespace default
```

### 3. Verify Installation
```bash
# Check Helm version
helm version

# Check Skaffold version  
skaffold version

# Check Kubernetes nodes
kubectl get nodes

# Check if ingress-nginx is running
kubectl get pods -n ingress-nginx
```

## 📁 Project Structure

```
skaffold-spring-boot3/
├── myapp-chart/                 # Helm chart directory
│   ├── templates/
│   │   ├── deployment.yaml      # Kubernetes deployment configuration
│   │   ├── service.yaml         # Kubernetes service configuration  
│   │   └── ingress.yaml         # Ingress configuration for routing
│   ├── Chart.yaml               # Helm chart metadata
│   └── values.yaml              # Default configuration values
├── src/                         # Spring Boot application source
│   └── main/
│       └── java/
│           └── com/henry/dockerfiledemo/
│               ├── DockerfileDemoApplication.java
│               └── UserController.java
├── Dockerfile                   # Docker build configuration
├── skaffold.yaml               # Skaffold configuration
├── pom.xml                     # Maven dependencies
└── README.md                   # This file
```

## ⚙️ Configuration Files

### Dockerfile
```dockerfile
FROM eclipse-temurin:17-jdk-focal
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies (for better Docker layer caching)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Run the application
CMD ["./mvnw", "spring-boot:run"]
```

### Skaffold Configuration
```yaml
apiVersion: skaffold/v2beta10
kind: Config
build:
  artifacts:
  - image: skaffold-demo-docker
    context: .
    docker:
      dockerfile: Dockerfile
deploy:
  helm:
    releases:
    - name: myapp
      chartPath: ./myapp-chart
      valuesFiles:
      - ./myapp-chart/values.yaml
```

### Helm Chart Values
```yaml
replicaCount: 1

image:
  repository: skaffold-demo-docker
  tag: latest

service:
  port: 8080

ingress:
  enabled: true
```

## 🚦 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/HenryXiloj/skaffold-spring-boot3.git
cd skaffold-spring-boot3
```

### 2. Add Local DNS Entry
Add this entry to your `/etc/hosts` file (or `C:\Windows\System32\drivers\etc\hosts` on Windows):
```
127.0.0.1 myapp.local
```

### 3. Start Development
```bash
# Start Skaffold in development mode
skaffold dev
```

This command will:
- Build the Docker image
- Deploy to your local Kubernetes cluster using Helm
- Watch for code changes and automatically redeploy
- Stream logs from the application

### 4. Test the Application
Once deployed, test the endpoints:
```bash
# Test hello endpoint
curl http://myapp.local/hello

# Test user endpoint  
curl http://myapp.local/user
```

Expected responses:
- `/hello`: `"hello world"`
- `/user`: `{"firstName":"Henry","lastName":"Xiloj"}`

## 🔧 Development Workflow

### Available Skaffold Commands
```bash
# Development mode with hot-reload
skaffold dev

# Build and deploy once
skaffold run

# Delete deployed resources
skaffold delete

# Debug mode
skaffold debug
```

### Useful Kubernetes Commands
```bash
# View all resources
kubectl get all

# Check deployments
kubectl get deployment

# Check services
kubectl get svc

# Check ingress
kubectl get ingress

# View application logs
kubectl logs -f deployment/myapp

# Get pod details
kubectl describe pod <pod-name>
```

### Useful Helm Commands
```bash
# List Helm releases
helm list

# Get release status
helm status myapp

# Upgrade release
helm upgrade myapp ./myapp-chart

# Rollback release
helm rollback myapp 1
```

## 🐛 Troubleshooting

### Common Issues

1. **Ingress not working**
   - Ensure ingress-nginx controller is running: `kubectl get pods -n ingress-nginx`
   - Restart Docker Desktop Kubernetes if needed

2. **Application not accessible**
   - Check if `myapp.local` is added to your hosts file
   - Verify ingress configuration: `kubectl get ingress`

3. **Build failures**
   - Ensure Docker Desktop is running
   - Check if you have sufficient disk space
   - Try `skaffold delete` and `skaffold dev` again

4. **Permission issues on Linux/Mac**
   - Make Maven wrapper executable: `chmod +x mvnw`

### Check Application Status
```bash
# Check if pods are running
kubectl get pods

# Check service endpoints
kubectl get endpoints

# Check ingress status
kubectl describe ingress myapp-ingress
```

## 📚 Learn More

- [Skaffold Documentation](https://skaffold.dev/docs/)
- [Helm Documentation](https://helm.sh/docs/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/)
- [Original Blog Post](https://jarmx.blogspot.com/2023/11/deploying-spring-boot-apps-locally-with.html)
