package com.example.backend.repository;

import com.example.backend.model.Cart;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Find a cart associated with a specific user
    Optional<Cart> findByUser(User user);

    // Find a cart by user ID (convenience method)
    // Spring Data JPA can derive this if User has an 'id' field
    Optional<Cart> findByUserId(Long userId);
}
