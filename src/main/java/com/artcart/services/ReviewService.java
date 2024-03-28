package com.artcart.services;

import com.artcart.request.ReviewReq;

public interface ReviewService {
    void addReview(String productId , ReviewReq reviewReq);
    void deleteReview(String reviewId);
}
