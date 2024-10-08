package com.artcart.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_table")
public class Order {
    @Id
    private String id;
    private LocalDateTime orderDate;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private BillingAddress billingAddress;
    @JsonManagedReference
    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL)
    private List<ProductBelongsToOrder> products;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderBelongsToSeller> sellers; // Updated field
    @OneToOne(cascade = CascadeType.ALL)
    private Payment payment;
    @ManyToOne
    @JsonManagedReference
    private Customer customer;
    private String status;
}
