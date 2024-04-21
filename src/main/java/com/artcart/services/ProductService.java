package com.artcart.services;

import com.artcart.model.Seller;
import com.artcart.request.ProductAddRequest;
import com.artcart.request.UpdateProduct;
import com.artcart.request.ProductReqDto;
import com.artcart.response.ProductResDto;

import java.util.List;

public interface ProductService {

    void addProduct(ProductAddRequest productAddRequest , Integer sellerId);
    void deleteProduct(String productId);
    ProductResDto updateProduct(String productId, UpdateProduct newProductReqDto);
    List<ProductResDto> getAllProduct();
    ProductResDto getSingleProduct(String productId);

    List<ProductResDto> getAllProductOfSeller(Integer sellerId);


}
