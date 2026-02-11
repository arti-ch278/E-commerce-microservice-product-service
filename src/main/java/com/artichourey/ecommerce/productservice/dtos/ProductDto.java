package com.artichourey.ecommerce.productservice.dtos;

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
public class ProductDto {
	
	private Long id;

    @NotBlank(message="product name is required")
    private String name;

    @NotBlank(message="description is required")
    private String description;

    @Positive(message="price must be positive")
    private double price;

    @PositiveOrZero(message="discount price cannot be negative")
    private double discountPrice;

    @Min(value=0, message="quantity must be 0 or more")
    private int quantity;

    private String brand;
    private String imageUrl;

    @NotNull(message="category Id is required")
    private Long categoryId;
	

}
