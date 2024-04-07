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
@Table(name = "seller_table")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String email;
    private String profileImage;
    private String aadhaarNo;
    private String aadhaarImage;
    private String phoneNumber;
    private LocalDateTime regDate;
    private boolean approved;

    @JsonBackReference
    @OneToMany(mappedBy = "seller")
    private List<Product> products;
}
