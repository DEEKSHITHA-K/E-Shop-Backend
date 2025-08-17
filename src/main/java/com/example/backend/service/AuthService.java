package com.example.backend.service;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // Inject UserService instead of UserRepository
    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Authenticates a user based on their email and password.
     * This simplified version directly checks credentials against the database
     * without password encoding or JWT generation, as per the request.
     *
     * @param authRequest Contains the user's email and password for authentication.
     * @return AuthResponse containing user ID and email if authentication is successful.
     * @throws RuntimeException if the user is not found or credentials do not match.
     */
    public AuthResponse authenticateUser(AuthRequest authRequest) {
        // Delegate finding user by email to UserService
        User user = userService.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials: User not found"));

        // TEMPORARY DEBUG PRINTS: Check exact passwords being compared
        System.out.println("DEBUG: Incoming Password (from form): [" + authRequest.getPassword() + "]");
        System.out.println("DEBUG: Stored Password (from DB): [" + user.getPassword() + "]");
        System.out.println("DEBUG: Passwords match? " + user.getPassword().equals(authRequest.getPassword()));
        // END TEMPORARY DEBUG PRINTS

        // In a real application, you would compare encoded passwords.
        // As requested, this version directly compares plain text passwords.
        // This is INSECURE and should NOT be used in a production environment.
        if (!user.getPassword().equals(authRequest.getPassword())) {
            throw new RuntimeException("Invalid credentials: Password mismatch");
        }

        // Return AuthResponse with userId and email, aligning with the updated AuthResponse DTO
        return new AuthResponse(user.getId(), user.getEmail());
    }

    /**
     * Registers a new user.
     * This simplified version saves the user directly without password encoding,
     * as per the request.
     *
     * @param registerRequest Contains the new user's name, email, and password.
     * @return AuthResponse containing the newly registered user's ID and email.
     * @throws RuntimeException if the email is already registered.
     */
    public AuthResponse registerUser(RegisterRequest registerRequest) {
        // Delegate checking email existence to UserService
        if (userService.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already registered: " + registerRequest.getEmail());
        }

        // Create a new User entity from the registration request
        User newUser = new User();
        newUser.setName(registerRequest.getName());
        newUser.setEmail(registerRequest.getEmail());
        // In a real application, the password should be hashed before saving.
        // As requested, this version saves the plain text password.
        // This is INSECURE and should NOT be used in a production environment.
        newUser.setPassword(registerRequest.getPassword());

        // Delegate saving the new user to UserService
        User savedUser = userService.registerNewUser(newUser);

        // Return AuthResponse with userId and email, aligning with the updated AuthResponse DTO
        return new AuthResponse(savedUser.getId(), savedUser.getEmail());
    }
}
