package com.artcart.request;

import com.artcart.model.Address;
import com.artcart.model.ProductBelongsToOrder;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccecptOrderReq {
    private String orderId;
    private String courierName;
}
