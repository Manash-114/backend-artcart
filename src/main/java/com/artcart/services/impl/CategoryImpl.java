package com.artcart.services.impl;

import com.artcart.exception.ResourceNotFoundException;
import com.artcart.model.Category;
import com.artcart.repository.CategoryRepo;
import com.artcart.response.CategoryDto;
import com.artcart.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryImpl implements CategoryService {

    private CategoryRepo categoryRepo;
    private ModelMapper modelMapper;

    public CategoryImpl(CategoryRepo categoryRepo, ModelMapper modelMapper){
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }
    @Override
    public CategoryDto addNewCategory(CategoryDto categoryDto) {
        Category ca = modelMapper.map(categoryDto, Category.class);
        System.out.println(ca);
        Category save = categoryRepo.save(ca);
        return modelMapper.map(save,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(Integer cId, CategoryDto newCategoryDto) {
        Category category = categoryRepo.findById(cId).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + cId));
        category.setName(newCategoryDto.getName());
        return modelMapper.map(categoryRepo.save(category),CategoryDto.class);
    }

    @Override
    public void deleteCategory(Integer cId) {
        Category category = categoryRepo.findById(cId).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + cId));
        categoryRepo.delete(category);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> all = categoryRepo.findAll();
        List<CategoryDto> res = all.stream().map((item)-> modelMapper.map(item,CategoryDto.class)).toList();
        return  res;
    }
    @Override
    public CategoryDto getSingleCategory(Integer cId){
        return  modelMapper.map(categoryRepo.findById(cId).orElseThrow(()->new ResourceNotFoundException("category not found with id "+cId)),CategoryDto.class);
    }


}
