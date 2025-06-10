package uade.apis.backend.features.products.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateProductDTO {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Double price;

    @NotNull
    private Integer stock;

    private String image;

    @NotNull
    private Long categoryId;
}