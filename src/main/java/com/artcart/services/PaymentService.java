package com.artcart.services;

import com.artcart.request.PaymentReq;
import com.artcart.response.PaymentRes;

public interface PaymentService {
    void createPayment(PaymentReq paymentReq);
    PaymentRes getPaymentDetailsByOrder(String orderId);
}
