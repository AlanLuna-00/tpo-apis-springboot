package uade.apis.backend.features.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uade.apis.backend.features.auth.dto.LoginDTO;
import uade.apis.backend.features.auth.dto.RegisterDTO;
import uade.apis.backend.features.users.entity.User;
import uade.apis.backend.features.users.entity.UserRole;
import uade.apis.backend.features.users.repository.UserRepository;
import uade.apis.backend.security.JwtTokenProvider;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(LoginDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadCredentialsException("Email o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }

        return jwtTokenProvider.generateToken(user);
    }

    public User register(RegisterDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(UserRole.USER)
            .build();

        return userRepository.save(user);
    }
}

