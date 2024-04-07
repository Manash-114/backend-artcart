package com.artcart.repository;

import com.artcart.model.Product;
import com.artcart.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepo extends JpaRepository<Seller,Integer> {

    Seller findByEmail(String email);
    List<Seller> findByApproved(boolean type);

    Seller findByProducts(Product product);
}
