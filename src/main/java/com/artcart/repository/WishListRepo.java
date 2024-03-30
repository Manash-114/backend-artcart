package com.artcart.repository;

import com.artcart.model.Customer;
import com.artcart.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepo extends JpaRepository<WishList,String> {
    List<WishList> findByCustomer(Customer customer);
}
