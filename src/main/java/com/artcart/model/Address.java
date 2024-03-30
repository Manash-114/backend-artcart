package com.artcart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    private String id;
    private String city;
    private String state;
    private String zipCode;
    private String street;
    @ManyToOne
    private Customer customer;
}
