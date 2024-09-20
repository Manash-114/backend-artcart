package com.artcart.mapper;

import com.artcart.model.Product;
import com.artcart.response.ProductResDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResDto toProductResDto(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductResDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.isStock(),
                product.getProductImages(),
                product.getCategory(),
                product.getSeller()
//                product.getReviews()
        );
    }

    public Product toProduct(ProductResDto productResDto) {
        if (productResDto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productResDto.getId());
        product.setName(productResDto.getName());
        product.setPrice(productResDto.getPrice());
        product.setDescription(productResDto.getDescription());
        product.setStock(productResDto.getStock());
        product.setProductImages(productResDto.getProductImages());
        product.setCategory(productResDto.getCategory());
        product.setSeller(productResDto.getSeller());
        product.setReviews(productResDto.getReviews());

        return product;
    }
}