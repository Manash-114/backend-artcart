package com.artcart.services;

import com.artcart.request.ProductAddRequest;
import com.artcart.request.UpdateProductReq;
import com.artcart.response.ProductResDto;

import java.util.List;

public interface ProductService {

    void addProduct(ProductAddRequest productAddRequest , String sellerId);
    void deleteProduct(String productId);
    ProductResDto updateProduct(String productId, UpdateProductReq newProductReqDto);
    List<ProductResDto> getAllProduct();
    ProductResDto getSingleProduct(String productId);

    List<ProductResDto> getAllProductOfSeller(String sellerId);


}
