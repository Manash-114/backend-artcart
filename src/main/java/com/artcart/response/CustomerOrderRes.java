package com.artcart.response;

import com.artcart.model.Address;
import com.artcart.model.ProductBelongsToOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderRes {
    private String orderId;
    private Integer orderAmount;
    private LocalDateTime orderDate;

    private Address address;
    private List<ProductBelongsToOrder> productsBelongsToOrder;
}
