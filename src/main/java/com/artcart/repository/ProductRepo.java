package com.artcart.repository;

import com.artcart.model.Product;
import com.artcart.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,String> {
    List<Product> findBySeller(Seller seller);
}
