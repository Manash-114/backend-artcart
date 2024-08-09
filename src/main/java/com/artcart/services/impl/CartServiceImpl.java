package com.artcart.services.impl;

import com.artcart.exception.ResourceNotFoundException;
import com.artcart.model.Cart;
import com.artcart.model.Customer;
import com.artcart.model.Product;
import com.artcart.model.ProductBelongsToCart;
import com.artcart.repository.CartRepo;
import com.artcart.repository.CustomerRepo;
import com.artcart.repository.ProductRepo;
import com.artcart.request.ProductAddToCartReq;
import com.artcart.response.CartResDto;
import com.artcart.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {
    private CartRepo cartRepo;
    private CustomerRepo customerRepo;
    private ModelMapper modelMapper;
    private ProductRepo productRepo;

    public CartServiceImpl(CartRepo cartRepo, CustomerRepo customerRepo, ModelMapper modelMapper, ProductRepo productRepo) {

        this.cartRepo = cartRepo;
        this.customerRepo = customerRepo;
        this.modelMapper = modelMapper;
        this.productRepo = productRepo;
    }

    @Override
    public CartResDto crateCart(String customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("customer not found with id" + customerId));
        Cart byCustomer = cartRepo.findByCustomer(customer);
        if(byCustomer == null){
            Cart cart = new Cart();
            cart.setId(UUID.randomUUID().toString());
            cart.setCustomer(customer);
            Cart save = cartRepo.save(cart);
            CartResDto cartResDto = new CartResDto();
            cartResDto.setId(save.getId());
            cartResDto.setPrice(save.getPrice());
            cartResDto.setCustomerId(save.getCustomer().getId());
            return cartResDto;
        }else{
            CartResDto cartResDto = new CartResDto();
            cartResDto.setId(byCustomer.getId());
            cartResDto.setPrice(byCustomer.getPrice());
            cartResDto.setCustomerId(byCustomer.getCustomer().getId());
            return cartResDto;
        }



    }

    @Override
    public void deleteCart(String cartId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart not found with id" + cartId));
        cartRepo.delete(cart);
    }

    @Override
    public CartResDto getCustomerCart(String customerId) {

        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("customer not found with id" + customerId));
        Cart cart = cartRepo.findByCustomer(customer);
        return CartResDto.builder().id(cart.getId()).price(cart.getPrice()).customerId(cart.getCustomer().getId()).build();
    }

    @Override
    public CartResDto getCartById(String cartId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart not found with id" + cartId));
        return CartResDto.builder().id(cart.getId()).customerId(cart.getCustomer().getId()).price(cart.getPrice()).build();
    }

    @Override
    public CartResDto addProductToCart(String cartId, List<ProductAddToCartReq> productAddToCartReq) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart not found with id" + cartId));


        for(ProductAddToCartReq p : productAddToCartReq){
            Product product = productRepo.findById(p.getProductId()).orElseThrow(() -> new ResourceNotFoundException("product not found with id" + p.getProductId()));

            Set<ProductBelongsToCart> collect = cart.getProductBelongsToCart().stream().filter((i) -> i.getProduct().getId().compareTo(p.getProductId()) == 0).collect(Collectors.toSet());
            if(collect.size() == 0) {
                ProductBelongsToCart productBelongsToCart = new ProductBelongsToCart();
                productBelongsToCart.setId(UUID.randomUUID().toString());
                productBelongsToCart.setProduct(product);
                productBelongsToCart.setCart(cart);
                productBelongsToCart.setProductQuantity(p.getQuantity());
                cart.getProductBelongsToCart().add(productBelongsToCart);
                cart.setPrice(productBelongsToCart.getProduct().getPrice() * productBelongsToCart.getProductQuantity() );
            }else{
                collect.forEach((i)->{
                    i.setProductQuantity(p.getQuantity());
                });
                collect.addAll(cart.getProductBelongsToCart());
                cart.setProductBelongsToCart(collect);
            }
        }



        Integer p_total = 0;
        for(ProductBelongsToCart p : cart.getProductBelongsToCart()){
            p_total+= p.getProductQuantity() * p.getProduct().getPrice();
        }
        cart.setPrice(p_total);
        Cart save = cartRepo.save(cart);
        return CartResDto.builder().id(save.getId())
                .price(save.getPrice())
                .customerId(save.getCustomer().getId()).build();
    }
}
