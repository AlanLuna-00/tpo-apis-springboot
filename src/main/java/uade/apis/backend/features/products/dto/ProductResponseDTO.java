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
    private CategoryDTO category;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDTO {
        private Long id;
        private String name;
    }

    public static ProductResponseDTO from(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImage(product.getImage());
        
        if (product.getCategory() != null) {
            dto.setCategory(new CategoryDTO(
                product.getCategory().getId(),
                product.getCategory().getName()
            ));
        }
        
        return dto;
    }
}

