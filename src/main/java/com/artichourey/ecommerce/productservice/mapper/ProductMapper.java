package com.artichourey.ecommerce.productservice.mapper;

import org.springframework.stereotype.Component;

import com.artichourey.ecommerce.productservice.dtos.ProductDto;
import com.artichourey.ecommerce.productservice.entity.Category;
import com.artichourey.ecommerce.productservice.entity.Product;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .quantity(product.getQuantity())
                .skuCode(product.getSkuCode())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .build();
    }

    public Product toEntity(ProductDto dto, Category category) {
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .brand(dto.getBrand())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .discountPrice(dto.getDiscountPrice())
                .imageUrl(dto.getImageUrl())
                .quantity(dto.getQuantity())
                .skuCode(dto.getSkuCode()) 
                .category(category)
                .build();
    }
}