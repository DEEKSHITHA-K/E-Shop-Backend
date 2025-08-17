package com.example.backend.controller;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // Base path for authentication endpoints
public class AuthController {

    @Autowired
    private AuthService authService; // Autowire the simplified AuthService

    /**
     * Handles user login requests.
     * This method calls the simplified AuthService to authenticate the user.
     * It maps RuntimeExceptions from the service to appropriate HTTP status codes.
     *
     * @param authRequest Contains the user's email and password.
     * @return ResponseEntity with AuthResponse on success, or an error message on failure.
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            // Call the authenticateUser method from the AuthService
            AuthResponse authResponse = authService.authenticateUser(authRequest);
            // If authentication is successful, return OK (200) with the AuthResponse
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            // Catch RuntimeException which now covers "Invalid credentials"
            // Since the AuthService throws a RuntimeException for both "User not found"
            // and "Password mismatch", we can map it to UNAUTHORIZED (401).
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected errors during the authentication process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication error: " + e.getMessage());
        }
    }

    /**
     * Handles new user registration requests.
     * This method calls the simplified AuthService to register a new user.
     * It maps RuntimeExceptions from the service to appropriate HTTP status codes.
     *
     * @param registerRequest Contains the new user's name, email, and password.
     * @return ResponseEntity with AuthResponse on successful registration, or an error message on failure.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Call the registerUser method from the AuthService
            AuthResponse authResponse = authService.registerUser(registerRequest);
            // If registration is successful, return CREATED (201) with the AuthResponse
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Catch RuntimeException, which specifically handles "Email already registered"
            // Map this to CONFLICT (409) as the resource already exists.
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected errors during the registration process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration error: " + e.getMessage());
        }
    }
}
