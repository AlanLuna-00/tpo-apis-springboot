package uade.apis.backend.features.products.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import uade.apis.backend.shared.exceptions.NotFoundException;
import uade.apis.backend.features.products.dto.CreateProductDTO;
import uade.apis.backend.features.products.dto.UpdateProductDTO;
import uade.apis.backend.features.products.entity.Product;
import uade.apis.backend.features.products.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public Page<Product> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.productRepository.findByDeletedFalse(pageable);
    }

    public Product getById(Long id) {
        return this.productRepository.findById(id)
            .filter(p -> !p.isDeleted())
            .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
    }

    public Product create(CreateProductDTO dto) {
        Product product = Product.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .price(dto.getPrice())
            .stock(dto.getStock())
            .image(dto.getImage())
            .deleted(false)
            .build();

        return this.productRepository.save(product);
    }

    public void update(Long id, UpdateProductDTO dto) {
        Product product = this.getById(id);

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImage(dto.getImage());

        this.productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = this.getById(id);
        product.setDeleted(true);
        this.productRepository.save(product);
    }
}

