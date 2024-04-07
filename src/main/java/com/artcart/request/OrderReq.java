package com.artcart.request;

import com.artcart.model.*;
import jakarta.persistence.*;
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
public class OrderReq {
    private String orderId;
    private String addressId;
    private List<ProductAddToCartReq> products;
    private PaymentReq paymentReq;
    private String status;
}
