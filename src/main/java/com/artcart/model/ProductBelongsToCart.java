package com.artcart.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductBelongsToCart {
    @Id
    private String id;
    @ManyToOne
    private Cart cart;
    @ManyToOne
    private Product product;
    private Integer productQuantity;
}