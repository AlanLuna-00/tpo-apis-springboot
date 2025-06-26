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
                                        .role(UserRole.USER)
                                        .build());
                        log.info("✅ Usuario USER creado");
                }
        }

        private void createCategories() {
                if (categoryRepository.count() == 0) {
                        List<Category> categories = List.of(
                                        Category.builder()
                                                        .name("Tecnologia")
                                                        .description("Productos tecnológicos y electrónicos")
                                                        .active(true)
                                                        .build(),
                                        Category.builder()
                                                        .name("bebidas")
                                                        .description("Bebidas y refrescos")
                                                        .active(true)
                                                        .build(),
                                        Category.builder()
                                                        .name("otros")
                                                        .description("Otros productos diversos")
                                                        .active(true)
                                                        .build());

                        categoryRepository.saveAll(categories);
                        log.info("✅ Categorías de ejemplo creadas");
                }
        }

        private void createProducts() {
                if (productRepository.count() == 0) {
                        Category tecnologia = categoryRepository.findByName("Tecnologia")
                                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                        Category bebidas = categoryRepository.findByName("bebidas")
                                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                        Category otros = categoryRepository.findByName("otros")
                                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

                        List<Product> sampleProducts = List.of(
                                        Product.builder()
                                                        .name("Macbook Pro")
                                                        .description("Macbook Pro M4 16GB")
                                                        .price(2000.0)
                                                        .stock(5)
                                                        .image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQoCxxKSxTtPEx0dxVf0Xktg0oUCZ4PfTzwoQ&s")
                                                        .category(otros)
                                                        .deleted(false)
                                                        .build(),
                                        Product.builder()
                                                        .name("Iphone 14")
                                                        .description("Descripción del producto 2")
                                                        .price(800.0)
                                                        .stock(6)
                                                        .image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCGn0N5jOZHd4F6atw6IDvrWTMmitxfPjNgA&s")
                                                        .category(otros)
                                                        .deleted(false)
                                                        .build(),
                                        Product.builder()
                                                        .name("Coca Cola 1.5L")
                                                        .description("Botella de Coca Cola de 1.5L")
                                                        .price(2149.99)
                                                        .stock(6)
                                                        .image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwvpC1Gn03JxTpIHCR93jZCtee81Ynp1WphQ&s")
                                                        .category(bebidas)
                                                        .deleted(false)
                                                        .build(),
                                        Product.builder()
                                                        .name("AirPods Pro")
                                                        .description("Buenardopolis")
                                                        .price(500.0)
                                                        .stock(100)
                                                        .image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWkSWnL6892QKKyasE7ASJfgfa9IfibsouRA&s")
                                                        .category(otros)
                                                        .deleted(false)
                                                        .build(),
                                        Product.builder()
                                                        .name("Monitor")
                                                        .description("LG")
                                                        .price(1000.0)
                                                        .stock(10)
                                                        .image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThEhmVnKwD1Yr-yZZBHLCdZuFWVeFIve7OwA&s")
                                                        .category(otros)
                                                        .deleted(false)
                                                        .build(),
                                        Product.builder()
                                                        .name("Go Pro")
                                                        .description("GoPro, la mejor del pais.")
                                                        .price(1300.0)
                                                        .stock(10)
                                                        .image("https://via.placeholder.com/300x300/FF6B6B/FFFFFF?text=GoPro")
                                                        .category(tecnologia)
                                                        .deleted(false)
                                                        .build(),
                                        Product.builder()
                                                        .name("Dron")
                                                        .description("Flipado")
                                                        .price(100.0)
                                                        .stock(6)
                                                        .image("https://via.placeholder.com/300x300/4ECDC4/FFFFFF?text=Dron")
                                                        .category(tecnologia)
                                                        .deleted(false)
                                                        .build());

                        productRepository.saveAll(sampleProducts);
                        log.info("✅ Productos de ejemplo creados");
                }
        }
}
