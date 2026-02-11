package com.artichourey.ecommerce.productservice.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.artichourey.ecommerce.productservice.dtos.ProductDto;
import com.artichourey.ecommerce.productservice.entity.Category;
import com.artichourey.ecommerce.productservice.entity.Product;
import com.artichourey.ecommerce.productservice.exception.ResourceNotFoundException;
import com.artichourey.ecommerce.productservice.mapper.ProductMapper;
import com.artichourey.ecommerce.productservice.repository.ProductRepository;
import com.artichourey.ecommerce.productservice.service.ProductService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final Logger log=LoggerFactory.getLogger(ProductServiceImpl.class);
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	
	private final String uploadDir=System.getProperty("user.dir")+"uploads/products/";
	
	
	
	@Override
	public ProductDto createProduct(ProductDto productDto) {
		log.info("Creating product: name='{}', categoryId={}", productDto.getName(), productDto.getCategoryId());
		Category category=null;
		if(productDto.getCategoryId()!=null) {
			category= new Category();
			category.setId(productDto.getCategoryId());
			
		}
		Product product=productMapper.toEntity(productDto, category);
		Product saved=productRepository.save(product);
		log.info("Product created successfully: ID={}, name='{}'", saved.getId(), saved.getName());
		return productMapper.toDto(saved);
	}

	@Override
	public ProductDto updateProduct(Long id, ProductDto productDto) {
		log.info("Updating product: ID={}", id);
		Product product =productRepository.findById(id).orElseThrow(()-> 
		new ResourceNotFoundException("Product is not found "+id));
		product.setName(productDto.getName());
		product.setBrand(productDto.getBrand());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setDiscountPrice(productDto.getDiscountPrice());
		product.setQuantity(productDto.getQuantity());
		product.setImageUrl(productDto.getImageUrl());
		if(productDto.getCategoryId()!=null) {
			Category category= new Category();
			category.setId(productDto.getCategoryId());
			product.setCategory(category);
		}
		
	 Product saved=productRepository.save(product);
	 log.info("Product updated successfully: ID={}, name='{}'", saved.getId(), saved.getName());
		return productMapper.toDto(saved);
	}

	@Override
	public void deleteProduct(Long id) {
		log.info("Deleting product with ID={}", id);
		Product product=productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found"+id));
		log.info("Product deleted successfully: ID={}, name='{}'", product.getId(), product.getName());
        productRepository.delete(product);
         
         
	}

	@Override
	public ProductDto getProductById(Long id) {
		log.info("Fetching product with ID={}", id);
		Product product=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found"+id));
		log.info("Product fetched successfully: ID={}, name='{}'", product.getId(), product.getName());
		return productMapper.toDto(product);
	}

	@Override
	public Page<ProductDto> getAllProducts(int page, int size, String sortBy, String sortDir) {
		log.info("Fetching all products: page={}, size={}, sortBy={}, sortDir={}", page, size, sortBy, sortDir);

		    Sort sort=sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
		    Pageable pageable=PageRequest.of(page, size, sort);
		    Page<Product> productPage=productRepository.findAll(pageable);
		    log.info("Total products fetched: {}", productPage.getTotalElements());
		return productPage.map(productMapper::toDto);
	}

	@Override
	public Page<ProductDto> searchProduct(String keyword, int page, int size) {
		 log.info("Searching products with keyword='{}', page={}, size={}", keyword, page, size);
		Pageable pageable=PageRequest.of(page, size);
		Page<Product> productPage=productRepository.searchProducts(keyword, pageable);
		log.info("Products found: {}", productPage.getTotalElements());
		return productPage.map(productMapper::toDto);
	}

	@Override
	public Page<ProductDto> filterProducts(Long categoryId, double minPrice, double maxPrice, int page, int size) {
		 log.info("Filtering products: categoryId={}, minPrice={}, maxPrice={}, page={}, size={}",
	                categoryId, minPrice, maxPrice, page, size);
		Pageable pageable=PageRequest.of(page, size);
		Page<Product> productPage=productRepository.advanceFilter(null, categoryId, minPrice, maxPrice, pageable);
		log.info("Filtered products found: {}", productPage.getTotalElements());
		return productPage.map(productMapper::toDto);
	}

	@Override
	public Page<ProductDto> advanceFilter(String keyword, Long categoryId, double minPrice, double maxPrice, int page,
			int size, String sortBy, String sortDir) {
		log.info("Advance filtering products: keyword='{}', categoryId={}, minPrice={}, maxPrice={}, page={}, size={}, sortBy={}, sortDir={}",
                keyword, categoryId, minPrice, maxPrice, page, size, sortBy, sortDir);
		Pageable pageable=PageRequest.of(page, size);
		Page<Product> productPage=productRepository.advanceFilter(keyword, categoryId, minPrice, maxPrice, pageable);
		log.info("Advance filtered products found: {}", productPage.getTotalElements());
		return productPage.map(productMapper::toDto);
		
	}

	@Override
	public ProductDto uploadImage(Long productId, MultipartFile file) throws IOException {
		log.info("Uploading image for product ID={}", productId);
		Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Resource Not Found"+productId));
		if(file.isEmpty()) {
			throw new RuntimeException("Image file is Empty");
		}long maxSize=2*1024*1024;
		if(file.getSize()>maxSize) {
			throw new RuntimeException("file size must be less then 2 MB");
		}
		List<String> allowedType=List.of("image/jpeg","image/png","image/jpg");
		if(!allowedType.contains(file.getContentType())) {
			throw new RuntimeException("only jpeg, png, jpg allowed");
		}
		
		String originalName=file.getOriginalFilename();
		if(originalName==null || !originalName.contains(".")) {
			throw new RuntimeException("Invalid file name");
		}
		String ext=originalName.substring(originalName.lastIndexOf(".")+1).toLowerCase();
		List<String> allowedExt=List.of("jpg","jpeg","png");
		if(!allowedExt.contains(ext)) {
			throw new RuntimeException("Invalid image extension");
		}
		
		File folder= new File(uploadDir);
		if(!folder.exists()) {
		   folder.mkdirs();
		}
		String fileName=UUID.randomUUID().toString()+"."+ext;
		Path filePath=Paths.get(uploadDir+fileName);
		Files.write(filePath, file.getBytes());
		String imageUrl="/products/images/"+fileName;
		product.setImageUrl(imageUrl);
		Product saved=productRepository.save(product);
		 log.info("Image uploaded successfully for product ID={}, fileName='{}', URL='{}'", productId, fileName, imageUrl);
		
		return productMapper.toDto(saved);
	}

}
