package com.artcart.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Payment {
    @Id
    private String id;
    private Integer amount;
    private String mode;
    private LocalDateTime date;
    @JsonManagedReference
    @ManyToOne
    private Customer customer;
    @JsonBackReference
    @OneToOne
    private Order order;
}
