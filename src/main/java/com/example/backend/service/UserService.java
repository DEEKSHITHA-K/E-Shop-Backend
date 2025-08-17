package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user by saving them to the database.
     * Note: In this simplified version, the password is saved as plain text.
     * This is INSECURE and should NOT be used in a production environment.
     * Passwords MUST be hashed before storage in a real application.
     *
     * @param user The User entity to be registered.
     * @return The saved User entity.
     */
    public User registerNewUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return An Optional containing the User if found, or empty if not.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Checks if a user with the given email address already exists.
     *
     * @param email The email address to check.
     * @return true if a user with the email exists, false otherwise.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user to find.
     * @return An Optional containing the User if found, or empty if not.
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
