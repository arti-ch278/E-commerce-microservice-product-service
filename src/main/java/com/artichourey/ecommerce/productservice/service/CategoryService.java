package com.artichourey.ecommerce.productservice.service;

import java.util.List;

import com.artichourey.ecommerce.productservice.dtos.CategoryDto;
import com.artichourey.ecommerce.productservice.dtos.CategoryResponseDto;

public interface CategoryService {

	
	CategoryResponseDto createDto(CategoryDto categoryDto);
	CategoryResponseDto updateDto(Long id, CategoryDto categoryDto);
	void deleteCategoryById(Long id);
	CategoryResponseDto getCategory(Long id);
	List<CategoryResponseDto> getAllCategories();
	
	
}
