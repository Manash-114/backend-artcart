package com.artcart.request;

import com.artcart.model.Address;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BillingAddressReq {
    private String customerName;
    private String phoneNumber;
    private String alternatePhoneNumber;
    private String addressId;
}
