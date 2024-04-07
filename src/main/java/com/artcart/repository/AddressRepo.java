package com.artcart.repository;

import com.artcart.model.Address;
import com.artcart.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address,String> {
    List<Address> findByCustomer(Customer customer);
}
