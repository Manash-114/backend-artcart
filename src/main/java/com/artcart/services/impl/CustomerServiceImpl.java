package com.artcart.services.impl;

import com.artcart.model.Customer;
import com.artcart.repository.CustomerRepo;
import com.artcart.services.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepo customerRepo;

    public CustomerServiceImpl(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepo.findByEmail(email);
    }
}
