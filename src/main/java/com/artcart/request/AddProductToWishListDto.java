package com.artcart.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductToWishListDto {
    private List<String> productsId;
    private String wishListId;
}
