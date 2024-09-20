package com.artcart.response;

import com.artcart.model.Category;
import com.artcart.model.ProductImage;
import com.artcart.model.Review;
import com.artcart.model.Seller;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResDto {
    private String id;
    private String name;
    private Integer price;
    private String description;
    private Boolean stock;
    private List<ProductImage> productImages;
    private Category category;
    private Seller seller;
    @JsonIgnore
    private List<Review> reviews;

    public ProductResDto(String id, String name, Integer price, String description, Boolean stock, List<ProductImage> productImages, Category category, Seller seller) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.productImages = productImages;
        this.category = category;
        this.seller = seller;
    }
}
