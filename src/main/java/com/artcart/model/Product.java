package com.artcart.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "product_table")
public class Product {
    @Id
    private String id;
    private String name;
    private Integer price;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    private boolean stock;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product")
    @JsonManagedReference
    private List<ProductImage> productImages;
    @ManyToOne
//    @JsonManagedReference
    private Category category;
    @ManyToOne
    @JsonBackReference
    private Seller seller;
    @JsonBackReference
    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

}
