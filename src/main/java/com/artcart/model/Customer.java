package com.artcart.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Entity
@Data
public class Customer {
    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImage;

    @OneToMany(mappedBy = "customer")
    private List<Address> address;
    @OneToMany(mappedBy = "customer")
    private List<WishList> wishLists;

    @JsonBackReference
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}
