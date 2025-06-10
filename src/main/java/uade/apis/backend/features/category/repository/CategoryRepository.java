package uade.apis.backend.features.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uade.apis.backend.features.category.entity.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    
    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Optional<Category> findByName(String name);
} 