package com.artichourey.ecommerce.productservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

	private Long id;

	private String name;
	
	private String description;
	
	private Long parentId;


}
