package com.artcart.controller;

import com.artcart.config.JwtTokenProvider;
import com.artcart.exception.CloudinaryImageUploadException;
import com.artcart.exception.UserNotFound;
import com.artcart.model.Seller;
import com.artcart.repository.SellerRepo;
import com.artcart.request.AccecptOrderReq;
import com.artcart.request.ProductAddRequest;
import com.artcart.request.ProductReqDto;
import com.artcart.response.ProductResDto;
import com.artcart.response.SellerDto;
import com.artcart.response.SellerOrderRes;
import com.artcart.services.CloudinaryImageUpload;
import com.artcart.services.OrderService;
import com.artcart.services.ProductService;
import com.artcart.services.SellerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seller")
@PreAuthorize("hasAuthority('ROLE_SELLER')")
public class SellerController {

    private SellerService sellerService;
    private JwtTokenProvider jwtTokenProvider;
    private SellerRepo sellerRepo;
    private CloudinaryImageUpload cloudinaryImageUpload;
    private ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(SellerController.class);

    private OrderService orderService;

    private ProductService productService;
    private ModelMapper modelMapper;
    public SellerController(SellerService sellerService, JwtTokenProvider jwtTokenProvider,
                            SellerRepo sellerRepo, CloudinaryImageUpload cloudinaryImageUpload,
                            ObjectMapper objectMapper, OrderService orderService, ProductService productService,ModelMapper modelMapper) {
        this.sellerService = sellerService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.sellerRepo = sellerRepo;
        this.cloudinaryImageUpload = cloudinaryImageUpload;
        this.objectMapper = objectMapper;
        this.orderService = orderService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }


    @GetMapping
    @Operation(summary = "to get seller details by providing jwt token ")
    public ResponseEntity<SellerDto> getSellerHandler(@RequestHeader("Authorization") String jwtToken) throws Exception{
        String emailFromToken = jwtTokenProvider.getEmailFromToken(jwtToken);
        SellerDto sellerByEmail = sellerService.getSellerByEmail(emailFromToken);
        return new ResponseEntity<>(sellerByEmail,HttpStatus.OK);
    }

    @PostMapping("/save")
    @Operation(summary = "to save his profile ")
    public ResponseEntity<SellerDto> createHandler(@RequestParam("aadhaarImage")MultipartFile aadhaarImage,
                                                   @RequestParam("profileImage") MultipartFile profileImage,
                                                   @RequestParam("data") String data,
                                                   @RequestHeader("Authorization") String jwtToken) throws Exception{

        //getting seller email from jwt token
        String emailFromToken = jwtTokenProvider.getEmailFromToken(jwtToken);
        //getting seller object from above email
        Seller byEmail = sellerRepo.findByEmail(emailFromToken);
        if(byEmail == null)
            throw new UserNotFound("user not found");
        try{
            Map<Object, Object> cloudAadhaarImage = cloudinaryImageUpload.imageUpload(aadhaarImage);
            Map<Object, Object> cloudProfileImage = cloudinaryImageUpload.imageUpload(profileImage);

            SellerDto sellerDto = objectMapper.readValue(data, SellerDto.class);
            sellerDto.setId(byEmail.getId());
            sellerDto.setEmail(byEmail.getEmail());
            sellerDto.setAadhaarImage(cloudAadhaarImage.get("url").toString());
            sellerDto.setProfileImage(cloudProfileImage.get("url").toString());
            SellerDto sellerDto1 = sellerService.create(sellerDto);
            return new ResponseEntity<>(sellerDto1, HttpStatus.CREATED);
        }catch (Exception e){
            throw new CloudinaryImageUploadException("file uploaded failed");
        }
    }

//    @PostMapping(value = "/add-product", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/add-product")
    @Operation(summary = "to add a new product")
    public ResponseEntity<?> addProductHandler(@RequestHeader("Authorization") String token ,  @RequestBody ProductAddRequest productAddRequest) throws Exception{
        String sellerEmail = jwtTokenProvider.getEmailFromToken(token);
        SellerDto sellerByEmail = sellerService.getSellerByEmail(sellerEmail);
        productService.addProduct(productAddRequest,sellerByEmail.getId());
        Map<String,String> map = new HashMap<>();
        map.put("message","Product add successfully");
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("/new-order")
    @Operation(summary = "to get all get order details")
    public ResponseEntity<?> getAllNewOrder(@RequestHeader("Authorization") String token) throws Exception{
        String email = jwtTokenProvider.getEmailFromToken(token);
        SellerDto seller = sellerService.getSellerByEmail(email);
        List<SellerOrderRes> allOrderOfSeller = orderService.getAllNewOrderOfSeller(seller.getId());
        return new ResponseEntity<>(allOrderOfSeller, HttpStatus.OK);

    }
    @GetMapping("/all-order")
    @Operation(summary = "to get all order details")
    public ResponseEntity<?> getAllOrder(@RequestHeader("Authorization") String token) throws Exception{
        String email = jwtTokenProvider.getEmailFromToken(token);
        SellerDto seller = sellerService.getSellerByEmail(email);
        List<SellerOrderRes> allOrderOfSeller = orderService.getAllOrderOfSeller(seller.getId());
        return new ResponseEntity<>(allOrderOfSeller, HttpStatus.OK);

    }

    @PostMapping("/accept-order")
    @Operation(summary = "to accept a order ")
    public ResponseEntity<?> acceptOrder(@RequestHeader("Authorization") String token, @RequestBody AccecptOrderReq accecptOrderReq) throws Exception{
        String email = jwtTokenProvider.getEmailFromToken(token);
        SellerDto seller = sellerService.getSellerByEmail(email);
        sellerService.acceptOrder(accecptOrderReq);
        return new ResponseEntity<>("done", HttpStatus.OK);

    }

    @GetMapping("/all-products")
    public ResponseEntity<List<ProductResDto>> getAllProductOfSeller(@RequestHeader("Authorization") String token) throws Exception {
        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        SellerDto sellerByEmail = sellerService.getSellerByEmail(emailFromToken);
        List<ProductResDto> allProductOfSeller = productService.getAllProductOfSeller(sellerByEmail.getId());
        return new ResponseEntity<>(allProductOfSeller,HttpStatus.OK);
    }
}
