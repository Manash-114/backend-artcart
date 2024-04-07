package com.artcart.services;

import com.artcart.request.OrderReq;
import com.artcart.response.CustomerOrderRes;
import com.artcart.response.SellerOrderRes;

import java.util.List;

public interface OrderService {
    void createOrder(String customerId , OrderReq orderReq);
    List<CustomerOrderRes>  getAllOrdersOfCustomer(String customerId);
    List<SellerOrderRes> getAllOrderOfSeller(Integer sellerId);
    List<SellerOrderRes>  getAllNewOrderOfSeller(Integer sellerId);

    void modifyOrder(String orderId,OrderReq newOrderReq);

    void deleteOrder(String orderId);
}
