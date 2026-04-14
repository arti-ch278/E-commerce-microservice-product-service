package com.artichourey.ecommerce.productservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
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
import org.springframework.test.util.ReflectionTestUtils;

import com.artichourey.ecommerce.productservice.dtos.ProductDto;
import com.artichourey.ecommerce.productservice.dtos.ProductResponseDto;
import com.artichourey.ecommerce.productservice.entity.Category;
import com.artichourey.ecommerce.productservice.entity.Product;
import com.artichourey.ecommerce.productservice.exception.ResourceNotFoundException;
import com.artichourey.ecommerce.productservice.mapper.ProductMapper;
import com.artichourey.ecommerce.productservice.repository.CategoryRepository;
import com.artichourey.ecommerce.productservice.repository.ProductRepository;
import com.artichourey.ecommerce.productservice.serviceImpl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    
    // CREATE PRODUCT
    
    @Test
    void createProduct_ShouldReturnSavedProduct() {

        ProductDto dto = ProductDto.builder()
                .name("Laptop")
                .description("Gaming Laptop")
                .price(BigDecimal.valueOf(1000))
                .discountPrice(BigDecimal.valueOf(900))
                .quantity(5)
                .skuCode("LAP123")
                .categoryId(1L)
                .build();

        Category category = new Category();
        category.setId(1L);

        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .skuCode("LAP123")
                .build();

        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setName("Laptop");
        responseDto.setSkuCode("LAP123");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productMapper.toEntity(dto, category))
                .thenReturn(product);

        when(productRepository.save(product))
                .thenReturn(product);

        when(productMapper.toDto(product))
                .thenReturn(responseDto);

        ProductResponseDto result = productService.createProduct(dto);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals("LAP123", result.getSkuCode());

        verify(categoryRepository).findById(1L);
        verify(productMapper).toEntity(dto, category);
        verify(productRepository).save(product);
        verify(productMapper).toDto(product);
    }

    @Test
    void createProduct_ShouldThrowException_WhenCategoryNotFound() {

        ProductDto dto = ProductDto.builder()
                .name("Laptop")
                .categoryId(10L)
                .build();

        when(categoryRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.createProduct(dto));

        verify(categoryRepository).findById(10L);
        verify(productRepository, never()).save(any());
    }

    
    // GET PRODUCT BY ID
    
    @Test
    void getProductById_ShouldReturnProduct() {

        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .build();

        ProductResponseDto dto = new ProductResponseDto();
        dto.setName("Laptop");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productMapper.toDto(product))
                .thenReturn(dto);

        ProductResponseDto result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());

        verify(productRepository).findById(1L);
        verify(productMapper).toDto(product);
    }

    @Test
    void getProductById_ShouldThrowException_WhenNotFound() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(1L));
    }

    
    // DELETE PRODUCT
    
    @Test
    void deleteProduct_ShouldDeleteSuccessfully() {

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setSkuCode("LAP123");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).delete(product);
    }

    @Test
    void deleteProduct_ShouldThrowException_WhenNotFound() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(1L));

        verify(productRepository).findById(1L);
        verify(productRepository, never()).delete(any());
    }

   
    // GET ALL PRODUCTS
    
    @Test
    void getAllProducts_ShouldReturnPage() {

        Product product = new Product();
        product.setId(1L);

        ProductResponseDto dto = new ProductResponseDto();
        dto.setName("Laptop");

        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        when(productMapper.toDto(product))
                .thenReturn(dto);

        Page<ProductResponseDto> result =
                productService.getAllProducts(0, 10, "id", "asc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(productRepository).findAll(any(Pageable.class));
        verify(productMapper).toDto(product);
    }

    
    // SEARCH / FILTER (basic)
    
    @Test
    void searchProduct_ShouldReturnResults() {

        Product product = new Product();
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.searchProducts(anyString(), any(Pageable.class)))
                .thenReturn(page);

        when(productMapper.toDto(product))
                .thenReturn(new ProductResponseDto());

        Page<ProductResponseDto> result =
                productService.searchProduct("lap", 0, 10);

        assertEquals(1, result.getTotalElements());

        verify(productRepository).searchProducts(anyString(), any(Pageable.class));
    }

   
    // UPLOAD IMAGE
 
    @Test
    void uploadImage_ShouldUploadSuccessfully() throws Exception {

        ReflectionTestUtils.setField(productService, "uploadDir", "uploads/test"); 

        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "image.jpg",
                        "image/jpeg",
                        "test image".getBytes()
                );

        when(productRepository.save(any()))
                .thenReturn(product);

        when(productMapper.toDto(any()))
                .thenReturn(new ProductResponseDto());

        ProductResponseDto result =
                productService.uploadImage(1L, file);

        assertNotNull(result);

        verify(productRepository).findById(1L);
        verify(productRepository).save(any());
    }
    
    @Test
    void uploadImage_ShouldThrowException_WhenFileEmpty() throws Exception {

        Product product = new Product();
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        MockMultipartFile file =
                new MockMultipartFile("file", "", "image/jpeg", new byte[0]);

        assertThrows(RuntimeException.class,
                () -> productService.uploadImage(1L, file));
    }
}
