package uade.apis.backend.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uade.apis.backend.features.category.entity.Category;
import uade.apis.backend.features.category.repository.CategoryRepository;
import uade.apis.backend.features.products.entity.Product;
import uade.apis.backend.features.products.repository.ProductRepository;
import uade.apis.backend.features.users.entity.User;
import uade.apis.backend.features.users.entity.UserRole;
import uade.apis.backend.features.users.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        createUsers();
        createCategories();
        createProducts();
    }

    private void createUsers() {
        if (!userRepository.existsByEmail("admin@uade.com")) {
            userRepository.save(User.builder()
                .email("admin@uade.com")
                .password(passwordEncoder.encode("admin123"))
                .role(UserRole.ADMIN)
                .build());
            log.info("✅ Usuario ADMIN creado");
        }

        if (!userRepository.existsByEmail("user@uade.com")) {
            userRepository.save(User.builder()
                .email("user@uade.com")
                .password(passwordEncoder.encode("user123"))
                .role(UserRole.ADMIN)
                .build());
            log.info("✅ Usuario USER creado");
        }
    }

    private void createCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                Category.builder()
                    .name("Calzado")
                    .description("Zapatillas, zapatos y calzado deportivo")
                    .active(true)
                    .build(),
                Category.builder()
                    .name("Indumentaria")
                    .description("Ropa deportiva y casual")
                    .active(true)
                    .build(),
                Category.builder()
                    .name("Accesorios")
                    .description("Mochilas, bolsos y accesorios varios")
                    .active(true)
                    .build()
            );

            categoryRepository.saveAll(categories);
            log.info("✅ Categorías de ejemplo creadas");
        }
    }

    private void createProducts() {
        if (productRepository.count() == 0) {
            Category calzado = categoryRepository.findByName("Calzado")
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            Category indumentaria = categoryRepository.findByName("Indumentaria")
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            Category accesorios = categoryRepository.findByName("Accesorios")
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            List<Product> sampleProducts = List.of(
                Product.builder()
                    .name("Zapatilla Running")
                    .description("Zapatilla deportiva ideal para running")
                    .price(12999.99)
                    .stock(25)
                    .image("https://via.placeholder.com/150")
                    .category(calzado)
                    .deleted(false)
                    .build(),
                Product.builder()
                    .name("Campera Rompeviento")
                    .description("Campera impermeable con capucha")
                    .price(21999.00)
                    .stock(15)
                    .image("https://via.placeholder.com/150")
                    .category(indumentaria)
                    .deleted(false)
                    .build(),
                Product.builder()
                    .name("Mochila UADE")
                    .description("Mochila oficial con compartimento para notebook")
                    .price(8999.50)
                    .stock(30)
                    .image("https://via.placeholder.com/150")
                    .category(accesorios)
                    .deleted(false)
                    .build()
            );

            productRepository.saveAll(sampleProducts);
            log.info("✅ Productos de ejemplo creados");
        }
    }
}
