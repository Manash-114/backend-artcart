package com.artcart.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
    @Id
    private String id;
    private String name;
    private String email;
    private String profileImage;
    private String aadhaarNo;
    private String aadhaarImage;
    private String phoneNumber;
    private LocalDateTime regDate;
    private boolean approved;
    private boolean isProfileCompleted;

    @JsonBackReference
    @OneToMany(mappedBy = "seller")
    private List<Product> products;

    @OneToMany(mappedBy = "seller")
    private List<OrderBelongsToSeller> orders; // Updated field
}
