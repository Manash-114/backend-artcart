package com.artcart.services.impl;

import com.artcart.exception.ResourceNotFoundException;
import com.artcart.model.Category;
import com.artcart.model.Product;
import com.artcart.model.ProductImage;
import com.artcart.model.Seller;
import com.artcart.repository.ProductImageRepo;
import com.artcart.repository.ProductRepo;
import com.artcart.repository.SellerRepo;
import com.artcart.request.ProductAddRequest;
import com.artcart.request.UpdateProductReq;
import com.artcart.response.CategoryDto;
import com.artcart.response.ProductResDto;
import com.artcart.services.CategoryService;
import com.artcart.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ModelMapper modelMapper;
    private ProductRepo productRepo;
    private ProductImageRepo productImageRepo;
    private CategoryService categoryService;
    private SellerRepo sellerRepo;

    public ProductServiceImpl(ModelMapper modelMapper, ProductRepo productRepo, ProductImageRepo productImageRepo, CategoryService categoryService, SellerRepo sellerRepo) {
        this.modelMapper = modelMapper;
        this.productRepo = productRepo;
        this.productImageRepo = productImageRepo;
        this.categoryService = categoryService;
        this.sellerRepo = sellerRepo;
    }

    @Override
    public void addProduct(ProductAddRequest productReqDto , Integer sellerId) {
        //find category
        CategoryDto singleCategory = categoryService.getSingleCategory(productReqDto.getCategory());
       //find Seller
        Seller seller = sellerRepo.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("seller not found with id " + sellerId));


        Product product = new Product();
        String pId = UUID.randomUUID().toString();
        product.setId(pId);
        product.setCategory(modelMapper.map(singleCategory, Category.class));
        List<ProductImage> collect = productReqDto.getProductImages().stream().map((item -> new ProductImage(UUID.randomUUID().toString(), item , product ))).collect(Collectors.toList());
        product.setProductImages(collect);
        product.setName(productReqDto.getName());
        product.setPrice(productReqDto.getPrice());
        product.setSeller(seller);
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
    public ProductResDto updateProduct(String productId, UpdateProductReq productReqDto) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found with id " + productId));
        for(int i = 0 ;i<product.getProductImages().size();i++){
            product.getProductImages().get(i).setName(productReqDto.getProductImages().get(i));
        }
        product.setName(productReqDto.getName());
        product.setPrice(productReqDto.getPrice());
        product.setStock(productReqDto.isStock());
        product.setDescription(productReqDto.getDescription());
        Product save = productRepo.save(product);
        return modelMapper.map(save , ProductResDto.class);
    }

    @Override
    public List<ProductResDto> getAllProduct() {
        List<Product> all = productRepo.findAll();
        List<ProductResDto> collect1 = new ArrayList<>();

        all.forEach((p)->{
            if(p.isStock()){
                ProductResDto map = modelMapper.map(p, ProductResDto.class);
                collect1.add(map);
            }
        });

//        List<ProductResDto> collect = all.stream().map((p) -> modelMapper.map(p, ProductResDto.class)).collect(Collectors.toList());
        return collect1;
    }

    @Override
    public ProductResDto getSingleProduct(String productId) {

        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found with id " + productId));
        return modelMapper.map(product, ProductResDto.class);
    }

    @Override
    public List<ProductResDto> getAllProductOfSeller(Integer sellerId) {
        Seller seller = sellerRepo.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("Seller not found with id " + sellerId));
        List<Product> bySeller = productRepo.findBySeller(seller);
        List<ProductResDto> collect = bySeller.stream().map((product -> modelMapper.map(product, ProductResDto.class))).collect(Collectors.toList());
        return collect;
    }
}
