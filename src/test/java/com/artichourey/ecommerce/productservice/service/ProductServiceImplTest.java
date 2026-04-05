package com.artichourey.ecommerce.productservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import com.artichourey.ecommerce.productservice.dtos.ProductDto;
import com.artichourey.ecommerce.productservice.entity.Product;
import com.artichourey.ecommerce.productservice.exception.ResourceNotFoundException;
import com.artichourey.ecommerce.productservice.mapper.ProductMapper;
import com.artichourey.ecommerce.productservice.repository.ProductRepository;
import com.artichourey.ecommerce.productservice.serviceImpl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
	
	@Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;
   
    
    @Test
    void createProduct_ShouldReturnSavedProduct() {
        ProductDto dto = ProductDto.builder()
                .name("Laptop")
                .description("Gaming")
                .price(BigDecimal.valueOf(1000))
                .discountPrice(BigDecimal.valueOf(900))
                .quantity(5)
                .categoryId(1L)
                .build();

        Product product = Product.builder().id(1L).name("Laptop").build();

        when(productMapper.toEntity(any(), any())).thenReturn(product);
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.toDto(any())).thenReturn(dto);

        ProductDto result = productService.createProduct(dto);

        assertNotNull(result);
        verify(productRepository).save(any());
    }
    
    @Test
    void getProductById_ShouldReturnProduct() {
        Product product = Product.builder().id(1L).name("Laptop").build();

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        when(productMapper.toDto(product))
                .thenReturn(new ProductDto());

        ProductDto result = productService.getProductById(1L);

        assertNotNull(result);
    }
    
    @Test
    void getProductById_ShouldThrowException_WhenNotFound() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(1L));
    }
    
    @Test
    void deleteProduct_ShouldDeleteSuccessfully() {
        Product product = new Product();
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).delete(product);
    }
    
    @Test
    void getAllProducts_ShouldReturnPage() {
        Page<Product> page = new PageImpl<>(List.of(new Product()));
        when(productRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        when(productMapper.toDto(any()))
                .thenReturn(new ProductDto());

        Page<ProductDto> result =
                productService.getAllProducts(0,10,"id","asc");

        assertEquals(1, result.getTotalElements());
    }
    @Test
    void uploadImage_ShouldUploadSuccessfully() throws Exception {
        Product product = new Product();
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "image.jpg",
                        "image/jpeg",
                        "test image".getBytes());

        when(productRepository.save(any()))
                .thenReturn(product);

        when(productMapper.toDto(any()))
                .thenReturn(new ProductDto());

        ProductDto result = productService.uploadImage(1L, file);

        assertNotNull(result);
    }

}
