package uade.apis.backend.features.products.dto;
import lombok.*;
import uade.apis.backend.features.products.entity.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String image;

    public static ProductResponseDTO from(Product product) {
        return ProductResponseDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .image(product.getImage())
            .build();
    }
}

