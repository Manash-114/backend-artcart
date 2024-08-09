package com.artcart.services;

import com.artcart.request.OrderReq;
import com.artcart.response.CustomerOrderRes;
import com.artcart.response.CustomerUnDeliveredOrderRes;
import com.artcart.response.SellerOrderRes;

import java.util.List;

public interface OrderService {
    void createOrder(String customerId , OrderReq orderReq);
    List<CustomerUnDeliveredOrderRes>  getAllOrdersOfCustomer(String customerId);
    List<CustomerUnDeliveredOrderRes>  getAllUnDeliveredOrderOfCustomer(String customerId);
    List<SellerOrderRes> getAllOrderOfSeller(Integer sellerId);
    List<SellerOrderRes>  getAllNewOrderOfSeller(Integer sellerId);

    void modifyOrder(String orderId,OrderReq newOrderReq);

    void deleteOrder(String orderId);
}
