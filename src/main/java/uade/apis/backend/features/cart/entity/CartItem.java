package uade.apis.backend.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import uade.apis.backend.products.entity.Product;
import uade.apis.backend.users.entity.User;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Product product;

    private Integer quantity;
}
