package uade.apis.backend.features.cart.dto;

import lombok.Data;

@Data
public class CartRequestDTO {
    private Long productId;
    private Integer quantity;
}
