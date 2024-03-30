package com.artcart.services;

import com.artcart.model.Customer;
import com.artcart.model.WishList;
import com.artcart.response.ProductResDto;
import com.artcart.response.WishListResDto;

import java.util.List;

public interface WishListService {
    void createWishList(String name , String customerId);
    void addProductToWishList(List<String> productId,String WishListId);
    void removeProductFromWishList(String productId,String wishListId);
    void deleteWishList(String wishListId);
    List<WishListResDto> getAllWishListOfCustomer(String customerId);
    List<ProductResDto> getAllProductOfWishList(String wishListId);
    List<WishListResDto> getWishListByCustomer(Customer customer);
    WishListResDto getWishListById(String wishListId);


}
