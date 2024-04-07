package com.artcart.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Courier {
    @Id
    private String id;
    private String name;
    private String contactNumber;
}
