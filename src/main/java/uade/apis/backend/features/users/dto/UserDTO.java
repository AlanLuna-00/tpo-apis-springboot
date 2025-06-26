package uade.apis.backend.features.users.dto;

import lombok.*;
import uade.apis.backend.features.users.entity.UserRole;
import uade.apis.backend.features.users.entity.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private Long id;
  private String email;
  private UserRole role;
  private LocalDateTime createdAt;

  // Método para convertir de User a UserDTO
  public static UserDTO fromUser(User user) {
    return UserDTO.builder()
        .id(user.getId())
        .email(user.getEmail())
        .role(user.getRole())
        .createdAt(user.getCreatedAt())
        .build();
  }
}