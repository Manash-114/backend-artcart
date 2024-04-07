package com.artcart.controller;

import com.artcart.config.JwtTokenProvider;
import com.artcart.model.Customer;
import com.artcart.request.ProductAddToCartReq;
import com.artcart.response.CartResDto;
import com.artcart.services.CartService;
import com.artcart.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer/cart")
@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
public class CartController {

    private CartService cartService;
    private CustomerService customerService;
    private JwtTokenProvider jwtTokenProvider;

    public CartController(CartService cartService, CustomerService customerService, JwtTokenProvider jwtTokenProvider) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

//    @PostMapping
//    public ResponseEntity<?> createCartHandler(@RequestHeader("Authorization") String token) throws Exception {
//        String customerEmail = jwtTokenProvider.getEmailFromToken(token);
//        Customer customerByEmail = customerService.getCustomerByEmail(customerEmail);
//        CartResDto cartResDto = cartService.crateCart(customerByEmail.getId());
//        Map<String,Object> res = new HashMap<>();
//        res.put("message","cart created successfully");
//        res.put("data",cartResDto);
//        return new ResponseEntity<>(res , HttpStatus.CREATED);
//    }

    @PostMapping("/add-product")
    public ResponseEntity<?> addProductToCartHandler(@RequestHeader("Authorization") String token,@RequestBody List<ProductAddToCartReq> productAddToCartReq) throws Exception {
        String customerEmail = jwtTokenProvider.getEmailFromToken(token);
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        CartResDto cartResDto = cartService.crateCart(customer.getId());
        CartResDto updateCart = cartService.addProductToCart(cartResDto.getId(), productAddToCartReq);
        Map<String,Object> res = new HashMap<>();
        res.put("message","product added to cart successfully");
        res.put("data",updateCart);
        return new ResponseEntity<>(res , HttpStatus.CREATED);
    }
}
