package com.artcart.controller;

import com.artcart.config.JwtTokenProvider;
import com.artcart.model.Customer;
import com.artcart.repository.CustomerRepo;
import com.artcart.request.ReviewReq;
import com.artcart.request.UpdateProduct;
import com.artcart.response.ProductResDto;
import com.artcart.services.ProductService;
import com.artcart.services.ReviewService;
import com.artcart.services.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/product")
public class ProductController {

    private ProductService productService;
    private SellerService sellerService;

    private JwtTokenProvider jwtTokenProvider;
    private ModelMapper modelMapper;

    private ReviewService reviewService;
    private CustomerRepo customerRepo;

    public ProductController(ProductService productService, SellerService sellerService, JwtTokenProvider jwtTokenProvider, ModelMapper modelMapper, ReviewService reviewService, CustomerRepo customerRepo) {
        this.productService = productService;
        this.sellerService = sellerService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.modelMapper = modelMapper;
        this.reviewService = reviewService;
        this.customerRepo = customerRepo;
    }



    @GetMapping
    @Operation(summary = "to get all product")
    public ResponseEntity<?> getAll(){
        List<ProductResDto> allProduct = productService.getAllProduct();
        Map<String , Object> map = new HashMap<>();
        map.put("data",allProduct);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "to update the product details")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody UpdateProduct updateProduct){

        ProductResDto productResDto = productService.updateProduct(id, updateProduct);

        Map<String , Object> map = new HashMap<>();
        map.put("data",productResDto);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @Operation(summary = "to get single product")
    public ResponseEntity<ProductResDto> getSingle(@PathVariable String id){
        ProductResDto singleProduct = productService.getSingleProduct(id);
        return new ResponseEntity<>(singleProduct,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "to delete a single product")
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public ResponseEntity<?> deleteProduct(@PathVariable String id){
        productService.deleteProduct(id);
        Map<String,String> res = new HashMap<>();
        res.put("message","Product deleted successfully");
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @PostMapping("/review/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @Operation(summary = "to give review of particular product")
    public ResponseEntity<?> giveReview(@PathVariable String id , @RequestHeader("Authorization") String token , @RequestBody ReviewReq reviewReq) throws Exception {
        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        Customer byEmail = customerRepo.findByEmail(emailFromToken);
        reviewService.addReview(id,byEmail,reviewReq);
        Map<String,String> res = new HashMap<>();
        res.put("message","Review added successfully");
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @DeleteMapping("/review/{id}")
    @Operation(summary = "to delete a review")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<?> giveReview(@PathVariable String id){
        reviewService.deleteReview(id);
        Map<String,String> res = new HashMap<>();
        res.put("message","Review deleted successfully");
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
}
