package com.artichourey.ecommerce.productservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void createCategory_ShouldReturnCreated() throws Exception {

        CategoryDto dto = CategoryDto.builder()
                .name("Electronics")
                .description("All electronic items")
                .build();

        when(categoryService.createDto(any()))
                .thenReturn(dto);

        mockMvc.perform(post("/api/categories/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
    @Test
    void updateCategory_ShouldReturnOk() throws Exception {

        CategoryDto request = new CategoryDto();
        request.setName("Electronics");
        request.setDescription("Updated category");

        CategoryDto response = new CategoryDto();
        response.setId(1L);
        response.setName("Electronics");

        when(categoryService.updateDto(eq(1L), any(CategoryDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }
    
    @Test
    void deleteCategory_ShouldReturnOk() throws Exception {

        doNothing().when(categoryService).deleteCategoryById(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk());
    }
    @Test
    void getCategory_ShouldReturnOk() throws Exception {

        when(categoryService.getCategory(1L))
                .thenReturn(new CategoryDto());

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk());
    }
    
    @Test
    void getAllCategories_ShouldReturnList() throws Exception {

        when(categoryService.getAllCategories())
                .thenReturn(List.of(new CategoryDto()));

        mockMvc.perform(get("/api/categories/"))
                .andExpect(status().isOk());
    }
}
