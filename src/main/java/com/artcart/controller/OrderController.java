package com.artcart.controller;

import com.artcart.config.JwtTokenProvider;
import com.artcart.model.Customer;
import com.artcart.request.OrderReq;
import com.artcart.response.SellerOrderRes;
import com.artcart.services.CustomerService;
import com.artcart.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/order")
@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
public class OrderController {

    private JwtTokenProvider jwtTokenProvider;
    private OrderService orderService;

    private CustomerService customerService;

    public OrderController(JwtTokenProvider jwtTokenProvider, OrderService orderService, CustomerService customerService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<?> createOrderHandler(@RequestHeader("Authorization") String token , @RequestBody OrderReq orderReq) throws Exception{

        String email = jwtTokenProvider.getEmailFromToken(token);
        Customer customer = customerService.getCustomerByEmail(email);

        orderService.createOrder(customer.getId(),orderReq);

        return new ResponseEntity<>("done", HttpStatus.OK);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrderHandler(@RequestHeader("Authorization") String token , @PathVariable String  id) throws Exception{

        String email = jwtTokenProvider.getEmailFromToken(token);
        Customer customer = customerService.getCustomerByEmail(email);

        orderService.deleteOrder(id);
        return new ResponseEntity<>("done", HttpStatus.OK);

    }

}
