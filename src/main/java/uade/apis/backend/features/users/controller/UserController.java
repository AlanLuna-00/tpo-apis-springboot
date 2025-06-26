package uade.apis.backend.features.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uade.apis.backend.features.users.dto.UserDTO;
import uade.apis.backend.features.users.entity.UserRole;
import uade.apis.backend.features.users.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

  private final UserService userService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    List<UserDTO> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    UserDTO user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDTO> updateUser(
      @PathVariable Long id,
      @RequestBody UpdateUserRequest request) {
    UserDTO updatedUser = userService.updateUser(id, request.getEmail(), request.getRole());
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  // DTO para request de actualización
  public static class UpdateUserRequest {
    private String email;
    private UserRole role;

    // Getters y Setters
    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public UserRole getRole() {
      return role;
    }

    public void setRole(UserRole role) {
      this.role = role;
    }
  }
}