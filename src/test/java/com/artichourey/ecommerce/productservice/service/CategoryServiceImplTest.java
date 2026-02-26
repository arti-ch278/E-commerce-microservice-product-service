package com.artichourey.ecommerce.productservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.artichourey.ecommerce.productservice.dtos.CategoryDto;
import com.artichourey.ecommerce.productservice.entity.Category;
import com.artichourey.ecommerce.productservice.exception.ResourceNotFoundException;
import com.artichourey.ecommerce.productservice.mapper.CategoryMapper;
import com.artichourey.ecommerce.productservice.repository.CategoryRepository;
import com.artichourey.ecommerce.productservice.serviceImpl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
	@Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;
    
    @Test
    void createDto_ShouldReturnSavedCategory() {
        CategoryDto dto = CategoryDto.builder()
                .name("Electronics")
                .description("All electronic items")
                .build();

        Category category = Category.builder().id(1L).name("Electronics").build();

        when(categoryMapper.toEntity(dto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(dto);

        CategoryDto result = categoryService.createDto(dto);

        assertNotNull(result);
        verify(categoryRepository).save(category);
    }
    @Test
    void updateDto_ShouldUpdateCategory() {
        Category category = new Category();
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));
        when(categoryRepository.save(any()))
                .thenReturn(category);
        when(categoryMapper.toDto(any()))
                .thenReturn(new CategoryDto());

        CategoryDto result = categoryService.updateDto(1L, new CategoryDto());

        assertNotNull(result);
    }
    @Test
    void updateDto_ShouldThrowException_WhenNotFound() {
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.updateDto(1L, new CategoryDto()));
    }
    @Test
    void deleteCategory_ShouldDelete() {
        Category category = new Category();
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        categoryService.deleteCategoryById(1L);

        verify(categoryRepository).delete(category);
    }
    @Test
    void getAllCategories_ShouldReturnList() {
        when(categoryRepository.findAll())
                .thenReturn(List.of(new Category()));

        when(categoryMapper.toDto(any()))
                .thenReturn(new CategoryDto());

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
    }
}
