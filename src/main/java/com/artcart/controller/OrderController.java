package com.artcart.controller;

import com.artcart.config.JwtTokenProvider;
import com.artcart.model.Customer;
import com.artcart.request.OrderReq;
import com.artcart.request.PaymentReq;
import com.artcart.request.RazorpayPaymentReq;
import com.artcart.response.OrderResDto;
import com.artcart.response.SellerOrderRes;
import com.artcart.services.CustomerService;
import com.artcart.services.OrderService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        OrderResDto order = orderService.createOrder(customer.getId(), orderReq);
        Map<Object,Object> res = new HashMap<>();
        res.put("message","Order Created Successfully");
        res.put("status",HttpStatus.CREATED);
        res.put("data",order);
        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrderHandler(@RequestHeader("Authorization") String token , @PathVariable String  id) throws Exception{

        String email = jwtTokenProvider.getEmailFromToken(token);
        Customer customer = customerService.getCustomerByEmail(email);
        orderService.deleteOrder(id);
        return new ResponseEntity<>("done", HttpStatus.OK);

    }


    @PostMapping("/razor-payment")
    public ResponseEntity<?> makePayment(@RequestBody RazorpayPaymentReq request){
        try {
            RazorpayClient razorpayClient1 = new RazorpayClient("rzp_test_b7cdwAk8TAVDye","36efymUzLAH7YOECDf0LZgBH");
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount",request.getAmount()*100);
            orderRequest.put("currency","INR");
            orderRequest.put("receipt", "receipt#1");
            JSONObject notes = new JSONObject();
            notes.put("notes_key_1","Tea, Earl Grey, Hot");
            orderRequest.put("notes",notes);
            Order order = razorpayClient1.orders.create(orderRequest);
            JSONObject json = order.toJson();
            Map<String, Object> map = json.toMap();
            return new ResponseEntity<>(map,HttpStatus.OK);

        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
//        return new ResponseEntity<>("ok",HttpStatus.OK);
    }

}
