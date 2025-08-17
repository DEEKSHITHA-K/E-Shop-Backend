package com.example.backend.dto;

// DTO for authentication response, excluding JWT
public class AuthResponse {
    private Long userId;   // Include user ID in response
    private String email;  // Using email instead of generic username for consistency

    /**
     * Constructor for AuthResponse.
     *
     * @param userId The ID of the authenticated or registered user.
     * @param email The email address of the authenticated or registered user.
     */
    public AuthResponse(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    // Getter for user ID
    public Long getUserId() {
        return userId;
    }

    // Setter for user ID
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }
}
