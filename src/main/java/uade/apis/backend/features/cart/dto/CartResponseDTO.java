package uade.apis.backend.features.cart.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartResponseDTO {
    private Long cartId;
    private Long userId;
    private List<CartItemDto> items;

    @Data
    public static class CartItemDto {
        private Long itemId;
        private Long productId;
        private String productName;
        private Integer quantity;
        private Double price;
    }
}