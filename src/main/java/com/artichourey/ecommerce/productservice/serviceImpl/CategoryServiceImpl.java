package com.artichourey.ecommerce.productservice.serviceImpl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.artichourey.ecommerce.productservice.dtos.CategoryDto;
import com.artichourey.ecommerce.productservice.dtos.CategoryResponseDto;
import com.artichourey.ecommerce.productservice.entity.Category;
import com.artichourey.ecommerce.productservice.exception.ResourceNotFoundException;
import com.artichourey.ecommerce.productservice.mapper.CategoryMapper;
import com.artichourey.ecommerce.productservice.repository.CategoryRepository;
import com.artichourey.ecommerce.productservice.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto createDto(CategoryDto categoryDto) {

        log.info("Creating new category: name='{}', parentId={}",
                categoryDto.getName(), categoryDto.getParentId());

        // Validate parent category if provided
        if (categoryDto.getParentId() != null && categoryDto.getParentId() != 0) {
            categoryRepository.findById(categoryDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Parent category not found with id: " + categoryDto.getParentId()));
        }

        Category category = categoryMapper.toEntity(categoryDto);
        Category saved = categoryRepository.save(category);

        log.info("Category created successfully with ID={}", saved.getId());

        return categoryMapper.toDto(saved);
    }

    @Override
    public CategoryResponseDto updateDto(Long id, CategoryDto categoryDto) {

        log.info("Updating category with ID={}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + id));

        // Prevent self-parenting
        if (id.equals(categoryDto.getParentId())) {
            throw new IllegalArgumentException("Category cannot be parent of itself");
        }

        
        if (categoryDto.getParentId() != null && categoryDto.getParentId() != 0) {
            categoryRepository.findById(categoryDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Parent category not found with id: " + categoryDto.getParentId()));
        }

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setParentId(categoryDto.getParentId());

        log.info("Category updated successfully: ID={}", category.getId());

        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteCategoryById(Long id) {

        log.info("Deleting category with ID={}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + id));

        categoryRepository.delete(category);

        log.info("Category deleted successfully: ID={}", id);
    }

    @Override
    public CategoryResponseDto getCategory(Long id) {

        log.info("Fetching category with ID={}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + id));

        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {

        log.info("Fetching all categories");

        List<CategoryResponseDto> categories = categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();

        log.info("Total categories fetched: {}", categories.size());

        return categories;
    }
}