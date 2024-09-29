package com.artcart.services;

import com.artcart.request.OrderReq;
import com.artcart.response.CustomerOrderRes;
import com.artcart.response.CustomerUnDeliveredOrderRes;
import com.artcart.response.OrderResDto;
import com.artcart.response.SellerOrderRes;

import java.util.List;

public interface OrderService {
    OrderResDto createOrder(String customerId , OrderReq orderReq);
    List<CustomerUnDeliveredOrderRes>  getAllOrdersOfCustomer(String customerId);
    List<CustomerUnDeliveredOrderRes>  getAllUnDeliveredOrderOfCustomer(String customerId);
    List<SellerOrderRes> getAllOrderOfSeller(String sellerId);
    List<SellerOrderRes>  getAllNewOrderOfSeller(String sellerId);

    void modifyOrder(String orderId,OrderReq newOrderReq);

    void deleteOrder(String orderId);
}
