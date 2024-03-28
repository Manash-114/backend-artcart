package com.artcart.services;

import com.artcart.request.UpdateProduct;
import com.artcart.response.ProductReqDto;
import com.artcart.response.ProductResDto;

import java.util.List;

public interface ProductService {

    void addProduct(ProductReqDto productReqDto);
    void deleteProduct(String productId);
    ProductResDto updateProduct(String productId, UpdateProduct newProductReqDto);
    List<ProductResDto> getAllProduct();
    ProductResDto getSingleProduct(String productId);


}
