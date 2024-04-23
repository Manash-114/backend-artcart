package com.artcart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRes {
    private String id;
    private String city;
    private String state;
    private String pincode;
    private String address;
    private String locality;
    private String landmark;
}
