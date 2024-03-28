package com.artcart.services;

import com.artcart.response.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addNewCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(Integer cId, CategoryDto newCategoryDto);
    void deleteCategory(Integer cid);

    List<CategoryDto> getAllCategory();
    CategoryDto getSingleCategory(Integer cId);
}
