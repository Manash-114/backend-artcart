package com.artcart.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    private String id;
    private Integer price;
    @OneToOne
    private Customer customer;
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL)
    private Set<ProductBelongsToCart> productBelongsToCart;
}
