package com.artcart.repository;

import com.artcart.model.Order;
import com.artcart.model.OrderBelongsToSeller;
import com.artcart.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderBelongsToSellerRepo extends JpaRepository<OrderBelongsToSeller,Integer> {
//    List<OrderBelongsToSeller> findBySeller(Seller seller);

//    @Query("select o from Order o join o.sellers s where s.id=:sellerId")
    @Query("select o from OrderBelongsToSeller o where o.seller=:seller and o.status='CREATED'")
    List<OrderBelongsToSeller> findBySellerWithCreated(Seller seller);

    OrderBelongsToSeller findByOrderAndSeller(Order order,Seller seller);
}
