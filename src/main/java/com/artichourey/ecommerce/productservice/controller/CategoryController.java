package com.artichourey.ecommerce.productservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.artichourey.ecommerce.productservice.dtos.CategoryDto;
import com.artichourey.ecommerce.productservice.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Category APIs", description = "Endpoints for managing product categories")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;
	
	@Operation(summary = "Create a category",description = "Create a new product category. JWT required.",security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
		
		
		return new ResponseEntity<>(categoryService.createDto(categoryDto),HttpStatus.CREATED);
		
	}
	@Operation(summary = "Update a category", security = @SecurityRequirement(name = "bearerAuth"))
	@PutMapping("/{id}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id ,@Valid @RequestBody CategoryDto categoryDto){
		
		return ResponseEntity.ok(categoryService.updateDto(id, categoryDto));
		
	}
	@Operation(summary = "Delete a category", security = @SecurityRequirement(name = "bearerAuth"))
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id ){
		categoryService.deleteCategoryById(id);
		
		return ResponseEntity.ok("category deleted succsessfully");
		
	}
	@Operation(summary = "Get a category by ID")
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id){
		
		return ResponseEntity.ok(categoryService.getCategory(id));
		
	}
	@Operation(summary = "Get all categories",description = "Retrieve all product categories")
	@GetMapping
	public ResponseEntity<List<CategoryDto>> getAllCategories(){
		
		return ResponseEntity.ok(categoryService.getAllCategories());
	}
    

}
