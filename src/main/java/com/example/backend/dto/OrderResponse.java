package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// DTO for outgoing order details
public class OrderResponse {
    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private String shippingCity;
    private String shippingPostalCode;
    private String shippingCountry;
    private List<OrderItemResponse> items; // List of order items in the response

    public OrderResponse() {}

    public OrderResponse(Long id, LocalDateTime orderDate, BigDecimal totalAmount, String status,
                         String shippingAddress, String shippingCity, String shippingPostalCode,
                         String shippingCountry, List<OrderItemResponse> items) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingPostalCode = shippingPostalCode;
        this.shippingCountry = shippingCountry;
        this.items = items;
    }

    // Getters
    public Long getId() { return id; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getShippingAddress() { return shippingAddress; }
    public String getShippingCity() { return shippingCity; }
    public String getShippingPostalCode() { return shippingPostalCode; }
    public String getShippingCountry() { return shippingCountry; }
    public List<OrderItemResponse> getItems() { return items; }

    // Setters (if needed for deserialization, but primarily for serialization here)
    public void setId(Long id) { this.id = id; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setStatus(String status) { this.status = status; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setShippingCity(String shippingCity) { this.shippingCity = shippingCity; }
    public void setShippingPostalCode(String shippingPostalCode) { this.shippingPostalCode = shippingPostalCode; }
    public void setShippingCountry(String shippingCountry) { this.shippingCountry = shippingCountry; }
    public void setItems(List<OrderItemResponse> items) { this.items = items; }
}
