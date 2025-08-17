package com.example.backend.controller;

import com.example.backend.model.CartItem;
import com.example.backend.service.CartService;
import com.example.backend.dto.CartItemRequest; // Correct DTO for request body
import com.example.backend.dto.CartItemResponse;
import com.example.backend.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    // UserService is not directly needed in CartController if CartService handles user lookup
    // private final UserService userService;

    @Autowired
    public CartController(CartService cartService) { // Removed UserService from constructor
        this.cartService = cartService;
        // this.userService = userService;
    }

    // GET /api/cart?userId=123
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getUserCart(@RequestParam Long userId) {
        try {
            // CartService.getCartItemsByUserId returns List<CartItem> directly
            List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);
            List<CartItemResponse> response = cartItems.stream()
                    .map(CartItemResponse::new) // Uses the CartItemResponse(CartItem) constructor
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) { // Catch RuntimeException from service for user not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            System.err.println("Error fetching user cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // POST /api/cart?userId=123
    // userId is now a @RequestParam, and request body is CartItemRequest
    @PostMapping
    public ResponseEntity<CartItemResponse> addProductToCart(
            @RequestParam Long userId, // Get userId from query parameter
            @RequestBody CartItemRequest request) { // Request body contains productId and quantity
        try {
            // Pass userId, productId, and quantity to the service
            CartItem updatedItem = cartService.addProductToCart(userId, request.getProductId(), request.getQuantity());
            if (updatedItem == null) {
                // This case handles removal if quantity goes to 0 or less
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new CartItemResponse(updatedItem));
        } catch (RuntimeException e) { // Catch RuntimeException for ResourceNotFoundException or IllegalArgumentException
            System.err.println("Error adding product to cart: " + e.getMessage());
            // Differentiate between NOT_FOUND and BAD_REQUEST if needed, or return generic 400
            if (e.getMessage().contains("not found")) {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            System.err.println("Error adding product to cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // POST /api/cart/remove?userId=123
    // userId is a @RequestParam, and request body is CartItemRequest
    @PostMapping("/remove")
    public ResponseEntity<Void> removeProductFromCart(
            @RequestParam Long userId, // Get userId from query parameter
            @RequestBody CartItemRequest request) { // Request body contains productId and quantityToRemove
        try {
            // Pass userId, productId, and quantityToRemove to the service
            cartService.removeProductFromCart(userId, request.getProductId(), request.getQuantity());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) { // Catch RuntimeException for ResourceNotFoundException or IllegalArgumentException
            System.err.println("Error removing product from cart: " + e.getMessage());
            if (e.getMessage().contains("not found")) {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            System.err.println("Error removing product from cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE /api/cart/clear?userId=123
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestParam Long userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) { // Catch RuntimeException for ResourceNotFoundException
            System.err.println("Error clearing cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            System.err.println("Error clearing cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Helper method to convert CartItem entity to CartItemResponse DTO
    // This method is now part of the controller, as it was missing from your previous code.
    private CartItemResponse convertToDto(CartItem cartItem) {
        return new CartItemResponse(cartItem);
    }
}
