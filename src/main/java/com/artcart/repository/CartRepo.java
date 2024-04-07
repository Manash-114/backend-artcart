package com.artcart.repository;

import com.artcart.model.Cart;
import com.artcart.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart,String> {
    Cart findByCustomer(Customer customer);
}
