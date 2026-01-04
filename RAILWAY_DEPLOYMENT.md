# Railway Deployment Guide

This guide will help you deploy your Car Repair Shop application (backend and frontend) to Railway.

## Prerequisites
- Railway account (already registered)
- GitHub repository connected (already done)

## Deployment Steps

### Step 1: Connect Repository to Railway

1. Go to [Railway Dashboard](https://railway.com/dashboard)
2. Click **"New Project"**
3. Select **"Deploy from GitHub repo"**
4. Choose your repository: `ahakimov/carrepairshop`
5. Railway will detect your monorepo structure

### Step 2: Deploy Backend Service

1. In your Railway project, click **"New Service"**
2. Select **"GitHub Repo"** and choose your repository
3. In the service settings:
   - **Root Directory**: Set to `car_service_back`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/car-repair-shop-0.0.1-SNAPSHOT.jar`
   - **Environment**: Add environment variable:
     - `SPRING_PROFILES_ACTIVE=prod`
     - `PORT` (Railway will set this automatically)

### Step 3: Add PostgreSQL Database

1. In your Railway project, click **"New"** → **"Database"** → **"Add PostgreSQL"**
2. Railway will automatically create a PostgreSQL database
3. The `DATABASE_URL` environment variable will be automatically added to your backend service
4. Make sure your backend service is connected to this database service

### Step 4: Deploy Frontend Service

1. In your Railway project, click **"New Service"**
2. Select **"GitHub Repo"** and choose your repository
3. In the service settings:
   - **Root Directory**: Set to `car_service_front`
   - **Build Command**: `npm install && npm run build`
   - **Start Command**: `npm start`
   - **Environment Variables**: 
     - `NODE_ENV=production`
     - `PORT` (Railway will set this automatically)
     - If your frontend needs to connect to the backend, add:
       - `NEXT_PUBLIC_API_URL=https://your-backend-service.railway.app` (update after backend is deployed)

### Step 5: Configure Environment Variables

#### Backend Environment Variables:
- `SPRING_PROFILES_ACTIVE=prod`
- `DATABASE_URL` (automatically set by Railway PostgreSQL service)
- `PORT` (automatically set by Railway)

#### Frontend Environment Variables:
- `NODE_ENV=production`
- `PORT` (automatically set by Railway)
- `NEXT_PUBLIC_API_URL` (set this to your backend Railway URL after deployment)

### Step 6: Get Your Deployment URLs

1. After deployment, Railway will provide URLs for each service
2. Backend URL will be something like: `https://your-backend-service.railway.app`
3. Frontend URL will be something like: `https://your-frontend-service.railway.app`
4. Update the frontend's `NEXT_PUBLIC_API_URL` with the backend URL

### Step 7: Verify Deployment

1. Check backend health: `https://your-backend-service.railway.app/actuator/health`
2. Check frontend: `https://your-frontend-service.railway.app`
3. Test API endpoints from the frontend

## Troubleshooting

### Backend Issues:
- Check logs in Railway dashboard
- Verify `DATABASE_URL` is set correctly
- Ensure PostgreSQL dependency is in `pom.xml`
- Check that `SPRING_PROFILES_ACTIVE=prod` is set

### Frontend Issues:
- Check build logs for errors
- Verify `NEXT_PUBLIC_API_URL` points to correct backend URL
- Check that all dependencies are in `package.json`

## Notes

- Railway automatically handles HTTPS
- Each service gets its own domain
- Database migrations will run automatically on startup (ddl-auto=update)
- Railway provides automatic scaling and monitoring

