package com.example.backend.service;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.Cart;
import com.example.backend.model.CartItem;
import com.example.backend.model.Order;
import com.example.backend.model.OrderItem;
import com.example.backend.model.User;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.OrderItemRepository;
import com.example.backend.dto.OrderRequest; // DTO for incoming order request
import com.example.backend.dto.OrderItemRequest; // DTO for items within order request
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final CartService cartService; // To clear cart after order

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        UserService userService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
        this.cartService = cartService;
    }

    /**
     * Places a new order for a user based on their cart items.
     *
     * @param userId The ID of the user placing the order.
     * @param orderRequest The OrderRequest DTO containing shipping details and items.
     * @return The newly created Order entity.
     * @throws RuntimeException if user not found, cart is empty, or product not found.
     */
    @Transactional
    public Order placeOrder(Long userId, OrderRequest orderRequest) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Fetch current cart items to ensure consistency and get latest product details
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot place an order with an empty cart.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING"); // Initial status

        // Set shipping details from the request
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setShippingCity(orderRequest.getShippingCity());
        order.setShippingPostalCode(orderRequest.getShippingPostalCode());
        order.setShippingCountry(orderRequest.getShippingCountry());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); // Set bidirectional relationship
            orderItem.setProduct(cartItem.getProduct()); // Link to product entity
            orderItem.setQuantity(cartItem.getQuantity());
            // Denormalize product details for historical order item
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setPriceAtOrder(cartItem.getPriceAtAddToCart());
            orderItem.setProductImageUrl(cartItem.getProductImageUrl());

            order.getOrderItems().add(orderItem); // Add to order's list

            totalAmount = totalAmount.add(cartItem.getPriceAtAddToCart().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Clear the user's cart after successful order placement
        cartService.clearCart(userId);

        return savedOrder;
    }

    /**
     * Retrieves the order history for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of Order entities for the user.
     * @throws ResourceNotFoundException if user not found.
     */
    public List<Order> getOrderHistory(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return orderRepository.findByUser(user);
    }

    /**
     * Retrieves a single order by its ID.
     *
     * @param orderId The ID of the order.
     * @return An Optional containing the Order if found, or empty.
     */
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
