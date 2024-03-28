package com.artcart.request;

import com.artcart.model.Seller;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProduct {
    private String name;
    private Integer price;
    private String description;
    private boolean stock;
    private List<String> productImages;
    private Integer category;
}
