# Docker Deployment Script for Car Repair Shop
# This script builds and optionally pushes your Docker image

# Configuration
$IMAGE_NAME = "car-repair-shop"
$VERSION = "1.0.0"
$DOCKER_USERNAME = "your-dockerhub-username"  # Change this to your Docker Hub username

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Car Repair Shop - Docker Deployment" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Build the Docker image
Write-Host "[1/3] Building Docker image..." -ForegroundColor Yellow
docker build -t ${IMAGE_NAME}:${VERSION} -t ${IMAGE_NAME}:latest .

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Docker build failed!" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Docker image built successfully!" -ForegroundColor Green
Write-Host ""

# Step 2: Test the image locally (optional)
Write-Host "[2/3] Testing image locally..." -ForegroundColor Yellow
Write-Host "To run locally, execute:" -ForegroundColor Cyan
Write-Host "  docker run -p 8080:8080 ${IMAGE_NAME}:latest" -ForegroundColor White
Write-Host ""

# Step 3: Push to Docker Hub (optional)
Write-Host "[3/3] Push to Docker Hub?" -ForegroundColor Yellow
Write-Host "To push to Docker Hub, execute:" -ForegroundColor Cyan
Write-Host "  docker login" -ForegroundColor White
Write-Host "  docker tag ${IMAGE_NAME}:latest ${DOCKER_USERNAME}/${IMAGE_NAME}:${VERSION}" -ForegroundColor White
Write-Host "  docker tag ${IMAGE_NAME}:latest ${DOCKER_USERNAME}/${IMAGE_NAME}:latest" -ForegroundColor White
Write-Host "  docker push ${DOCKER_USERNAME}/${IMAGE_NAME}:${VERSION}" -ForegroundColor White
Write-Host "  docker push ${DOCKER_USERNAME}/${IMAGE_NAME}:latest" -ForegroundColor White
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✅ Deployment script completed!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Test locally: docker run -p 8080:8080 ${IMAGE_NAME}:latest" -ForegroundColor White
Write-Host "2. Access Swagger: http://localhost:8080/swagger-ui/index.html" -ForegroundColor White
Write-Host "3. Deploy to cloud platform of your choice" -ForegroundColor White

