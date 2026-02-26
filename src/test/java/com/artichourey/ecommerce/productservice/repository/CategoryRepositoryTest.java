package com.artichourey.ecommerce.productservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.artichourey.ecommerce.productservice.entity.Category;

@DataJpaTest
@ActiveProfiles("test")
public class CategoryRepositoryTest {
	@Autowired
    private CategoryRepository categoryRepository;

    @Test
    void existsByName_ShouldReturnTrue() {
        Category category = Category.builder()
                .name("Electronics")
                .description("Electronic items")
                .build();

        categoryRepository.save(category);

        boolean exists = categoryRepository.existsByName("Electronics");

        assertTrue(exists);
    }

    @Test
    void findByParentId_ShouldReturnChildren() {

        Category parent = categoryRepository.save(
                Category.builder().name("Parent").build()
        );

        Category child = Category.builder()
                .name("Child")
                .parentId(parent.getId())
                .build();

        categoryRepository.save(child);

        List<Category> result =
                categoryRepository.findByParentId(parent.getId());

        assertEquals(1, result.size());
        assertEquals("Child", result.get(0).getName());
    }

}
