package com.example.carrepairshop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class AuthenticationConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // Public endpoints
                        .requestMatchers("/public/**", "/auth/**").permitAll()
                        .requestMatchers("/", "/health", "/error", "/csrf", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        // API endpoints - make GET requests public for testing, POST/PUT/DELETE require auth
                        .requestMatchers(HttpMethod.GET, "/reservations/**", "/cars/**", "/clients/**", "/mechanics/**", "/services/**", "/repair-jobs/**").permitAll()
                        .requestMatchers("/api/books", "/api/books/**", "/api/users", "/api/users/**").hasAuthority(ADMIN)
                        // All other requests require authentication
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
}
