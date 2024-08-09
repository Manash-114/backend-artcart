package com.artcart.services.impl;

import com.artcart.exception.ResourceNotFoundException;
import com.artcart.model.Order;
import com.artcart.model.ProductBelongsToOrder;
import com.artcart.model.Seller;
import com.artcart.repository.OrderRepo;
import com.artcart.repository.ProductBelongsToOrderRepo;
import com.artcart.repository.SellerRepo;
import com.artcart.request.AccecptOrderReq;
import com.artcart.response.SellerDto;
import com.artcart.services.SellerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SellerServiceimpl implements SellerService {

    private SellerRepo sellerRepo;
    private ModelMapper modelMapper;

    private OrderRepo orderRepo;

    private ProductBelongsToOrderRepo productBelongsToOrderRepo;

    public SellerServiceimpl(SellerRepo sellerRepo, ModelMapper modelMapper, OrderRepo orderRepo, ProductBelongsToOrderRepo productBelongsToOrderRepo) {
        this.sellerRepo = sellerRepo;
        this.modelMapper = modelMapper;
        this.orderRepo = orderRepo;
        this.productBelongsToOrderRepo = productBelongsToOrderRepo;
    }

    @Override
    public SellerDto create(SellerDto sellerDto) {
        Seller seller = modelMapper.map(sellerDto, Seller.class);
        Seller byEmail = sellerRepo.findByEmail(sellerDto.getEmail());
        //update data
        byEmail.setName(seller.getName());
        byEmail.setAadhaarImage(seller.getAadhaarImage());
        byEmail.setProfileImage(seller.getProfileImage());
        byEmail.setPhoneNumber(seller.getPhoneNumber());
        byEmail.setAadhaarNo(seller.getAadhaarNo());
        byEmail.setProfileCompleted(true);
        Seller save = sellerRepo.save(byEmail);
        return modelMapper.map(save,SellerDto.class);
    }

    @Override
    public SellerDto getSellerByEmail(String email) {
        Seller byEmail = sellerRepo.findByEmail(email);
        return modelMapper.map(byEmail,SellerDto.class);
    }

    @Override
    public SellerDto updateProfile(SellerDto sellerDto) {
        return null;
    }

    @Override
    public void acceptOrder(AccecptOrderReq accecptOrderReq,String sellerId) {

        Seller seller = sellerRepo.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("seller not found with id +" + sellerId));

        Order order = orderRepo.findById(accecptOrderReq.getOrderId()).orElseThrow(()-> new ResourceNotFoundException("order not found with id"+accecptOrderReq.getOrderId()));

        List<ProductBelongsToOrder> byOrders = productBelongsToOrderRepo.findByOrders(order);
        List<ProductBelongsToOrder> collect = byOrders.stream().filter((b) -> b.getProducts().getSeller() == seller).collect(Collectors.toList());

        collect.forEach((i)->{
//            System.out.println(i.g());
            i.setCourierName(accecptOrderReq.getCourierName());
            i.setDeliveryStatus("SHIPPED");
        });
        order.setProducts(collect);
        orderRepo.save(order);
    }
}
