package com.artcart.repository;

import com.artcart.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,String> {
    Customer findByEmail(String email);
}
