package com.artcart.services;

import com.artcart.model.Customer;
import com.artcart.response.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addNewCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(String cId, CategoryDto newCategoryDto);
    void deleteCategory(String cid);

    List<CategoryDto> getAllCategory();
    CategoryDto getSingleCategory(String cId);

}
