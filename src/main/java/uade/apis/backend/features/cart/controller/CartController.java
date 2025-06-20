package uade.apis.backend.features.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uade.apis.backend.features.cart.dto.CartRequestDTO;
import uade.apis.backend.features.cart.dto.CartResponseDTO;
import uade.apis.backend.features.cart.service.CartService;
import uade.apis.backend.features.users.repository.UserRepository;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addItem(@RequestBody CartRequestDTO dto) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(cartService.addItemToCart(userId, dto));
    }

    @PutMapping("/item/{itemId}")
    public ResponseEntity<CartResponseDTO> updateQuantity(
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, itemId, quantity));
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        Long userId = getCurrentUserId();
        cartService.removeItem(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        Long userId = getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout() {
        Long userId = getCurrentUserId();
        cartService.checkoutCart(userId);
        return ResponseEntity.ok().build();
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
}