package com.example.backend.dto;

import com.example.backend.model.CartItem;
import java.math.BigDecimal; // Use BigDecimal for price

// DTO for CartItem response (to avoid exposing full Product entity details unnecessarily)
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price; // Changed to BigDecimal
    private String imageUrl;
    private Integer quantity;

    // Constructor that takes a CartItem entity
    public CartItemResponse(CartItem cartItem) {
        this.id = cartItem.getId();
        // Use denormalized fields from CartItem for robustness
        this.productId = cartItem.getProduct() != null ? cartItem.getProduct().getId() : null; // Still link to product ID
        this.productName = cartItem.getProductName();
        this.price = cartItem.getPriceAtAddToCart(); // Use BigDecimal directly
        this.imageUrl = cartItem.getProductImageUrl();
        this.quantity = cartItem.getQuantity();
    }

    // Constructor for manual creation (e.g., in tests or if needed)
    public CartItemResponse(Long id, Long productId, String productName, BigDecimal price, String imageUrl, Integer quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    // Getters
    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getPrice() { return price; } // Changed to BigDecimal
    public String getImageUrl() { return imageUrl; }
    public Integer getQuantity() { return quantity; }

    // Setters (if needed for deserialization, but primarily for serialization here)
    public void setId(Long id) { this.id = id; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setPrice(BigDecimal price) { this.price = price; } // Changed to BigDecimal
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
