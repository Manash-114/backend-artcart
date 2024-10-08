package com.artcart.response;


import com.artcart.model.Address;
import com.artcart.model.BillingAddress;
import com.artcart.model.ProductBelongsToOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SellerOrderRes {
    private String orderId;
    private BillingAddress billingAddress;
    private Address address;
    private List<ProductBelongsToOrder> productsBelongsToOrder;
}
