package com.artichourey.ecommerce.productservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Category DTO for product categories")
public class CategoryDto {
	@Schema(description = "Category ID", example = "1")
	private Long id;
	
	@NotBlank(message="category name can not be empty")
	@Schema(description = "Category name", example = "Electronics", required = true)
	private String name;
	
	@Schema(description = "Optional description of the category", example = "Devices and gadgets")
	private String description;
	
	@Schema(description = "Parent category ID if applicable", example = "0")
	private Long parentId;

}
