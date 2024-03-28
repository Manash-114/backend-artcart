package com.artcart.services;

import com.artcart.response.SellerDto;

import java.util.List;

public interface AdminServices {
    void approveSeller(SellerDto sellerDto);
    void addCategories();
    List<SellerDto> getAllUnApprovedSeller();
}
