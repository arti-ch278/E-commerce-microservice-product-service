package com.artichourey.ecommerce.productservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artichourey.ecommerce.productservice.entity.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {

	List<Category> findByParentId(Long parentId);
	
	boolean existsByName(String name);
	
	
}
