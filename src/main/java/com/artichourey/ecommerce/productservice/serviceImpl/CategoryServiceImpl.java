package com.artichourey.ecommerce.productservice.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.artichourey.ecommerce.productservice.dtos.CategoryDto;
import com.artichourey.ecommerce.productservice.entity.Category;
import com.artichourey.ecommerce.productservice.exception.ResourceNotFoundException;
import com.artichourey.ecommerce.productservice.mapper.CategoryMapper;
import com.artichourey.ecommerce.productservice.repository.CategoryRepository;
import com.artichourey.ecommerce.productservice.service.CategoryService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final Logger log= LoggerFactory.getLogger(CategoryServiceImpl.class);
	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;
	
	@Override
	public CategoryDto createDto(CategoryDto categoryDto) {
		log.info("Creating new category: name='{}', parentId={}", categoryDto.getName(), categoryDto.getParentId());
		Category category=categoryMapper.toEntity(categoryDto);
		Category saved=categoryRepository.save(category);
		log.info("Category created successfully with ID={}", saved.getId());
		return categoryMapper.toDto(saved) ;
	}

	@Override
	public CategoryDto updateDto(Long id, CategoryDto categoryDto) {
		log.info("Updating category with ID={}", id);
		Category category=categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("catogory not found exception"+id));
		category.setName(categoryDto.getName());
		category.setDescription(categoryDto.getDescription());
		category.setParentId(categoryDto.getParentId());
		log.info("Category updated successfully: ID={}", category.getId());
		return categoryMapper.toDto(categoryRepository.save(category));
	}

	@Override
	public void deleteCategoryById(Long id) {
		 
		Category category=categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("category not found "+id));
		log.error("Category with ID={} not found for deletion", id);
        categoryRepository.delete(category);
        log.info("Category deleted successfully: ID={}", id);
	}
	
	@Override
	public CategoryDto getCategory(Long id) {
		log.info("Fetching category with ID={}", id);
		Category category=categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("category not found "+id));
		
		log.info("Category fetched successfully: ID={}", id);
		return categoryMapper.toDto(category);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		
		log.info("Fetching all categories");

        List<CategoryDto> categories = categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();

        log.info("Total categories fetched: {}", categories.size());
        return categories;
	}

}
