package uade.apis.backend.features.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uade.apis.backend.features.cart.dto.CartRequestDTO;
import uade.apis.backend.features.cart.dto.CartResponseDTO;
import uade.apis.backend.features.cart.entity.Cart;
import uade.apis.backend.features.cart.entity.CartItem;
import uade.apis.backend.features.cart.repository.CartItemRepository;
import uade.apis.backend.features.cart.repository.CartRepository;
import uade.apis.backend.features.products.entity.Product;
import uade.apis.backend.features.products.repository.ProductRepository;
import uade.apis.backend.features.users.entity.User;
import uade.apis.backend.features.users.repository.UserRepository;
import uade.apis.backend.shared.exceptions.NotFoundException;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    @Transactional()
    public CartResponseDTO getCartByUserId(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return toDto(cart);
    }

    @Transactional
    public CartResponseDTO addItemToCart(Long userId, CartRequestDTO dto) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepo.findById(dto.getProductId())
            .orElseThrow(() -> new NotFoundException("Product not found"));

        CartItem existingItem = cart.getItems().stream()
            .filter(i -> i.getProduct().getId().equals(dto.getProductId()))
            .findFirst()
            .orElse(null);

        int totalQuantity = dto.getQuantity();
        if (existingItem != null) {
            totalQuantity += existingItem.getQuantity();
        }

        if (product.getStock() < dto.getQuantity()) {
            throw new IllegalStateException("No hay stock suficiente para " + product.getName());
        }

        product.setStock(product.getStock() - dto.getQuantity());
        productRepo.save(product);

        if (existingItem != null) {
            existingItem.setQuantity(totalQuantity);
        } else {
            CartItem newItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(dto.getQuantity())
                .build();
            cart.getItems().add(newItem);
        }

        cartRepo.save(cart);
        return toDto(cart);
    }


    @Transactional
    public CartResponseDTO updateItemQuantity(Long userId, Long itemId, Integer newQuantity) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cart.getItems().stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Item not found"));

        int currentQuantity = item.getQuantity();
        int diff = newQuantity - currentQuantity;

        Product product = item.getProduct();

        if (diff > 0) {
            if (product.getStock() < diff) {
                throw new IllegalStateException("No hay stock suficiente para " + product.getName());
            }
            product.setStock(product.getStock() - diff);
        } else if (diff < 0) {
            product.setStock(product.getStock() + Math.abs(diff));
        }

        item.setQuantity(newQuantity);
        productRepo.save(product);
        cartRepo.save(cart);
        return toDto(cart);
    }

    @Transactional
    public void removeItem(Long userId, Long itemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cart.getItems().stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Item not found"));

        Product product = item.getProduct();
        product.setStock(product.getStock() + item.getQuantity());
        productRepo.save(product);

        cart.getItems().remove(item);
        cartRepo.save(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepo.save(product);
        }

        cart.getItems().clear();
        cartRepo.save(cart);
    }

    @Transactional
    public void checkoutCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepo.save(cart);
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepo.findByUserId(userId).orElseGet(() -> {
            User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
            return cartRepo.save(Cart.builder().user(user).build());
        });
    }

    private CartResponseDTO toDto(Cart cart) {
        var dto = new CartResponseDTO();
        dto.setCartId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setItems(cart.getItems().stream().map(i -> {
            var itemDto = new CartResponseDTO.CartItemDto();
            itemDto.setItemId(i.getId());
            itemDto.setProductId(i.getProduct().getId());
            itemDto.setProductName(i.getProduct().getName());
            itemDto.setQuantity(i.getQuantity());
            itemDto.setPrice(i.getProduct().getPrice());
            return itemDto;
        }).collect(Collectors.toList()));
        return dto;
    }
}