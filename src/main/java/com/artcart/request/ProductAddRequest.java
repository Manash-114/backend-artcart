package com.artcart.request;

import com.artcart.model.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAddRequest {
    private String name;
    private Integer price;
    private String description;
    private boolean stock;
    private List<String> productImages;
    private Integer category;
}
