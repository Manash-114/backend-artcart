package com.artcart.services.impl;

import com.artcart.exception.ResourceNotFoundException;
import com.artcart.model.Category;
import com.artcart.model.Product;
import com.artcart.model.ProductImage;
import com.artcart.repository.ProductImageRepo;
import com.artcart.repository.ProductRepo;
import com.artcart.request.UpdateProduct;
import com.artcart.response.CategoryDto;
import com.artcart.response.ProductReqDto;
import com.artcart.response.ProductResDto;
import com.artcart.services.CategoryService;
import com.artcart.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ModelMapper modelMapper;
    private ProductRepo productRepo;
    private ProductImageRepo productImageRepo;
    private CategoryService categoryService;

    public ProductServiceImpl(ModelMapper modelMapper, ProductRepo productRepo, ProductImageRepo productImageRepo, CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.productRepo = productRepo;
        this.productImageRepo = productImageRepo;
        this.categoryService = categoryService;
    }

    @Override
    public void addProduct(ProductReqDto productReqDto) {
        //find category
        CategoryDto singleCategory = categoryService.getSingleCategory(productReqDto.getCategory());

        String pId = UUID.randomUUID().toString();

        Product product = new Product();
        product.setId(pId);
        product.setCategory(modelMapper.map(singleCategory, Category.class));
//        product.setProductImages(productDto.getProductImages());

        List<ProductImage> collect = productReqDto.getProductImages().stream().map((item -> new ProductImage(UUID.randomUUID().toString(), item, List.of(product)))).collect(Collectors.toList());
        product.setProductImages(collect);

        product.setName(productReqDto.getName());
        product.setPrice(productReqDto.getPrice());
        product.setSeller(productReqDto.getSeller());
        product.setStock(true);
        product.setDescription(productReqDto.getDescription());
        Product save = productRepo.save(product);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found with id " + productId));
        productRepo.delete(product);
    }

    @Override
    public ProductResDto updateProduct(String productId, UpdateProduct productReqDto) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found with id " + productId));

        CategoryDto singleCategory = categoryService.getSingleCategory(productReqDto.getCategory());
        product.setCategory(modelMapper.map(singleCategory, Category.class));
        for(int i = 0 ;i<product.getProductImages().size();i++){
            product.getProductImages().get(i).setName(productReqDto.getProductImages().get(i));
        }
        product.setName(productReqDto.getName());
        product.setPrice(productReqDto.getPrice());
        product.setStock(true);
        product.setDescription(productReqDto.getDescription());
        Product save = productRepo.save(product);
        return modelMapper.map(save , ProductResDto.class);
    }

    @Override
    public List<ProductResDto> getAllProduct() {
        List<Product> all = productRepo.findAll();
        List<ProductResDto> collect = all.stream().map((p) -> modelMapper.map(p, ProductResDto.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public ProductResDto getSingleProduct(String productId) {

        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found with id " + productId));
        return modelMapper.map(product, ProductResDto.class);
    }
}
