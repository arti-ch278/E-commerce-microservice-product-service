package com.artichourey.ecommerce.productservice.mapper;

import org.springframework.stereotype.Component;

import com.artichourey.ecommerce.productservice.dtos.CategoryDto;
import com.artichourey.ecommerce.productservice.dtos.CategoryResponseDto;
import com.artichourey.ecommerce.productservice.entity.Category;
@Component
public class CategoryMapper {
	
	public CategoryResponseDto toDto(Category category) {
		if (category == null) return null;
		return CategoryResponseDto.builder().id(category.getId()).name(category.getName())
				.parentId(category.getParentId()).description(category.getDescription()).build();
		
		
	}
	public Category toEntity(CategoryDto dto) {
		 if (dto == null) return null;
		return Category.builder().name(dto.getName())
				.description(dto.getDescription()).parentId(dto.getParentId()).build();
		
	}
	

}
