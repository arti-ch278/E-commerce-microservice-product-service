package com.artichourey.ecommerce.productservice.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.artichourey.ecommerce.productservice.dtos.ProductResponseDto;
import com.artichourey.ecommerce.productservice.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
@Tag(name = "Product APIs", description = "Endpoints for managing products")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
	
	private final ProductService productService;
	 @Operation(
	            summary = "Create a new product",
	            description = "Creates a new product with provided details"
	    )
	    @ApiResponses({
	            @ApiResponse(responseCode = "201", description = "Product created successfully",
	                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
	            @ApiResponse(responseCode = "400", description = "Invalid input"),
	            @ApiResponse(responseCode = "401", description = "Unauthorized")
	    })
	@PostMapping
	public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductDto productDto){
		ProductResponseDto dto=productService.createProduct(productDto);
	return new ResponseEntity<>(dto,HttpStatus.CREATED);  
		
	}
	
	
	@Operation(summary = "Update a product by ID")
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto){
		
		return ResponseEntity.ok(productService.updateProduct(id, productDto)) ;
		
	}
	@Operation(summary = "Delete a product by ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
		productService.deleteProduct(id);
		
		return ResponseEntity.noContent().build();
		
	}
	@Operation(summary = "Get a product by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id){
		
	return ResponseEntity.ok(productService.getProductById(id));
		
	}
	@Operation(summary = "Get all products with pagination",  
			description = "Returns a paginated list of products. Supports page, size, sortBy, sortDir query parameters.")
	@GetMapping
	public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@Parameter(description = "Page number (default 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (default 10)") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by (default 'id')") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction: asc or desc (default 'asc')") @RequestParam(defaultValue = "asc") String sortDir){
		
		
		
     return ResponseEntity.ok(productService.getAllProducts(page, size, sortBy, sortDir));
		
	}
	@Operation(summary = "Search products by keyword")
	@GetMapping("/search")
	public ResponseEntity<Page<ProductResponseDto>> searchProduct(@RequestParam String keyword,
			@Parameter(description = "Page number (default 0)") @RequestParam(defaultValue="0")int page,  
	        @Parameter(description = "Number of items per page (default 10)")@RequestParam(defaultValue="10")int size ){
		return ResponseEntity.ok(productService.searchProduct(keyword, page, size));
		
	}
	@Operation(summary = "Filter products by category and price")
	@GetMapping("/filter")
	public ResponseEntity<Page<ProductResponseDto>> filterProduct(@RequestParam(required=false)Long categoryId,@RequestParam(required=false)Double minPrice,
			                                              @RequestParam(required=false)Double maxPrice,@RequestParam(required=false, defaultValue="0")int page,
			                                              @RequestParam( defaultValue="10") int size,@RequestParam( defaultValue="id") String sortBy,
			                                              @RequestParam( defaultValue="asc") String sortDir){
															
		return ResponseEntity.ok(productService.filterProducts(categoryId, minPrice, maxPrice, page, size));
		
	}
	@Operation(summary = "Advanced product search")
	@GetMapping("/advance")
	public ResponseEntity<Page<ProductResponseDto>> advanceProduct(@RequestParam String keyword,
			@RequestParam(required=false)Long categoryId,
			@RequestParam(required=false)Double minPrice,
            @RequestParam(required=false)Double maxPrice,@RequestParam(required=false, defaultValue="0")int page,
            @RequestParam( defaultValue="10") int size,@RequestParam( defaultValue="id") String sortBy,
            @RequestParam( defaultValue="asc") String sortDir){
				
return ResponseEntity.ok(productService.advanceFilter(keyword, categoryId, minPrice, maxPrice, page, size, sortBy, sortDir));

}
	
	@Operation(
		    summary = "Upload product image",
		    description = "Upload an image file for a product. JWT required.")
		@PostMapping(
		    value = "/{id}/upload-image",
		    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
		)
		public ResponseEntity<ProductResponseDto> uploadImage(
		        @PathVariable Long id,

		        @Parameter(
		            description = "Product image file",
		            content = @Content(
		                mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
		                schema = @Schema(type = "string", format = "binary")
		            )
		        )
		        @RequestParam("file") MultipartFile file
		) throws IOException {

		    return ResponseEntity.ok(productService.uploadImage(id, file));
		}
	

}
