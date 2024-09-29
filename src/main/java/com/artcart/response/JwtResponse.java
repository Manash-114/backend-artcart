package com.artcart.response;

import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    String roles;
}
