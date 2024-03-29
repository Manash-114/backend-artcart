package com.artcart.services.impl;

import com.artcart.exception.ResourceNotFoundException;
import com.artcart.model.Customer;
import com.artcart.model.Product;
import com.artcart.model.Review;
import com.artcart.repository.ProductRepo;
import com.artcart.repository.ReviewRepo;
import com.artcart.request.ReviewReq;
import com.artcart.services.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepo reviewRepo;
    private ProductRepo productRepo;

    public ReviewServiceImpl(ReviewRepo reviewRepo, ProductRepo productRepo) {
        this.reviewRepo = reviewRepo;
        this.productRepo = productRepo;
    }

    @Override
    public void addReview(String productId, Customer customer , ReviewReq reviewReq) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found with id " + productId));

        Review byCustomer = reviewRepo.findByCustomerAndProduct(customer,product);
        if(byCustomer != null){
            throw new ResourceNotFoundException("review found ");
        }
        Review review = new Review();
        review.setId(UUID.randomUUID().toString());
        review.setContent(reviewReq.getContent());
        review.setRating(reviewReq.getRating());
        review.setProduct(product);
        review.setRegDate(LocalDateTime.now());
        review.setCustomer(customer);
        reviewRepo.save(review);
    }
    @Override
    public void deleteReview(String reviewId) {
        Review review = reviewRepo.findById(reviewId).orElseThrow(() -> new ResourceNotFoundException("review not found with id" + reviewId));
        reviewRepo.delete(review);
    }
}
