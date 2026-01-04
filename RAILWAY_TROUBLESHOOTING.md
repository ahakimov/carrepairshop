# Railway Deployment Troubleshooting

## Healthcheck Still Failing?

If your deployment shows "Healthcheck failure" even after the fixes, here's how to debug:

### Step 1: Check Railway Logs

1. Go to your Railway service
2. Click on **"Deployments"** tab
3. Click on the failed deployment
4. Click **"View logs"** or go to **"Deploy Logs"** tab
5. Look for:
   - Application startup errors
   - Database connection errors
   - Port binding issues
   - Any exception stack traces

### Step 2: Verify Environment Variables

In Railway service → **"Variables"** tab, make sure you have:

**Required:**
- `SPRING_PROFILES_ACTIVE` = `prod`
- `DATABASE_URL` (should be automatically added when you connect PostgreSQL)

**Check if DATABASE_URL is set:**
- If missing, go to your PostgreSQL service
- Click **"Variables"** tab
- Copy the `DATABASE_URL` value
- Go back to your backend service
- Add it manually if needed

### Step 3: Test Health Endpoint Manually

1. Get your service URL from Railway (Settings → Domains)
2. Try accessing: `https://your-service.up.railway.app/actuator/health`
3. If it returns `{"status":"UP"}`, the healthcheck should work
4. If you get an error, check what the error is

### Step 4: Common Issues

#### Issue: "Connection refused" or "Cannot connect to database"
**Solution:**
- Verify PostgreSQL service is running
- Check that DATABASE_URL is correctly formatted
- Ensure backend service is connected to PostgreSQL (Variables → Add Reference)

#### Issue: "Application failed to start"
**Solution:**
- Check deploy logs for specific error messages
- Verify all dependencies are in pom.xml
- Check if there are any missing environment variables

#### Issue: "Port already in use"
**Solution:**
- Railway sets PORT automatically, make sure your app uses `${PORT:8080}`
- Check application-prod.properties has `server.port=${PORT:8080}`

#### Issue: Healthcheck times out
**Solution:**
- The timeout is now set to 600 seconds (10 minutes)
- If app takes longer to start, check logs for startup issues
- Consider disabling healthcheck temporarily to see if app starts

### Step 5: Temporarily Disable Healthcheck

If you want to test if the app starts without healthcheck:

1. Go to Railway service → Settings
2. Find "Healthcheck" section
3. You can disable it temporarily to see if the app actually starts
4. Then check the service URL manually

### Step 6: Check Database Connection

The DatabaseConfig should parse Railway's DATABASE_URL. If it's failing:

1. Check the format of DATABASE_URL in Railway
2. It should be: `postgresql://user:password@host:port/database`
3. Check deploy logs for database connection errors

### Getting Help

If none of these work:
1. Copy the full error message from Railway logs
2. Check what the `/actuator/health` endpoint returns
3. Verify all environment variables are set correctly
4. Check if the app is actually running (even if healthcheck fails)

