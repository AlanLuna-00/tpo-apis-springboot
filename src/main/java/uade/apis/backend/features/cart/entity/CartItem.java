package uade.apis.backend.features.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import uade.apis.backend.features.products.entity.Product;
import uade.apis.backend.features.users.entity.User;

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
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
}
