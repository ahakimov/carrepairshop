# Railway Deployment Guide

This guide will help you deploy your Car Repair Shop application (backend and frontend) to Railway.

## Prerequisites
- Railway account (already registered)
- GitHub repository connected (already done)

## Important: Monorepo Setup

**CRITICAL**: Since this is a monorepo, you MUST set the **Root Directory** for each service in Railway's service settings. This is the most common cause of build failures.

## Deployment Steps

### Step 1: Connect Repository to Railway

1. Go to [Railway Dashboard](https://railway.com/dashboard)
2. Click **"New Project"**
3. Select **"Deploy from GitHub repo"**
4. Choose your repository: `ahakimov/carrepairshop`
5. Railway will create a service, but **DO NOT deploy yet** - we need to configure it first

### Step 2: Add PostgreSQL Database

1. In your Railway project, click **"New"** → **"Database"** → **"Add PostgreSQL"**
2. Railway will automatically create a PostgreSQL database
3. The `DATABASE_URL` environment variable will be automatically added when you connect services

### Step 3: Deploy Backend Service

1. **If you already created a service from Step 1, delete it first** (or create a new one)
2. Click **"New Service"** → **"GitHub Repo"** → select your repository `ahakimov/carrepairshop`
3. **IMPORTANT**: In the service settings, find **"Root Directory"** and set it to: `car_service_back`
4. Railway will auto-detect it's a Java/Maven project
5. In **"Settings"** → **"Variables"**, add:
   - `SPRING_PROFILES_ACTIVE` = `prod`
6. Connect the PostgreSQL database to this service:
   - Click on your backend service
   - Go to **"Variables"** tab
   - Click **"Add Reference"** → Select your PostgreSQL database
   - This will automatically add `DATABASE_URL`
7. The service should now build and deploy automatically

### Step 4: Deploy Frontend Service

1. Click **"New Service"** → **"GitHub Repo"** → select your repository `ahakimov/carrepairshop`
2. **IMPORTANT**: In the service settings, find **"Root Directory"** and set it to: `car_service_front`
3. Railway will auto-detect it's a Node.js/Next.js project
4. In **"Settings"** → **"Variables"**, add:
   - `NODE_ENV` = `production`
   - `NEXT_PUBLIC_API_URL` = `https://your-backend-service.railway.app` 
     - **Note**: Get this URL from your backend service after it deploys (it will be in the format: `https://your-service-name.up.railway.app`)
5. The service should now build and deploy automatically

### Step 5: Get Your Deployment URLs

1. After deployment, Railway will provide URLs for each service
2. Backend URL will be something like: `https://your-backend-service.up.railway.app`
3. Frontend URL will be something like: `https://your-frontend-service.up.railway.app`
4. **Update the frontend's `NEXT_PUBLIC_API_URL`** with the backend URL if you haven't already

### Step 6: Verify Deployment

1. Check backend health: `https://your-backend-service.up.railway.app/actuator/health`
   - If this doesn't work, check if you have actuator dependency in pom.xml
2. Check frontend: `https://your-frontend-service.up.railway.app`
3. Test API endpoints from the frontend

## Troubleshooting

### "Nixpacks was unable to generate a build plan" Error

**This means Railway is trying to build from the root directory instead of the service directory.**

**Solution:**
1. Go to your service settings in Railway
2. Find **"Root Directory"** field
3. Set it to either:
   - `car_service_back` for backend
   - `car_service_front` for frontend
4. Save and redeploy

### Backend Issues:
- **Build fails**: Check that Root Directory is set to `car_service_back`
- **Database connection fails**: Verify `DATABASE_URL` is set (should be automatic when you connect PostgreSQL)
- **Port issues**: Railway sets `PORT` automatically, make sure your app uses `${PORT:8080}` in application.properties
- Check logs in Railway dashboard

### Frontend Issues:
- **Build fails**: Check that Root Directory is set to `car_service_front`
- **API calls fail**: Verify `NEXT_PUBLIC_API_URL` points to correct backend URL
- **Build errors**: Check that all dependencies are in `package.json`
- Check logs in Railway dashboard

## Configuration Files

The following files are already configured:
- `car_service_back/nixpacks.toml` - Build configuration for backend
- `car_service_back/railway.toml` - Railway deployment config for backend
- `car_service_front/railway.toml` - Railway deployment config for frontend
- `car_service_back/src/main/resources/application-prod.properties` - Production config

## Notes

- Railway automatically handles HTTPS
- Each service gets its own domain
- Database migrations will run automatically on startup (ddl-auto=update)
- Railway provides automatic scaling and monitoring
- **Always set Root Directory in service settings, not in code**
