package uade.apis.backend.features.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uade.apis.backend.features.products.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {
        Page<Product> findByDeletedFalse(Pageable pageable);
}
