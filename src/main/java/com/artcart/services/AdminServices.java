package com.artcart.services;

import com.artcart.response.AdminDetailsResDto;
import com.artcart.response.SellerDto;

import java.util.List;

public interface AdminServices {
    void approveSeller(SellerDto sellerDto);
    void addCategories();

    AdminDetailsResDto getAdminDetais(String email);
    List<SellerDto> getAllUnApprovedSeller();
}
