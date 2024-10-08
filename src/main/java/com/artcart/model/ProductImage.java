package com.artcart.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductImage {
    @Id
    private String id;
    private String name;
    @ManyToOne
    @JsonBackReference
    private Product product;

}
