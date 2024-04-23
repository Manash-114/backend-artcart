package com.artcart.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class BillingAddress {
    @Id
    private String id;
    private String customerName;
    private String phoneNumber;
    private String alternatePhoneNumber;
    @ManyToOne
    @JsonBackReference
    private Address address;
}
