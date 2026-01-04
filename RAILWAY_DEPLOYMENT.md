# Railway Deployment Guide

This guide will help you deploy your Car Repair Shop application (backend and frontend) to Railway.

## Prerequisites
- Railway account (already registered)
- GitHub repository connected (already done)

## Important: Monorepo Setup

**CRITICAL**: Since this is a monorepo, you MUST set the **Root Directory** for each service in Railway's service settings. This is the most common cause of build failures.

## Deployment Steps

### Step 1: Connect Repository to Railway (Backend Service)

1. Go to [Railway Dashboard](https://railway.com/dashboard)
2. Click **"New Project"**
3. Select **"Deploy from GitHub repo"**
4. Choose your repository: `ahakimov/carrepairshop`
5. Railway will create a service - this will be your **BACKEND** service

### Step 2: Configure Backend Service

1. Click on the service that was just created
2. Go to **"Settings"** (gear icon)
3. **IMPORTANT**: Find **"Root Directory"** field and set it to: `car_service_back`
4. Go to **"Variables"** tab and add:
   - `SPRING_PROFILES_ACTIVE` = `prod`
5. Save the settings
6. The backend should now build and deploy

### Step 3: Add PostgreSQL Database

1. In your Railway project, click **"New"** → **"Database"** → **"Add PostgreSQL"**
2. Railway will automatically create a PostgreSQL database
3. Connect the database to your backend service:
   - Click on your backend service
   - Go to **"Variables"** tab
   - Click **"Add Reference"** → Select your PostgreSQL database
   - This will automatically add `DATABASE_URL` environment variable

### Step 4: Create Frontend Service

**IMPORTANT**: You need to create a SECOND service for the frontend!

1. In your Railway project, click **"New"** (or the **"+"** button)
2. Select **"GitHub Repo"**
3. Choose the same repository: `ahakimov/carrepairshop`
4. **IMPORTANT**: In the service settings, find **"Root Directory"** and set it to: `car_service_front`
5. Railway will auto-detect it's a Node.js/Next.js project
6. In **"Settings"** → **"Variables"**, add:
   - `NODE_ENV` = `production`
   - `NEXT_PUBLIC_API_URL` = `https://your-backend-service.up.railway.app` 
     - **Note**: Get this URL from your backend service after it deploys successfully
     - You can find it in the backend service's "Settings" → "Domains" section
7. The frontend service should now build and deploy automatically

### Step 5: Get Your Deployment URLs

1. After deployment, Railway will provide URLs for each service:
   - **Backend URL**: Found in backend service → Settings → Domains
     - Format: `https://your-backend-service.up.railway.app`
   - **Frontend URL**: Found in frontend service → Settings → Domains
     - Format: `https://your-frontend-service.up.railway.app`
2. **Update the frontend's `NEXT_PUBLIC_API_URL`** with the backend URL if you haven't already

### Step 6: Verify Deployment

1. Check backend health: `https://your-backend-service.up.railway.app/actuator/health`
   - Should return: `{"status":"UP"}`
2. Check frontend: `https://your-frontend-service.up.railway.app`
3. Test API endpoints from the frontend

## Visual Guide: Creating Second Service

When you're in your Railway project dashboard:
1. You'll see your backend service (already created)
2. Click the **"+"** or **"New"** button (usually at the top or bottom of the services list)
3. Select **"GitHub Repo"**
4. Choose `ahakimov/carrepairshop` again
5. **Before deploying**, go to Settings → Root Directory → Set to `car_service_front`

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
- **Healthcheck fails**: Check that Actuator is working at `/actuator/health`
- Check logs in Railway dashboard

### Frontend Issues:
- **Build fails**: Check that Root Directory is set to `car_service_front`
- **API calls fail**: Verify `NEXT_PUBLIC_API_URL` points to correct backend URL
- **Build errors**: Check that all dependencies are in `package.json`
- Check logs in Railway dashboard

### "I only see one service option"

If you only see one service:
1. You need to manually create a second service
2. Click **"New"** or **"+"** button in your Railway project
3. Select **"GitHub Repo"** again
4. Choose the same repository
5. Set Root Directory to the appropriate folder

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
- **You need TWO separate services** - one for backend, one for frontend
