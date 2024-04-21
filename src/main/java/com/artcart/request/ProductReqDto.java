package com.artcart.request;

import com.artcart.model.Seller;
import lombok.*;

import java.util.List;


public class ProductReqDto {
    private String name;
    private Integer price;
    private String description;
    private boolean stock;
    private List<String> productImages;
    private Integer category;
    private Seller seller;

    public ProductReqDto() {
    }

    public ProductReqDto(String name, Integer price, String description, boolean stock, List<String> productImages, Integer category, Seller seller) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.productImages = productImages;
        this.category = category;
        this.seller = seller;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    public List<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<String> productImages) {
        this.productImages = productImages;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
