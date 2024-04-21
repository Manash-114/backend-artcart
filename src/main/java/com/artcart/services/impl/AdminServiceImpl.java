package com.artcart.services.impl;

import com.artcart.model.Admin;
import com.artcart.model.Seller;
import com.artcart.repository.AdminRepo;
import com.artcart.repository.SellerRepo;
import com.artcart.response.AdminDetailsResDto;
import com.artcart.response.SellerDto;
import com.artcart.services.AdminServices;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminServices {


    private ModelMapper modelMapper;
    private SellerRepo sellerRepo;
    private AdminRepo adminRepo;

    public AdminServiceImpl(ModelMapper modelMapper, SellerRepo sellerRepo, AdminRepo adminRepo) {
        this.modelMapper = modelMapper;
        this.sellerRepo = sellerRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    public void approveSeller(SellerDto sellerDto) {
        Seller seller = modelMapper.map(sellerDto, Seller.class);
        seller.setApproved(true);
        sellerRepo.save(seller);
    }

    @Override
    public void addCategories() {

    }

    @Override
    public AdminDetailsResDto getAdminDetais(String email) {
        Admin byEmail = adminRepo.findByEmail(email);
        AdminDetailsResDto map = modelMapper.map(byEmail, AdminDetailsResDto.class);
        return map;
    }

    @Override
    public List<SellerDto> getAllUnApprovedSeller() {
        List<Seller> byApproved = sellerRepo.findByApprovedAndIsProfileCompleted(false,true);
        List<SellerDto> collect = byApproved.stream().map((item) -> modelMapper.map(item, SellerDto.class)).collect(Collectors.toList());
        return  collect;
    }
}
