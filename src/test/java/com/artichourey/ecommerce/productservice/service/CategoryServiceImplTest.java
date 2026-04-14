package com.artichourey.ecommerce.productservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import com.artichourey.ecommerce.productservice.dtos.CategoryResponseDto;
import com.artichourey.ecommerce.productservice.entity.Category;
import com.artichourey.ecommerce.productservice.exception.ResourceNotFoundException;
import com.artichourey.ecommerce.productservice.mapper.CategoryMapper;
import com.artichourey.ecommerce.productservice.repository.CategoryRepository;
import com.artichourey.ecommerce.productservice.serviceImpl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    
    // CREATE
   
    @Test
    void createDto_ShouldReturnSavedCategory() {

        CategoryDto dto = CategoryDto.builder()
                .name("Electronics")
                .description("All electronic items")
                .parentId(null)
                .build();

        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("All electronic items")
                .build();

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setName("Electronics");

        when(categoryMapper.toEntity(dto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.createDto(dto);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());

        verify(categoryMapper).toEntity(dto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void createDto_ShouldThrowException_WhenParentNotFound() {

        CategoryDto dto = CategoryDto.builder()
                .name("Mobiles")
                .parentId(10L)
                .build();

        when(categoryRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.createDto(dto));

        verify(categoryRepository).findById(10L);
    }

    
    // UPDATE
   

    @Test
    void updateDto_ShouldUpdateSuccessfully() {

        Category existing = new Category();
        existing.setId(1L);
        existing.setName("Old Name");
        existing.setDescription("Old Desc");

        CategoryDto dto = CategoryDto.builder()
                .name("Updated Name")
                .description("Updated Desc")
                .parentId(10L)
                .build();

        Category parent = new Category();
        parent.setId(10L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(parent));

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setName("Updated Name");

        when(categoryMapper.toDto(existing)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.updateDto(1L, dto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Desc", existing.getDescription());
        assertEquals(10L, existing.getParentId());

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findById(10L);
    }

    @Test
    void updateDto_ShouldThrowException_WhenNotFound() {

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.updateDto(1L, new CategoryDto()));

        verify(categoryRepository).findById(1L);
    }

    @Test
    void updateDto_ShouldThrowException_WhenSelfParenting() {

        Category existing = new Category();
        existing.setId(1L);

        CategoryDto dto = CategoryDto.builder()
                .name("Test")
                .parentId(1L)
                .build();

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.updateDto(1L, dto));
    }

    
    // DELETE
 

    @Test
    void deleteCategory_ShouldDelete() {

        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        categoryService.deleteCategoryById(1L);

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteCategory_ShouldThrowException_WhenNotFound() {

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.deleteCategoryById(1L));

        verify(categoryRepository).findById(1L);
        verify(categoryRepository, never()).delete(any());
    }

    
    // GET ONE
   

    @Test
    void getCategory_ShouldReturnCategory() {

        Category category = new Category();
        category.setId(1L);

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setName("Category");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(categoryMapper.toDto(category))
                .thenReturn(responseDto);

        CategoryResponseDto result = categoryService.getCategory(1L);

        assertNotNull(result);
        assertEquals("Category", result.getName());

        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void getCategory_ShouldThrowException_WhenNotFound() {

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getCategory(1L));

        verify(categoryRepository).findById(1L);
    }

   
    // GET ALL
    

    @Test
    void getAllCategories_ShouldReturnList() {

        Category category = new Category();
        category.setId(1L);

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setName("Category");

        when(categoryRepository.findAll())
                .thenReturn(List.of(category));

        when(categoryMapper.toDto(category))
                .thenReturn(responseDto);

        List<CategoryResponseDto> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Category", result.get(0).getName());

        verify(categoryRepository).findAll();
    }
}