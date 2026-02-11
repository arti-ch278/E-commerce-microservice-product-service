package com.artichourey.ecommerce.productservice.mapper;

import org.springframework.stereotype.Component;

import com.artichourey.ecommerce.productservice.dtos.CategoryDto;
import com.artichourey.ecommerce.productservice.entity.Category;
@Component
public class CategoryMapper {
	
	public CategoryDto toDto(Category category) {
		
		
		
		return CategoryDto.builder().id(category.getId()).name(category.getName())
				.parentId(category.getParentId()).description(category.getDescription()).build();
		
		
	}
	public Category toEntity(CategoryDto dto) {
		return Category.builder().id(dto.getId()).name(dto.getName())
				.description(dto.getDescription()).parentId(dto.getParentId()).build();
		
	}
	

}
