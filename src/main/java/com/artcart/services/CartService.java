package com.artcart.services;

import com.artcart.request.ProductAddToCartReq;
import com.artcart.response.CartResDto;

import java.util.List;

public interface CartService {
    CartResDto crateCart(String customerId);
    void deleteCart(String cartId);
    CartResDto getCustomerCart(String customerId);
    CartResDto getCartById(String cartId);

    CartResDto addProductToCart(String cartId , List<ProductAddToCartReq> productAddToCartReq);
}
