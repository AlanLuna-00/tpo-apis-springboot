package uade.apis.backend.features.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uade.apis.backend.features.auth.dto.LoginDTO;
import uade.apis.backend.features.auth.dto.RegisterDTO;
import uade.apis.backend.features.auth.service.AuthService;
import uade.apis.backend.features.users.entity.User;
import uade.apis.backend.features.users.service.UserService;
import uade.apis.backend.security.JwtTokenProvider;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthService authService;
        private final UserService userService;
        private final JwtTokenProvider jwtTokenProvider;

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody @Valid LoginDTO request) {
                String token = authService.login(request);
                User user = userService.findByEmail(request.getEmail());

                ResponseCookie cookie = ResponseCookie.from("uade-apis", token)
                                .httpOnly(true)
                                .secure(false) // * True si salimos de HTTP
                                .path("/")
                                .maxAge(86400) // * 1 dia
                                .sameSite("Lax")
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(Map.of(
                                                "token", token,
                                                "user", Map.of(
                                                                "id", user.getId(),
                                                                "email", user.getEmail(),
                                                                "role", user.getRole())));
        }

        @PostMapping("/logout")
        public ResponseEntity<?> logout() {
                var cookie = ResponseCookie.from("uade-apis", "")
                                .httpOnly(true)
                                .path("/")
                                .maxAge(0)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(Map.of("message", "Logout exitoso"));
        }

        @GetMapping("/me")
        public ResponseEntity<?> me() {
                var authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = (String) authentication.getPrincipal();

                User user = userService.findByEmail(email);

                return ResponseEntity.ok(Map.of(
                                "id", user.getId(),
                                "email", user.getEmail(),
                                "role", user.getRole()));
        }

        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO request) {
                User newUser = authService.register(request);

                // Generar token para el usuario recién registrado
                String token = authService.generateTokenForUser(newUser);

                ResponseCookie cookie = ResponseCookie.from("uade-apis", token)
                                .httpOnly(true)
                                .secure(false) // * True si salimos de HTTP
                                .path("/")
                                .maxAge(86400) // * 1 día
                                .sameSite("Lax")
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(Map.of(
                                                "user", Map.of(
                                                                "id", newUser.getId(),
                                                                "email", newUser.getEmail(),
                                                                "role", newUser.getRole())));
        }
}
