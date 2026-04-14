package com.artichourey.ecommerce.productservice.service;

import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.artichourey.ecommerce.productservice.dtos.ProductDto;
import com.artichourey.ecommerce.productservice.dtos.ProductResponseDto;

public interface ProductService {
	
   
	ProductResponseDto createProduct(ProductDto productDto); 
	
	ProductResponseDto updateProduct(Long id,ProductDto productDto);
    
    void deleteProduct(Long id);
    
    ProductResponseDto getProductById(Long id);
    
    ProductResponseDto uploadImage(Long productId,MultipartFile file) throws IOException;
    
    Page<ProductResponseDto> getAllProducts(int page, int size, String sortBy, String sortDir);
    
    Page<ProductResponseDto> searchProduct(String keyword, int page, int size);
    
    Page<ProductResponseDto> filterProducts(Long categoryId, double minPrice, double maxPrice, int page, int size);
    
    Page<ProductResponseDto> advanceFilter(String keyword,Long categoryId, double minPrice, double maxPrice, int page, int size, String sortBy,String sortDir);
    
    
    
    

}
