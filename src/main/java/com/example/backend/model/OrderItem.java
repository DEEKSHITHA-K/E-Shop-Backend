package com.example.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Many-to-one relationship with Product (optional, can be denormalized)
    // Here, we link to the product for current details, but also denormalize for historical accuracy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // --- ADDED DENORMALIZED PRODUCT DETAILS FOR HISTORICAL ACCURACY AND DTO MAPPING ---
    @Column(nullable = false)
    private String productName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtOrder; // Price of the product when the order was placed

    private String productImageUrl; // Image URL at the time of order
    // ----------------------------------------------------------------------------------

    public OrderItem() {}

    public OrderItem(Order order, Product product, Integer quantity, String productName, BigDecimal priceAtOrder, String productImageUrl) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.productName = productName;
        this.priceAtOrder = priceAtOrder;
        this.productImageUrl = productImageUrl;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    // Getters and Setters for denormalized fields
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getPriceAtOrder() { return priceAtOrder; }
    public void setPriceAtOrder(BigDecimal priceAtOrder) { this.priceAtOrder = priceAtOrder; }
    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
               "id=" + id +
               ", productId=" + (product != null ? product.getId() : "null") +
               ", quantity=" + quantity +
               ", productName='" + productName + '\'' +
               '}';
    }
}
