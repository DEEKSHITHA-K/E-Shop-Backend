package com.example.backend.dto;

import java.math.BigDecimal;

// DTO for individual order items in an OrderResponse
public class OrderItemResponse {
    private Long id; // OrderItem ID
    private Long productId;
    private String productName;
    private BigDecimal price; // Price at the time of order
    private String imageUrl;
    private Integer quantity;

    public OrderItemResponse() {}

    public OrderItemResponse(Long id, Long productId, String productName, BigDecimal price, String imageUrl, Integer quantity) {
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
    public BigDecimal getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public Integer getQuantity() { return quantity; }

    // Setters (if needed for deserialization)
    public void setId(Long id) { this.id = id; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
