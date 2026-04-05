package com.artichourey.ecommerce.productservice.dtos;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Product DTO for product management")
public class ProductDto {
	
	@Schema(description = "Product ID", example = "1")
	private Long id;

    @NotBlank(message="product name is required")
    @Schema(description = "Name of the product", example = "iPhone 14", required = true)
    private String name;

    @NotBlank(message="description is required")
    @Schema(description = "Product description", example = "Latest iPhone model with 128GB storage", required = true)
    private String description;

    @Positive(message="price must be positive")
    @Schema(description = "Product price", example = "799.99", required = true)
    private BigDecimal price;

    @PositiveOrZero(message="discount price cannot be negative")
    @Schema(description = "Discounted price if any", example = "749.99")
    private BigDecimal discountPrice;

    @Min(value=0, message="quantity must be 0 or more")
    @Schema(description = "Quantity available in stock", example = "50", required = true)
    private int quantity;
    
    @Schema(description = "Brand of the product", example = "Apple")
    private String brand;
    
    @Schema(description = "URL of the product image", example = "https://example.com/images/iphone14.jpg")
    private String imageUrl;
    
    @NotBlank(message = "SKU code is required")
    @Schema(description = "Unique SKU code for the product", example = "IP14-128GB-BLK", required = true)
    private String skuCode; 
   
    @NotNull(message="category Id is required")
    @Schema(description = "ID of the category this product belongs to", example = "1", required = true)
    private Long categoryId;
	

}
