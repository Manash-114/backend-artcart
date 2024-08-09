package com.artcart.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductReq {
    private String name;
    private Integer price;
    private String description;
    private boolean stock;
    private List<String> productImages;
}
