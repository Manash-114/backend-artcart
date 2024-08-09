package com.artcart.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingInAndSingUp
{
    @Id
    private String id;
    private String email;
    private String password;
    private String role;
}
