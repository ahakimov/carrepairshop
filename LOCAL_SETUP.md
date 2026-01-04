# Local Development Setup Guide

This guide will help you run the Car Repair Shop application locally on your machine.

## Prerequisites

### Required Software:
1. **Java 21** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/#java21) or [OpenJDK](https://adoptium.net/)
2. **Maven** - Usually comes with Java or download from [Maven](https://maven.apache.org/download.cgi)
3. **Node.js 20+** - Download from [Node.js](https://nodejs.org/)
4. **npm** - Comes with Node.js

### Verify Installation:
```bash
java -version    # Should show Java 21
mvn -version     # Should show Maven version
node -v          # Should show Node.js version
npm -v           # Should show npm version
```

## Step 1: Run Backend (Spring Boot)

### Option A: Using Maven (Recommended)

1. **Open terminal/command prompt**
2. **Navigate to backend directory:**
   ```bash
   cd car_service_back
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
   
   Or if you're on Windows and mvn doesn't work:
   ```bash
   .\mvnw.cmd spring-boot:run
   ```

4. **Wait for startup** - You should see:
   ```
   Started CarRepairShopApplication in X.XXX seconds
   ```

5. **Backend will be running at:** `http://localhost:8080`

### Option B: Using IDE (IntelliJ IDEA / Eclipse / VS Code)

1. **Open the project** in your IDE
2. **Open:** `car_service_back/src/main/java/com/example/carrepairshop/CarRepairShopApplication.java`
3. **Right-click** → **Run** or click the green play button
4. **Backend will start** on `http://localhost:8080`

### Verify Backend is Running:

Open in browser:
- Health check: `http://localhost:8080/`
- Health endpoint: `http://localhost:8080/health`
- Actuator health: `http://localhost:8080/actuator/health`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

You should see JSON responses or the Swagger UI.

## Step 2: Run Frontend (Next.js)

1. **Open a NEW terminal/command prompt** (keep backend running in the first one)

2. **Navigate to frontend directory:**
   ```bash
   cd car_service_front
   ```

3. **Install dependencies** (first time only):
   ```bash
   npm install
   ```

4. **Run the development server:**
   ```bash
   npm run dev
   ```

5. **Frontend will be running at:** `http://localhost:3000`

6. **Open your browser** and go to: `http://localhost:3000`

## Step 3: Configure Frontend to Connect to Backend

If your frontend needs to connect to the backend API:

1. **Create environment file** in `car_service_front`:
   ```bash
   # Windows PowerShell
   New-Item .env.local
   
   # Or create manually: .env.local
   ```

2. **Add backend URL** to `.env.local`:
   ```
   NEXT_PUBLIC_API_URL=http://localhost:8080
   ```

3. **Restart the frontend** (stop with Ctrl+C and run `npm run dev` again)

## Troubleshooting

### Backend Issues:

**"Port 8080 already in use"**
- Another application is using port 8080
- Solution: Stop the other application or change port in `application.properties`:
  ```
  server.port=8081
  ```

**"Java version error"**
- Make sure you have Java 21 installed
- Check: `java -version`

**"Maven not found"**
- Use the Maven wrapper: `.\mvnw.cmd spring-boot:run` (Windows) or `./mvnw spring-boot:run` (Mac/Linux)

**"Database connection error"**
- For local development, the app uses H2 in-memory database
- This should work automatically - no setup needed
- If you see errors, check `application-h2.properties` is being used

### Frontend Issues:

**"Port 3000 already in use"**
- Another Next.js app is running
- Solution: Stop it or use a different port:
  ```bash
  npm run dev -- -p 3001
  ```

**"Module not found"**
- Run `npm install` again
- Delete `node_modules` and `package-lock.json`, then run `npm install`

**"Cannot connect to backend"**
- Make sure backend is running on `http://localhost:8080`
- Check `.env.local` has `NEXT_PUBLIC_API_URL=http://localhost:8080`

## Quick Start Commands

### Terminal 1 (Backend):
```bash
cd car_service_back
mvn spring-boot:run
```

### Terminal 2 (Frontend):
```bash
cd car_service_front
npm install  # First time only
npm run dev
```

## Access Points

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080
- **Backend Health:** http://localhost:8080/health
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **H2 Console:** http://localhost:8080/h2-console (if enabled)

## Database (Local)

The app uses **H2 in-memory database** for local development:
- No installation needed
- Data is stored in memory (lost when app stops)
- Automatically creates tables from your entities
- Can access H2 console at `/h2-console` if enabled

## Next Steps

Once everything is running locally:
1. Test the application
2. Make changes and see them hot-reload
3. Debug any issues
4. When ready, we can fix the Railway deployment

## Need Help?

If you encounter any errors:
1. Check the error message carefully
2. Make sure all prerequisites are installed
3. Verify ports 8080 and 3000 are available
4. Check that both services are running

