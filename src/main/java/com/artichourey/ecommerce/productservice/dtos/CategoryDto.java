package com.artichourey.ecommerce.productservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CategoryDto {
	private Long id;
	@NotBlank(message="category name can not be empty")
	private String name;
	private String description;
	private Long parentId;

}
