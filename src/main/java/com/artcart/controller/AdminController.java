package com.artcart.controller;


import com.artcart.config.JwtTokenProvider;
import com.artcart.exception.UserNotFound;
import com.artcart.model.Seller;
import com.artcart.repository.SellerRepo;
import com.artcart.response.AdminDetailsResDto;
import com.artcart.response.CategoryDto;
import com.artcart.response.SellerDto;
import com.artcart.services.AdminServices;
import com.artcart.services.CategoryService;
import com.artcart.services.SellerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin")
public class AdminController {
    private AdminServices adminServices;
    private SellerRepo sellerRepo;
    private SellerService sellerService;
    private ModelMapper modelMapper;
    private JwtTokenProvider jwtTokenProvider;
    private CategoryService categoryService;

    public AdminController(AdminServices adminServices, SellerRepo sellerRepo, SellerService sellerService, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider, CategoryService categoryService) {
        this.adminServices = adminServices;
        this.sellerRepo = sellerRepo;
        this.sellerService = sellerService;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.categoryService = categoryService;
    }

    @GetMapping("/profile")
    public ResponseEntity<AdminDetailsResDto> getAdminDetails(@RequestHeader("Authorization") String token) throws Exception {
        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        AdminDetailsResDto adminDetais = adminServices.getAdminDetais(emailFromToken);
        return new ResponseEntity<>(adminDetais,HttpStatus.OK);

    }

    @GetMapping("/all-unapproved-seller")
    @Operation(summary = "Get All Unapproved seller details",
    responses = {
            @ApiResponse(
                    description = "Success",
                    responseCode = "200"
            ),
            @ApiResponse(
                    description = "Unauthorized / Invalid token",
                    responseCode = "403"
            )
    })
    public ResponseEntity<List<SellerDto>> handlerForUnapprovedSeller(){
        List<SellerDto> allUnApprovedSeller = adminServices.getAllUnApprovedSeller();
        return new ResponseEntity<>(allUnApprovedSeller, HttpStatus.OK);
    }

    @PutMapping("/approve-seller/{sellerId}/{approvedStatus}")
    @Operation(summary = "To approve seller")
    public ResponseEntity<Map<String, String>> handlerForApprovedSingleSeller(@PathVariable String sellerId,@PathVariable Integer approvedStatus){
        Seller seller = sellerRepo.findById(sellerId).orElseThrow(() -> new UserNotFound("seller not found with id " + sellerId));
        if(approvedStatus==1)
            seller.setApproved(true);
        else
            seller.setApproved(false);
        sellerRepo.save(seller);
        Map<String,String> res = new HashMap<>();
        res.put("message","successfully approved");
        res.put("status",HttpStatus.OK.toString());
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping("/seller-details")
    @Operation(summary = "Get all seller details")
    public ResponseEntity<List<SellerDto>> getAllSellerDetails(){
        List<Seller> all = sellerRepo.findAll();
        List<SellerDto> res =  new ArrayList<>();
        all.stream().map(item->res.add(modelMapper.map(item, SellerDto.class))
        ).collect(Collectors.toList());
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @PostMapping("/category")
    @Operation(summary = "to add new category")
    public ResponseEntity<?> addNewCategoryHandler(@RequestBody CategoryDto categoryDto){
        System.out.println(categoryDto);
        CategoryDto categoryDto1 = categoryService.addNewCategory(categoryDto);
        Map<String,Object> res =new HashMap<>();
        res.put("message","Category Add Successfully");
        res.put("data",categoryDto1);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping("/category")
    @Operation(summary = "to get all categories")
    public ResponseEntity<List<CategoryDto>> getAllCategory(){
        List<CategoryDto> res = categoryService.getAllCategory();
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "to get single category details")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable Integer id){
        CategoryDto res = categoryService.getSingleCategory(id);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }


    @DeleteMapping("/category/{id}")
    @Operation(summary = "to delete a category")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id){
        categoryService.deleteCategory(id);
        Map<String,String> res = new HashMap<>();
        res.put("message","Deleted successfully");
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @PutMapping("/category/{id}")
    @Operation(summary = "to update a category")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id , @RequestBody CategoryDto newCategory){
        CategoryDto categoryDto = categoryService.updateCategory(id, newCategory);
        Map<String,Object> res = new HashMap<>();
        res.put("message","updated successfully");
        res.put("data",categoryDto);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }



}
