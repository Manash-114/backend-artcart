package com.artcart.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Entity
@Setter
@Getter
public class Review {
    @Id
    private String id;
    private String content;
    private Integer rating;
    private LocalDateTime regDate;
    @ManyToOne
    @JsonBackReference
    private Product product;
    @ManyToOne
    private Customer  customer;
}
