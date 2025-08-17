package com.example.backend.controller;

import com.example.backend.dto.OrderRequest;
import com.example.backend.dto.OrderResponse;
import com.example.backend.dto.OrderItemResponse;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.Order;
import com.example.backend.model.OrderItem;
import com.example.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Places a new order for a user.
     * Expects userId as a query parameter and OrderRequest in the request body.
     *
     * @param userId The ID of the user placing the order.
     * @param orderRequest The OrderRequest DTO.
     * @return ResponseEntity with the created OrderResponse.
     */
    @PostMapping
    public ResponseEntity<?> placeOrder(
            @RequestParam Long userId,
            @RequestBody OrderRequest orderRequest) {
        try {
            Order newOrder = orderService.placeOrder(userId, orderRequest);
            return new ResponseEntity<>(convertToDto(newOrder), HttpStatus.CREATED); // 201 Created
        } catch (RuntimeException e) { // Catches ResourceNotFoundException or IllegalArgumentException
            System.err.println("Error placing order: " + e.getMessage());
            if (e.getMessage().contains("User not found") || e.getMessage().contains("Product not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 Not Found
            } else if (e.getMessage().contains("empty cart")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 400 Bad Request
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error placing order: " + e.getMessage()); // 500 Internal Server Error
        } catch (Exception e) {
            System.err.println("Unexpected error placing order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error placing order: " + e.getMessage()); // 500 Internal Server Error
        }
    }

    /**
     * Retrieves the order history for a specific user.
     * Expects userId as a query parameter.
     *
     * @param userId The ID of the user.
     * @return ResponseEntity with a list of OrderResponse objects.
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrderHistory(@RequestParam Long userId) {
        try {
            List<Order> orders = orderService.getOrderHistory(userId);
            List<OrderResponse> response = orders.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response); // 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found if user not found
        } catch (Exception e) {
            System.err.println("Error fetching order history: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
        }
    }

    // Helper method to convert Order entity to OrderResponse DTO
    private OrderResponse convertToDto(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getProduct() != null ? item.getProduct().getId() : null,
                        item.getProductName(),
                        item.getPriceAtOrder(),
                        item.getProductImageUrl(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getShippingAddress(),
                order.getShippingCity(),
                order.getShippingPostalCode(),
                order.getShippingCountry(),
                itemResponses
        );
    }
}
