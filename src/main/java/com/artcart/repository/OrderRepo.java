package com.artcart.repository;

import com.artcart.model.Customer;
import com.artcart.model.Order;
import com.artcart.model.Seller;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,String> {
    List<Order> findByStatus(String status);

    @Query("select o from Order o join o.sellers s where s.id=:sellerId AND o.status='CREATED'")
    List<Order> findNewOrderBySellerId(@Param("sellerId") Integer sellerId);
    @Query("select o from Order o join o.sellers s where s.id=:sellerId")
    List<Order> findAllOrderBySellerId(@Param("sellerId") Integer sellerId);
    List<Order> findByCustomer(Customer customer);

}
