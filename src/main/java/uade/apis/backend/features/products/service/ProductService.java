package uade.apis.backend.products.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import uade.apis.backend.config.exceptions.NotFoundException;
import uade.apis.backend.products.dto.CreateProductDTO;
import uade.apis.backend.products.dto.UpdateProductDTO;
import uade.apis.backend.products.entity.Product;
import uade.apis.backend.products.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public Page<Product> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findByDeletedFalse(pageable);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
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

        return productRepository.save(product);
    }

    public Product update(Long id, UpdateProductDTO dto) {
        Product product = getById(id);

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImage(dto.getImage());

        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = getById(id);
        product.setDeleted(true);
        productRepository.save(product);
    }
}

