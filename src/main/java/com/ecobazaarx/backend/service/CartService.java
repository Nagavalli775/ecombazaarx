package com.ecobazaarx.backend.service;

import com.ecobazaarx.backend.entity.Cart;
import com.ecobazaarx.backend.entity.CartItem;
import com.ecobazaarx.backend.entity.Product;
import com.ecobazaarx.backend.entity.User;
import com.ecobazaarx.backend.repository.CartItemRepository;
import com.ecobazaarx.backend.repository.CartRepository;
import com.ecobazaarx.backend.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    // Create or get user's cart
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = Cart.builder()
                            .user(User.builder().id(userId).build())
                            .build(); // timestamps created automatically
                    return cartRepository.save(cart);
                });
    }

    // Add product to cart
    public Cart addToCart(Long userId, Long productId, int quantity) {

        Cart cart = getOrCreateCart(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem existingItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();

            cartItemRepository.save(newItem);
        }

        return cart;
    }

    // Update quantity of product
    public Cart updateQuantity(Long userId, Long productId, int quantity) {

        Cart cart = getOrCreateCart(userId);

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return cart;
    }

    // Remove product
    public void removeFromCart(Long userId, Long productId) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        cartItemRepository.delete(item);
    }

    // Get all items
    public List<CartItem> getCartItems(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cartItemRepository.findByCartId(cart.getId());
    }

    // Calculate total carbon footprint
    public double calculateCarbon(Long userId) {

        Cart cart = getOrCreateCart(userId);

        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());

        return items.stream()
                .mapToDouble(i ->
                        i.getProduct().getCarbonImpactKg() * i.getQuantity()
                ).sum();
    }

    // Clear cart after checkout
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        cartItemRepository.deleteAll(items);
    }
}
