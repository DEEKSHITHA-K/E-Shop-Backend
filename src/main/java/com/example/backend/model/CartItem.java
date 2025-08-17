package com.example.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

// Represents a single item in a shopping cart
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with Cart, multiple items can belong to one cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // Many-to-one relationship with Product, references the product being added
    // EAGER fetch here is acceptable for cart view, but denormalized fields are more robust
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // --- ADDED DENORMALIZED PRODUCT DETAILS FOR ROBUSTNESS AND DTO MAPPING ---
    @Column(nullable = false)
    private String productName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtAddToCart; // Price of the product when it was added to cart

    private String productImageUrl; // Image URL at the time of adding to cart
    // -------------------------------------------------------------------------

    public CartItem() {}

    // Constructor for creating a new cart item (useful for service layer)
    public CartItem(Cart cart, Product product, Integer quantity, String productName, BigDecimal priceAtAddToCart, String productImageUrl) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.productName = productName;
        this.priceAtAddToCart = priceAtAddToCart;
        this.productImageUrl = productImageUrl;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    // Getters and Setters for denormalized fields
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getPriceAtAddToCart() { return priceAtAddToCart; }
    public void setPriceAtAddToCart(BigDecimal priceAtAddToCart) { this.priceAtAddToCart = priceAtAddToCart; }
    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        // For cart items, equality is usually based on the cart and product combination
        return Objects.equals(cart, cartItem.cart) &&
               Objects.equals(product, cartItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cart, product);
    }

    @Override
    public String toString() {
        return "CartItem{" +
               "id=" + id +
               ", cartId=" + (cart != null ? cart.getId() : "null") +
               ", productId=" + (product != null ? product.getId() : "null") +
               ", quantity=" + quantity +
               ", productName='" + productName + '\'' +
               '}';
    }
}
