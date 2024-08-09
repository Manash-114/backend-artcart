package com.artcart.services.impl;

import com.artcart.exception.ResourceNotFoundException;
import com.artcart.model.Customer;
import com.artcart.model.Product;
import com.artcart.model.ProductsBelongsToWishList;
import com.artcart.model.WishList;
import com.artcart.repository.CustomerRepo;
import com.artcart.repository.ProductRepo;
import com.artcart.repository.WishListRepo;
import com.artcart.response.ProductResDto;
import com.artcart.response.WishListResDto;
import com.artcart.services.WishListService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WishListServiceImpl implements WishListService {

    private WishListRepo wishListRepo;
    private ProductRepo productRepo;
    private CustomerRepo customerRepo;
    private ModelMapper modelMapper;

    public WishListServiceImpl(WishListRepo wishListRepo, ProductRepo productRepo, CustomerRepo customerRepo, ModelMapper modelMapper) {
        this.wishListRepo = wishListRepo;
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createWishList(String name , String customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("customer not found with id" + customerId));
        WishList wishList = new WishList();
        wishList.setId(UUID.randomUUID().toString());
        wishList.setName(name);
        wishList.setCreatedDate(LocalDateTime.now());
        wishList.setCustomer(customer);
        wishListRepo.save(wishList);
    }

    @Override
    public void addProductToWishList(List<String> productIds, String wishListId) {
        // Find the wish list
        WishList wishList = wishListRepo.findById(wishListId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id " + wishListId));

        // Create a list to hold the new join entities
        List<ProductsBelongsToWishList> newProductsBelongsToWishList = new ArrayList<>();

        // Iterate over each product ID
        for (String productId : productIds) {
            // Find the product
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

            // Check if the product is already in the wish list
            boolean alreadyAdded = wishList.getProducts().stream()
                    .anyMatch(p -> p.getProduct().equals(product));

            if (!alreadyAdded) {
                // Create a new ProductsBelongsToWishList instance
                ProductsBelongsToWishList pbw = new ProductsBelongsToWishList();
                pbw.setProduct(product);
                pbw.setWishList(wishList);
                newProductsBelongsToWishList.add(pbw);
            }
        }

        // Add new join entities to the wish list
        if (!newProductsBelongsToWishList.isEmpty()) {
            wishList.getProducts().addAll(newProductsBelongsToWishList);
        }

        // Save the updated wish list
        wishListRepo.save(wishList);
    }

    @Override
    public void removeProductFromWishList(String productId, String wishListId) {
        WishList wishList = wishListRepo.findById(wishListId).orElseThrow(() -> new ResourceNotFoundException("wishlist not found with id" + wishListId));
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found with id" + productId));
        wishList.getProducts().forEach((i)->System.out.println(i.getId()));
        if(wishList.getProducts().contains(product)){
            wishList.getProducts().remove(product);
        }
        wishListRepo.save(wishList);
    }

    @Override
    public void deleteWishList(String wishListId) {
            wishListRepo.deleteById(wishListId);
    }

    @Override
    public List<WishListResDto> getAllWishListOfCustomer(String customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("customer not found with id" + customerId));
        List<WishList> byCustomer = wishListRepo.findByCustomer(customer);

        List<WishListResDto> res = new ArrayList<>();
        byCustomer.stream().forEach((i)->{
            res.add(modelMapper.map(i, WishListResDto.class));
        });
        return res;
    }

    @Override
    public List<ProductResDto> getAllProductOfWishList(String wishListId) {

        WishList wishList = wishListRepo.findById(wishListId).orElseThrow(() -> new ResourceNotFoundException("wishlistnot found with id" + wishListId));
        List<ProductResDto> collect = wishList.getProducts().stream().map((i) -> modelMapper.map(i, ProductResDto.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<WishListResDto> getWishListByCustomer(Customer customer) {
        List<WishList> byCustomer = wishListRepo.findByCustomer(customer);
        List<WishListResDto> collect = byCustomer.stream().map((i) -> modelMapper.map(i, WishListResDto.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public WishListResDto getWishListById(String wishListId) {
        WishList wishList = wishListRepo.findById(wishListId).orElseThrow(() -> new ResourceNotFoundException("wishlist not found with id" + wishListId));
        return modelMapper.map(wishList, WishListResDto.class);

    }
}
