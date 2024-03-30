package com.artcart.controller;

import com.artcart.config.JwtTokenProvider;
import com.artcart.model.Customer;
import com.artcart.request.AddProductToWishListDto;
import com.artcart.request.WishListReq;
import com.artcart.response.ProductResDto;
import com.artcart.response.WishListResDto;
import com.artcart.services.CustomerService;
import com.artcart.services.WishListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
public class WishListController {

    private WishListService wishListService;
    private JwtTokenProvider jwtTokenProvider;

    private CustomerService customerService;

    public WishListController(WishListService wishListService, JwtTokenProvider jwtTokenProvider, CustomerService customerService) {
        this.wishListService = wishListService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerService = customerService;
    }

    @PostMapping("/wishlist")
    public ResponseEntity<?> createWishListHandler(@RequestHeader("Authorization") String token, @RequestBody WishListReq wishListReq) throws Exception {
        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        Customer customerByEmail = customerService.getCustomerByEmail(emailFromToken);
        wishListService.createWishList(wishListReq.getName(),customerByEmail.getId());
        Map<String,String> res = new HashMap<>();
        res.put("message","Wishlist added successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @GetMapping("/wishlist")
    public ResponseEntity<?> getAllWishList(@RequestHeader("Authorization") String token, @RequestBody WishListReq wishListReq) throws Exception {
        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        Customer customerByEmail = customerService.getCustomerByEmail(emailFromToken);
        List<WishListResDto> resData = wishListService.getAllWishListOfCustomer(customerByEmail.getId());
        Map<String,Object   > res = new HashMap<>();
        res.put("data",resData);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/wishlist/add-product")
    public ResponseEntity<?> addProductToWishListHandler(@RequestHeader("Authorization") String token, @RequestBody AddProductToWishListDto addProductToWishListDto) throws Exception {
        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        Customer customerByEmail = customerService.getCustomerByEmail(emailFromToken);
        WishListResDto wishListById = wishListService.getWishListById(addProductToWishListDto.getWishListId());
        wishListService.addProductToWishList(addProductToWishListDto.getProductsId(),wishListById.getId());
        Map<String,String> res = new HashMap<>();
        res.put("message","Products  added to wishlist successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/wishlist/remove-product/{wishListId}/{productId}")
    public ResponseEntity<?> removeProductToWishListHandler(@PathVariable String wishListId, @PathVariable String productId) throws Exception {
        wishListService.removeProductFromWishList(productId,wishListId);
        Map<String,String> res = new HashMap<>();
        res.put("message","Product deleted from wishlist successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/wishlist/{wishListId}")
    public ResponseEntity<?> deleteWishlist(@PathVariable String wishListId) throws Exception {
        wishListService.deleteWishList(wishListId);
        Map<String,String> res = new HashMap<>();
        res.put("message","wishlist deleted successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/wishlist/{wishListId}")
    public ResponseEntity<?> getAllProductOfWishList(@PathVariable String wishListId) throws Exception {
        List<ProductResDto> allProductOfWishList = wishListService.getAllProductOfWishList(wishListId);

        Map<String,Object> res = new HashMap<>();
        res.put("data",allProductOfWishList);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
