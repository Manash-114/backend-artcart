package com.artcart.controller;

import com.artcart.config.JwtTokenProvider;
import com.artcart.model.Customer;
import com.artcart.request.AddressReq;
import com.artcart.response.AddressRes;
import com.artcart.response.CustomerOrderRes;
import com.artcart.services.CustomerService;
import com.artcart.services.OrderService;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
public class CustomerController {

    private JwtTokenProvider jwtTokenProvider;
    private CustomerService customerService;
    private OrderService orderService;

    public CustomerController(JwtTokenProvider jwtTokenProvider, CustomerService customerService, OrderService orderService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @PostMapping("/new-address")
    public ResponseEntity<AddressRes> addNewAddressHandler(@RequestHeader("Authorization") String token, @RequestBody AddressReq addressReq) throws Exception {
        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        Customer customer = customerService.getCustomerByEmail(emailFromToken);
        AddressRes addressRes = customerService.addNewAddress(customer.getId(), addressReq);
        return new ResponseEntity<>(addressRes, HttpStatus.OK);
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressRes>> getAllAddress(@RequestHeader("Authorization") String token) throws Exception {
        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        Customer customer = customerService.getCustomerByEmail(emailFromToken);
        List<AddressRes> allAddress = customerService.getAllAddress(customer.getId());
        return new ResponseEntity<>(allAddress, HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(@RequestHeader("Authorization") String token) throws Exception {
        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        Customer customer = customerService.getCustomerByEmail(emailFromToken);
        List<CustomerOrderRes> allOrdersOfCustomer = orderService.getAllOrdersOfCustomer(customer.getId());
        return new ResponseEntity<>(allOrdersOfCustomer, HttpStatus.OK);
    }


}
