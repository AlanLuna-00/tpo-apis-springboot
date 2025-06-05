package uade.apis.backend.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uade.apis.backend.users.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
