package com.artcart.request;

import com.artcart.model.Address;
import com.artcart.model.ProductBelongsToOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccecptOrderReq {
    private String orderId;
    private String productId;
    private String deliveryStatus;
    private String courierName;
}
