package com.example.carrepairshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
@Profile("prod")
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() {
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            // Fallback to default Spring Boot auto-configuration
            return DataSourceBuilder.create().build();
        }

        try {
            // Handle Railway's DATABASE_URL format: postgresql://user:password@host:port/database
            String url = databaseUrl;
            if (url.startsWith("postgresql://")) {
                url = url.replace("postgresql://", "http://");
            }
            URI dbUri = new URI(url);
            
            String userInfo = dbUri.getUserInfo();
            if (userInfo == null || !userInfo.contains(":")) {
                throw new IllegalArgumentException("Invalid DATABASE_URL format: missing user info");
            }
            
            String[] credentials = userInfo.split(":", 2);
            String username = credentials[0];
            String password = credentials.length > 1 ? credentials[1] : "";
            
            int port = dbUri.getPort() > 0 ? dbUri.getPort() : 5432;
            String path = dbUri.getPath();
            if (path != null && path.startsWith("/")) {
                path = path.substring(1); // Remove leading slash
            }
            
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + port + "/" + path;

            return DataSourceBuilder.create()
                    .url(dbUrl)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error parsing DATABASE_URL: " + e.getMessage());
            e.printStackTrace();
            // Fallback to default Spring Boot auto-configuration
            return DataSourceBuilder.create().build();
        }
    }
}

