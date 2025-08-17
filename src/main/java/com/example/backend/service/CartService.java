package com.example.backend.service;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.Cart;
import com.example.backend.model.CartItem;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.CartItemRepository;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Retrieves the cart for a given user ID. If the cart does not exist, it creates a new one.
     * @param userId The ID of the user.
     * @return The user's cart.
     * @throws ResourceNotFoundException if the user is not found.
     */
    @Transactional
    public Cart getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Optional<Cart> existingCart = cartRepository.findByUser(user);
        return existingCart.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    /**
     * Adds a product to the user's cart or updates its quantity.
     * @param userId The ID of the user.
     * @param productId The ID of the product to add.
     * @param quantity The quantity to add (can be negative to remove quantity).
     * @return The updated cart item.
     * @throws ResourceNotFoundException if product or user not found, or invalid quantity.
     * @throws IllegalArgumentException if quantity is invalid.
     */
    @Transactional
    public CartItem addProductToCart(Long userId, Long productId, int quantity) {
        if (quantity == 0) {
            throw new IllegalArgumentException("Quantity cannot be zero.");
        }

        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            if (newQuantity <= 0) {
                cartItemRepository.delete(cartItem);
                return null;
            }
            cartItem.setQuantity(newQuantity);
        } else {
            if (quantity < 0) {
                throw new IllegalArgumentException("Cannot remove negative quantity of a non-existent item.");
            }
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setProductName(product.getName());
            cartItem.setPriceAtAddToCart(product.getPrice());
            cartItem.setProductImageUrl(product.getImageUrl());
        }

        return cartItemRepository.save(cartItem);
    }

    /**
     * Retrieves all cart items for a specific user's cart.
     * This is the method that CartController is trying to call.
     *
     * @param userId The ID of the user whose cart items to retrieve.
     * @return A list of CartItem objects.
     * @throws ResourceNotFoundException if user or cart not found.
     */
    public List<CartItem> getCartItemsByUserId(Long userId) {
        Cart cart = getCartByUserId(userId); // Get the user's cart
        return cartItemRepository.findByCart(cart);
    }


    /**
     * Removes a specific quantity of a product from the cart, or the entire item if quantity is -1.
     * @param userId The ID of the user.
     * @param productId The ID of the product to remove.
     * @param quantityToRemove The quantity to remove. If -1, removes all.
     * @throws ResourceNotFoundException if cart item not found.
     * @throws IllegalArgumentException if quantityToRemove is invalid.
     */
    @Transactional
    public void removeProductFromCart(Long userId, Long productId, int quantityToRemove) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart for user " + userId + ", product " + productId));

        if (quantityToRemove == -1 || quantityToRemove >= cartItem.getQuantity()) {
            cartItemRepository.delete(cartItem);
        } else if (quantityToRemove > 0 && quantityToRemove < cartItem.getQuantity()) {
            cartItem.setQuantity(cartItem.getQuantity() - quantityToRemove);
            cartItemRepository.save(cartItem);
        } else {
            throw new IllegalArgumentException("Invalid quantity to remove: " + quantityToRemove);
        }
    }

    /**
     * Clears all items from the user's cart.
     * @param userId The ID of the user.
     */
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cartItemRepository.deleteAll(cart.getCartItems());
    }
}
