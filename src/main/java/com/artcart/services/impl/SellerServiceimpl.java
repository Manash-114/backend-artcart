package com.artcart.services.impl;

import com.artcart.exception.ResourceNotFoundException;
import com.artcart.model.Order;
import com.artcart.model.OrderBelongsToSeller;
import com.artcart.model.ProductBelongsToOrder;
import com.artcart.model.Seller;
import com.artcart.repository.OrderBelongsToSellerRepo;
import com.artcart.repository.OrderRepo;
import com.artcart.repository.ProductBelongsToOrderRepo;
import com.artcart.repository.SellerRepo;
import com.artcart.request.AccecptOrderReq;
import com.artcart.response.OrderResDto;
import com.artcart.response.SellerDto;
import com.artcart.services.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SellerServiceimpl implements SellerService {

    private SellerRepo sellerRepo;
    private ModelMapper modelMapper;

    private OrderRepo orderRepo;

    @Autowired
    private OrderBelongsToSellerRepo orderBelongsToSellerRepo;
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
    public OrderResDto acceptOrder(AccecptOrderReq accecptOrderReq,String sellerId) {
        log.info(String.format("data %s",accecptOrderReq));
        Seller seller = sellerRepo.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("seller not found with id +" + sellerId));
        Order order = orderRepo.findById(accecptOrderReq.getOrderId()).orElseThrow(()-> new ResourceNotFoundException("order not found with id"+accecptOrderReq.getOrderId()));
        List<ProductBelongsToOrder> byOrders = productBelongsToOrderRepo.findByOrders(order);
        OrderBelongsToSeller byOrder = orderBelongsToSellerRepo.findByOrderAndSeller(order,seller);
        byOrder.setStatus("SHIPPED");
        List<ProductBelongsToOrder> collect = byOrders.stream().filter((b) -> b.getProducts().getSeller() == seller).collect(Collectors.toList());
        collect.forEach((i)->{
//            System.out.println(i.g());
            i.setCourierName(accecptOrderReq.getCourierName());

            i.setDeliveryStatus("SHIPPED");
        });
        order.setProducts(collect);
        orderBelongsToSellerRepo.save(byOrder);
        Order save = orderRepo.save(order);

        OrderResDto orderResDto =  OrderResDto
                .builder()
                .id(save.getId())
                .orderDate(save.getOrderDate())
                .build();
        return orderResDto;

    }
}
