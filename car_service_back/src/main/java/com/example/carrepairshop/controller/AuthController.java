package com.example.carrepairshop.controller;

import com.example.carrepairshop.model.AuthResponse;
import com.example.carrepairshop.model.LoginRequest;
import com.example.carrepairshop.model.RegistrationRequest;
import com.example.carrepairshop.security.AuthenticationConfig;
import com.example.carrepairshop.security.user.DuplicatedUserInfoException;
import com.example.carrepairshop.security.user.User;
import com.example.carrepairshop.security.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.validUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(new AuthResponse(user.getId(), user.getName()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody RegistrationRequest registrationRequest) {
        if (userService.hasUserWithUsername(registrationRequest.getUsername())) {
            throw new DuplicatedUserInfoException(String.format("Username %s is already been used", registrationRequest.getUsername()));
        }
        if (userService.hasUserWithEmail(registrationRequest.getEmail())) {
            throw new DuplicatedUserInfoException(String.format("Email %s is already been used", registrationRequest.getEmail()));
        }

        User user = userService.saveUser(this.mapSignUpRequestToUser(registrationRequest));
        return new AuthResponse(user.getId(), user.getName());
    }

    private User mapSignUpRequestToUser(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setName(registrationRequest.getName());
        user.setEmail(registrationRequest.getEmail());
        user.setRole(AuthenticationConfig.USER);
        return user;
    }
}
