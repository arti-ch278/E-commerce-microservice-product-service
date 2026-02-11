package com.artichourey.ecommerce.productservice.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.artichourey.ecommerce.productservice.dtos.ProductDto;
import com.artichourey.ecommerce.productservice.service.ProductService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	
	@PostMapping("/")
	public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto){
		ProductDto dto=productService.createProduct(productDto);
	return new ResponseEntity<>(dto,HttpStatus.CREATED);  
		
	}
	@PutMapping("/{id}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto){
		
		return ResponseEntity.ok(productService.updateProduct(id, productDto)) ;
		
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id){
		productService.deleteProduct(id);
		
		return ResponseEntity.ok("product deleted successfully");
		
	}
	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){
		
	
	return ResponseEntity.ok(productService.getProductById(id));
		
	}
	
	@GetMapping("/")
	public ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size,
			                                               @RequestParam(defaultValue="id")  String sortBy,@RequestParam(defaultValue="asc") String sortDir){
		
		
		
     return ResponseEntity.ok(productService.getAllProducts(page, size, sortBy, sortDir));
		
	}
	@GetMapping("/search")
	public ResponseEntity<Page<ProductDto>> searchProduct(@RequestParam String keyword,@RequestParam(defaultValue="0")int page, @RequestParam(defaultValue="10")int size ){
		return ResponseEntity.ok(productService.searchProduct(keyword, page, size));
		
	}
	@GetMapping("/filter")
	public ResponseEntity<Page<ProductDto>> filterProduct(@RequestParam(required=false)Long categoryId,@RequestParam(required=false)Double minPrice,
			                                              @RequestParam(required=false)Double maxPrice,@RequestParam(required=false, defaultValue="0")int page,
			                                              @RequestParam( defaultValue="10") int size,@RequestParam( defaultValue="id") String sortBy,
			                                              @RequestParam( defaultValue="asc") String sortDir){
															
		return ResponseEntity.ok(productService.filterProducts(categoryId, minPrice, maxPrice, page, size));
		
	}
	@GetMapping("/advance")
	public ResponseEntity<Page<ProductDto>> advanceProduct(@RequestParam String keyword,
			@RequestParam(required=false)Long categoryId,
			@RequestParam(required=false)Double minPrice,
            @RequestParam(required=false)Double maxPrice,@RequestParam(required=false, defaultValue="0")int page,
            @RequestParam( defaultValue="10") int size,@RequestParam( defaultValue="id") String sortBy,
            @RequestParam( defaultValue="asc") String sortDir){
				
return ResponseEntity.ok(productService.advanceFilter(keyword, categoryId, minPrice, maxPrice, page, size, sortBy, sortDir));

}
@PostMapping("/{id}/upload-image")
public ResponseEntity<ProductDto> uploadImage(@PathVariable Long id ,@RequestParam("file") MultipartFile file) throws IOException{
	
	return ResponseEntity.ok(productService.uploadImage(id, file));
	
}
	

}
