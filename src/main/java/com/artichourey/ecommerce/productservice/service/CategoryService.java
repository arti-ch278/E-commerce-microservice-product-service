package com.artichourey.ecommerce.productservice.service;

import java.util.List;

import com.artichourey.ecommerce.productservice.dtos.CategoryDto;

public interface CategoryService {

	
	CategoryDto createDto(CategoryDto categoryDto);
	CategoryDto updateDto(Long id, CategoryDto categoryDto);
	void deleteCategoryById(Long id);
	CategoryDto getCategory(Long id);
	List<CategoryDto> getAllCategories();
	
	
}
