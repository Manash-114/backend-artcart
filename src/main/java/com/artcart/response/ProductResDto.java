package com.artcart.response;

import com.artcart.model.Category;
import com.artcart.model.ProductImage;
import com.artcart.model.Review;
import com.artcart.model.Seller;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResDto {
    private String id;
    private String name;
    private Integer price;
    private String description;
    private boolean stock;
    private List<ProductImage> productImages;
    private Category category;
    private Seller seller;
    private List<Review> reviews;
}
