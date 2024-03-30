package com.artcart.services;

import com.artcart.model.Customer;

public interface CustomerService {
    Customer getCustomerByEmail(String email);
}
