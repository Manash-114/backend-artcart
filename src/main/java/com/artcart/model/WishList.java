package com.artcart.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class WishList {
    @Id
    private String id;
    private String name;
    private LocalDateTime createdDate;
    @ManyToOne
    private Customer customer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "product_belongs_to_wishList")
    private List<Product> products;
}
