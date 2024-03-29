package com.artcart.services;

import com.artcart.model.Customer;
import com.artcart.request.ReviewReq;

public interface ReviewService {
    void addReview(String productId , Customer customer , ReviewReq reviewReq);
    void deleteReview(String reviewId);
}
