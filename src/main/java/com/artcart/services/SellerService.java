package com.artcart.services;

import com.artcart.request.AccecptOrderReq;
import com.artcart.response.OrderResDto;
import com.artcart.response.SellerDto;

public interface SellerService {

    SellerDto create(SellerDto sellerDto);
    SellerDto getSellerByEmail(String email);
    SellerDto updateProfile(SellerDto sellerDto);

    OrderResDto acceptOrder(AccecptOrderReq accecptOrderReq, String sellerId);
}
