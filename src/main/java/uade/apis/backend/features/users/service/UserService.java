package uade.apis.backend.users.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uade.apis.backend.config.exceptions.NotFoundException;
import uade.apis.backend.users.entity.User;
import uade.apis.backend.users.entity.UserRole;
import uade.apis.backend.users.repository.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use.");
        }

        User user = User.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .roles(Collections.singleton(UserRole.USER))
            .build();

        return userRepository.save(user);
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
