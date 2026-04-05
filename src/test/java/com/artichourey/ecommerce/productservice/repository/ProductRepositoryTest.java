package com.artichourey.ecommerce.productservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.artichourey.ecommerce.productservice.entity.Category;
import com.artichourey.ecommerce.productservice.entity.Product;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {
	
	 @Autowired
	    private ProductRepository productRepository;

	    @Autowired
	    private CategoryRepository categoryRepository;

	    @Test
	    void searchProducts_ShouldReturnMatchingProducts() {

	        Category category = categoryRepository.save(
	                Category.builder().name("Electronics").build()
	        );

	        Product product = Product.builder()
	                .name("Laptop")
	                .description("Gaming laptop")
	                .price(BigDecimal.valueOf(1500))
	                .category(category)
	                .build();

	        productRepository.save(product);

	        Page<Product> result =
	                productRepository.searchProducts("laptop",
	                        PageRequest.of(0, 10));

	        assertEquals(1, result.getTotalElements());
	        assertEquals("Laptop", result.getContent().get(0).getName());
	    }

	    @Test
	    void advanceFilter_ShouldFilterByPriceRange() {

	        Category category = categoryRepository.save(
	                Category.builder().name("Electronics").build()
	        );

	        productRepository.save(Product.builder()
	                .name("Cheap Phone")
	                .description("Budget phone")
	                .price(BigDecimal.valueOf(200))
	                .category(category)
	                .build());

	        productRepository.save(Product.builder()
	                .name("Expensive Laptop")
	                .description("High-end laptop")
	                .price(BigDecimal.valueOf(2000))
	                .category(category)
	                .build());

	        Page<Product> result =
	                productRepository.advanceFilter(
	                        null,
	                        category.getId(),
	                        100.0,
	                        1000.0,
	                        PageRequest.of(0, 10)
	                );

	        assertEquals(1, result.getTotalElements());
	        assertEquals("Cheap Phone",
	                result.getContent().get(0).getName());
	    }

}
