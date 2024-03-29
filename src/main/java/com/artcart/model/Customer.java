package com.artcart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
public class Customer {
    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImage;
    private String city;
    private String state;
    private String zipCode;
    private String street;
}
