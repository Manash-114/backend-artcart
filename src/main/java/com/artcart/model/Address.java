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
    private String pincode;
    private String address;
    private String locality;
    private String landmark;
    @ManyToOne
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "address")
    @JsonBackReference
    private List<BillingAddress> billingAddress;

//    @JsonBackReference
//    @OneToMany(mappedBy = "address")
//    private List<Order> orders;
}
