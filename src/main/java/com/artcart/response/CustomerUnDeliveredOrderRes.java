package com.artcart.response;

import com.artcart.model.Address;
import com.artcart.model.Product;
import com.artcart.model.ProductBelongsToOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CustomerUnDeliveredOrderRes {
    private String orderId;
    private LocalDateTime orderDate;
    private Address address;
    private ProductBelongsToOrder productBelongsToOrder;
}
