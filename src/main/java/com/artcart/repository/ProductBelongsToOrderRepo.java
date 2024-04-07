package com.artcart.repository;

import com.artcart.model.Order;
import com.artcart.model.ProductBelongsToOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBelongsToOrderRepo extends JpaRepository<ProductBelongsToOrder,String> {

    List<ProductBelongsToOrder> findByOrders(Order order);
}
