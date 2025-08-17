package com.example.backend.repository;

import com.example.backend.model.CartItem;
import com.example.backend.model.Cart; // Import Cart model
import com.example.backend.model.Product; // Import Product model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Find all cart items for a specific cart
    List<CartItem> findByCart(Cart cart);

    // Find a specific cart item by cart and product
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product); // This is the method in question

    // Delete all cart items for a specific cart
    void deleteByCart(Cart cart);
}
