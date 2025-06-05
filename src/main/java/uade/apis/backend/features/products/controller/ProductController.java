package uade.apis.backend.features.products.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uade.apis.backend.features.products.dto.CreateProductDTO;
import uade.apis.backend.features.products.dto.ProductResponseDTO;
import uade.apis.backend.features.products.dto.UpdateProductDTO;
import uade.apis.backend.features.products.entity.Product;
import uade.apis.backend.features.products.service.ProductService;
import uade.apis.backend.shared.responses.PageResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Product> result = productService.getAll(page, size);
        List<ProductResponseDTO> items = result
            .getContent()
            .stream()
            .map(ProductResponseDTO::from)
            .toList();

        PageResponse<ProductResponseDTO> response = new PageResponse<>(
            items,
            new PageResponse.Meta(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
            )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(ProductResponseDTO.from(product));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CreateProductDTO dto) {
        Product saved = productService.create(dto);
        URI location = URI.create("/products/" + saved.getId());
        return ResponseEntity.created(location).build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid UpdateProductDTO dto) {
        productService.update(id, dto);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
