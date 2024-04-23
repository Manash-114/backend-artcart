package com.artcart.controller;

import com.artcart.response.CategoryDto;
import com.artcart.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SELLER')")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    @Operation(summary = "to get all categories")
    public ResponseEntity<List<CategoryDto>> getAllCategory(){
        List<CategoryDto> res = categoryService.getAllCategory();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
