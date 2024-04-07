package com.artcart.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "product_belongs_to_order")
public class ProductBelongsToOrder {
    @Id
    private String id;
    private Integer productQuantity;
    private String deliveryStatus;
    private String courierName;
    @ManyToOne
    private Product products;
    @JsonBackReference
    @ManyToOne
    private Order orders;
}
