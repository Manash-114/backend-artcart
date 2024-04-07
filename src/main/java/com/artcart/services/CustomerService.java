package com.artcart.services;

import com.artcart.model.Address;
import com.artcart.model.Customer;
import com.artcart.request.AddressReq;
import com.artcart.response.AddressRes;

import java.util.List;

public interface CustomerService {
    Customer getCustomerByEmail(String email);
    AddressRes addNewAddress(String customerId , AddressReq addressReq);
    AddressRes getSingleAddress(String customerId , String addressId);
    List<AddressRes> getAllAddress(String customerId);

}
