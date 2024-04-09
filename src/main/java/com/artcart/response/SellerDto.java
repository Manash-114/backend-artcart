package com.artcart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerDto {
    private Integer id;
    private String name;
    private String email;
    private String profileImage;
    private String aadhaarNo;
    private String aadhaarImage;
    private String phoneNumber;
    private LocalDateTime regDate;
    private boolean approved;
}
