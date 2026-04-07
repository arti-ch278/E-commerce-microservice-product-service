package com.artichourey.ecommerce.productservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.artichourey.ecommerce.productservice.dtos.ProductDto;
import com.artichourey.ecommerce.productservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {
	
	 @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private ProductService productService;

	    @Autowired
	    private ObjectMapper objectMapper;
	    
	    @Test
	    void createProduct_ShouldReturnCreated() throws Exception {
	        ProductDto dto = ProductDto.builder()
	                .name("Laptop")
	                .description("Gaming")
	                .price(BigDecimal.valueOf(1000))
	                .quantity(5)
	                .categoryId(1L)
	                .skuCode("lap-123-00")
	                .build();

	        when(productService.createProduct(any()))
	                .thenReturn(dto);

	        mockMvc.perform(post("/api/products/")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(dto)))
	                .andExpect(status().isCreated());
	    }
	    
	    @Test
	    void getProduct_ShouldReturnOk() throws Exception {
	        when(productService.getProductById(1L))
	                .thenReturn(new ProductDto());

	        mockMvc.perform(get("/api/products/1"))
	                .andExpect(status().isOk());
	    }
	    
	    @Test
	    void updateProduct_ShouldReturnOk() throws Exception {
	        ProductDto dto = ProductDto.builder()
	                .name("Updated Laptop")
	                .description("Gaming")
	                .price(BigDecimal.valueOf(1200))
	                .quantity(10)
	                .categoryId(1L)
	                .skuCode("lap-123-00")
	                .build();

	        when(productService.updateProduct(eq(1L), any()))
	                .thenReturn(dto);

	        mockMvc.perform(put("/api/products/1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(dto)))
	                .andExpect(status().isOk());
	    }
	    @Test
	    void deleteProduct_ShouldReturnOk() throws Exception {

	        doNothing().when(productService).deleteProduct(1L);

	        mockMvc.perform(delete("/api/products/1"))
	                .andExpect(status().isOk());
	    }
	    @Test
	    void getAllProducts_ShouldReturnPage() throws Exception {

	        Page<ProductDto> page =
	                new PageImpl<>(List.of(new ProductDto()));

	        when(productService.getAllProducts(
	                anyInt(), anyInt(), anyString(), anyString()))
	                .thenReturn(page);

	        mockMvc.perform(get("/api/products/")
	                .param("page", "0")
	                .param("size", "10"))
	                .andExpect(status().isOk());
	    }
	    @Test
	    void searchProduct_ShouldReturnPage() throws Exception {

	        Page<ProductDto> page =
	                new PageImpl<>(List.of(new ProductDto()));

	        when(productService.searchProduct(
	                anyString(), anyInt(), anyInt()))
	                .thenReturn(page);

	        mockMvc.perform(get("/api/products/search")
	                .param("keyword", "laptop"))
	                .andExpect(status().isOk());
	    }
	    @Test
	    void filterProduct_ShouldReturnPage() throws Exception {

	        Page<ProductDto> page =
	                new PageImpl<>(List.of(new ProductDto()));

	        when(productService.filterProducts(
	                anyLong(),
	                anyDouble(),
	                anyDouble(),
	                anyInt(),
	                anyInt()
	        )).thenReturn(page);

	        mockMvc.perform(get("/api/products/filter")
	                .param("categoryId", "1")
	                .param("minPrice", "100")
	                .param("maxPrice", "2000"))
	                .andExpect(status().isOk());
	    }
	    
	    @Test
	    void advanceProduct_ShouldReturnPage() throws Exception {

	        Page<ProductDto> page =
	                new PageImpl<>(List.of(new ProductDto()));

	        when(productService.advanceFilter(
	                anyString(),
	                anyLong(),
	                anyDouble(),
	                anyDouble(),
	                anyInt(),
	                anyInt(),
	                anyString(),
	                anyString()
	        )).thenReturn(page);

	        mockMvc.perform(get("/api/products/advance")
	                .param("keyword", "laptop")
	                .param("minPrice", "0")
	                .param("maxPrice", "10000"))
	                .andExpect(status().isOk());
	    }
	    
	    @Test
	    void uploadImage_ShouldReturnOk() throws Exception {

	        MockMultipartFile file =
	                new MockMultipartFile(
	                        "file",
	                        "image.jpg",
	                        "image/jpeg",
	                        "test image".getBytes());

	        when(productService.uploadImage(eq(1L), any()))
	                .thenReturn(new ProductDto());

	        mockMvc.perform(multipart("/api/products/1/upload-image")
	                .file(file))
	                .andExpect(status().isOk());
	    }
	    

}
