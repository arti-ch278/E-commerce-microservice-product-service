package com.artichourey.ecommerce.productservice.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.artichourey.ecommerce.productservice.dtos.ProductDto;

public interface ProductService {
	
   
	ProductDto createProduct(ProductDto productDto); 
	
    ProductDto updateProduct(Long id,ProductDto productDto);
    
    void deleteProduct(Long id);
    
    ProductDto getProductById(Long id);
    
    ProductDto uploadImage(Long productId,MultipartFile file) throws IOException;
    
    Page<ProductDto> getAllProducts(int page, int size, String sortBy, String sortDir);
    
    Page<ProductDto> searchProduct(String keyword, int page, int size);
    
    Page<ProductDto> filterProducts(Long categoryId, double minPrice, double maxPrice, int page, int size);
    
    Page<ProductDto> advanceFilter(String keyword,Long categoryId, double minPrice, double maxPrice, int page, int size, String sortBy,String sortDir);
    
    
    
    

}
