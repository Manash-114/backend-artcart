package com.artcart.repository;

import com.artcart.model.Customer;
import com.artcart.model.Product;
import com.artcart.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<Review,String> {
    Review findByCustomerAndProduct(Customer customer , Product product);
}
