package com.artcart.response;

import com.artcart.model.Seller;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductReqDto {
    private String name;
    private Integer price;
    private String description;
    private boolean stock;
    private List<String> productImages;
    private Integer category;
    private Seller seller;
}
