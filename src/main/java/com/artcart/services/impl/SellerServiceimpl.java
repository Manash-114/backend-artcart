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
    public void acceptOrder(AccecptOrderReq accecptOrderReq) {

        Order order = orderRepo.findById(accecptOrderReq.getOrderId()).orElseThrow(()-> new ResourceNotFoundException("order not found with id"+accecptOrderReq.getOrderId()));
        List<ProductBelongsToOrder> byOrders = productBelongsToOrderRepo.findByOrders(order);

        byOrders.forEach((p)->{
            if(p.getId().compareTo(accecptOrderReq.getProductId())==0){
                p.setCourierName(accecptOrderReq.getCourierName());
                p.setDeliveryStatus(accecptOrderReq.getDeliveryStatus());
            }
        });
        order.setProducts(byOrders);
        orderRepo.save(order);
    }
}
