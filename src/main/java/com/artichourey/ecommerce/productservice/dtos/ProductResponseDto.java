package com.artichourey.ecommerce.productservice.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {
	
	private Long id;

    private String name;

    private String description;

    private BigDecimal price;
    
    private BigDecimal discountPrice;

    private int quantity;
    
    private String brand;
    
    private String imageUrl;
    
    private String skuCode; 
   
    private Long categoryId;
	

}
