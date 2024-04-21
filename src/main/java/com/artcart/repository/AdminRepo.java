package com.artcart.repository;

import com.artcart.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin,String> {
    Admin findByEmail(String email);
}
