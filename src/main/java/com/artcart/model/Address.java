package com.artcart.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    private String id;
    private String city;
    private String state;
    private String zipCode;
    private String street;
    @ManyToOne
    @JsonBackReference
    private Customer customer;
    @JsonBackReference
    @OneToMany(mappedBy = "address")
    private List<Order> orders;
}
