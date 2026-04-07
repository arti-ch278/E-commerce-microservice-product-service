package com.artichourey.ecommerce.productservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.artichourey.ecommerce.productservice.dtos.CategoryDto;
import com.artichourey.ecommerce.productservice.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false) // disable security filters for unit test
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCategory_ShouldReturnCreated() throws Exception {
        // Arrange
        CategoryDto requestDto = CategoryDto.builder()
                .name("Electronics")
                .description("All electronic items")
                .build();

        CategoryDto responseDto = CategoryDto.builder()
                .id(1L)
                .name("Electronics")
                .description("All electronic items")
                .build();

        when(categoryService.createDto(any(CategoryDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/categories") // NO trailing slash
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.description").value("All electronic items"));
    }

    @Test
    void updateCategory_ShouldReturnOk() throws Exception {
        CategoryDto requestDto = new CategoryDto();
        requestDto.setName("Electronics");
        requestDto.setDescription("Updated category");

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(1L);
        responseDto.setName("Electronics");
        responseDto.setDescription("Updated category");

        when(categoryService.updateDto(eq(1L), any(CategoryDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.description").value("Updated category"));
    }

    @Test
    void deleteCategory_ShouldReturnOk() throws Exception {
        doNothing().when(categoryService).deleteCategoryById(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("category deleted succsessfully"));
    }

    @Test
    void getCategory_ShouldReturnOk() throws Exception {
        CategoryDto responseDto = CategoryDto.builder()
                .id(1L)
                .name("Electronics")
                .description("All electronic items")
                .build();

        when(categoryService.getCategory(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.description").value("All electronic items"));
    }

    @Test
    void getAllCategories_ShouldReturnList() throws Exception {
        CategoryDto category1 = CategoryDto.builder()
                .id(1L)
                .name("Electronics")
                .description("All electronic items")
                .build();

        CategoryDto category2 = CategoryDto.builder()
                .id(2L)
                .name("Books")
                .description("All kinds of books")
                .build();

        when(categoryService.getAllCategories()).thenReturn(List.of(category1, category2));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Books"));
    }
}