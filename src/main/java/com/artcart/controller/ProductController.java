package com.artcart.controller;

import com.artcart.config.JwtTokenProvider;
import com.artcart.model.Seller;
import com.artcart.request.ReviewReq;
import com.artcart.request.UpdateProduct;
import com.artcart.response.ProductReqDto;
import com.artcart.response.ProductResDto;
import com.artcart.response.SellerDto;
import com.artcart.services.ProductService;
import com.artcart.services.ReviewService;
import com.artcart.services.SellerService;
import com.cloudinary.http44.api.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;
    private SellerService sellerService;

    private JwtTokenProvider jwtTokenProvider;
    private ModelMapper modelMapper;

    private ReviewService reviewService;

    public ProductController(ProductService productService, SellerService sellerService, JwtTokenProvider jwtTokenProvider, ModelMapper modelMapper, ReviewService reviewService) {
        this.productService = productService;
        this.sellerService = sellerService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.modelMapper = modelMapper;
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @PostMapping()
    public ResponseEntity<?> addProductHandler(@RequestHeader("Authorization") String token , @RequestBody ProductReqDto productReqDto) throws Exception{
        String sellerEmail = jwtTokenProvider.getEmailFromToken(token);
        System.out.println(productReqDto);
        SellerDto sellerByEmail = sellerService.getSellerByEmail(sellerEmail);
        productReqDto.setSeller(modelMapper.map(sellerByEmail, Seller.class));
        productService.addProduct(productReqDto);
        Map<String,String> map = new HashMap<>();
        map.put("message","Product add successfully");
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        List<ProductResDto> allProduct = productService.getAllProduct();
        Map<String , Object> map = new HashMap<>();
        map.put("data",allProduct);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody UpdateProduct updateProduct){

        ProductResDto productResDto = productService.updateProduct(id, updateProduct);

        Map<String , Object> map = new HashMap<>();
        map.put("data",productResDto);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResDto> getSingle(@PathVariable String id){
        ProductResDto singleProduct = productService.getSingleProduct(id);
        return new ResponseEntity<>(singleProduct,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public ResponseEntity<?> deleteProduct(@PathVariable String id){
        productService.deleteProduct(id);
        Map<String,String> res = new HashMap<>();
        res.put("message","Product deleted successfully");
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @PostMapping("/review/{id}")
    public ResponseEntity<?> giveReview(@PathVariable String id , @RequestBody ReviewReq reviewReq){
        reviewService.addReview(id,reviewReq);
        Map<String,String> res = new HashMap<>();
        res.put("message","Review added successfully");
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> giveReview(@PathVariable String id){
        reviewService.deleteReview(id);
        Map<String,String> res = new HashMap<>();
        res.put("message","Review deleted successfully");
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
}
