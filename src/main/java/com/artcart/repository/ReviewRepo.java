package com.artcart.repository;

import com.artcart.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<Review,String> {
}
