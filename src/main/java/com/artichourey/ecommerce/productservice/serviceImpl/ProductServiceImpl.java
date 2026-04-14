package com.artichourey.ecommerce.productservice.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.artichourey.ecommerce.productservice.dtos.ProductDto;
import com.artichourey.ecommerce.productservice.dtos.ProductResponseDto;
import com.artichourey.ecommerce.productservice.entity.Category;
import com.artichourey.ecommerce.productservice.entity.Product;
import com.artichourey.ecommerce.productservice.exception.ResourceNotFoundException;
import com.artichourey.ecommerce.productservice.mapper.ProductMapper;
import com.artichourey.ecommerce.productservice.repository.CategoryRepository;
import com.artichourey.ecommerce.productservice.repository.ProductRepository;
import com.artichourey.ecommerce.productservice.service.ProductService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

   
    // CREATE PRODUCT
    
    @Override
    public ProductResponseDto createProduct(ProductDto productDto) {

        log.info("Creating product: name='{}', categoryId={}",
                productDto.getName(), productDto.getCategoryId());

        Category category = null;

        if (productDto.getCategoryId() != null) {
            category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found with id: " + productDto.getCategoryId()));
        }

        Product product = productMapper.toEntity(productDto, category);
        Product saved = productRepository.save(product);

        log.info("Product created successfully: ID={}, name='{}', skuCode='{}'",
                saved.getId(), saved.getName(), saved.getSkuCode());

        return productMapper.toDto(saved);
    }

   
    // UPDATE PRODUCT
   
    @Override
    public ProductResponseDto updateProduct(Long id, ProductDto productDto) {

        log.info("Updating product: ID={}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + id));

        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountPrice(productDto.getDiscountPrice());
        product.setQuantity(productDto.getQuantity());
        product.setImageUrl(productDto.getImageUrl());
        product.setSkuCode(productDto.getSkuCode());

        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found with id: " + productDto.getCategoryId()));
            product.setCategory(category);
        }

        Product saved = productRepository.save(product);

        log.info("Product updated successfully: ID={}, name='{}', skuCode='{}'",
                saved.getId(), saved.getName(), saved.getSkuCode());

        return productMapper.toDto(saved);
    }

    
    // DELETE PRODUCT
   
    @Override
    public void deleteProduct(Long id) {

        log.info("Deleting product with ID={}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + id));

        productRepository.delete(product);

        log.info("Product deleted successfully: ID={}, name='{}', skuCode='{}'",
                product.getId(), product.getName(), product.getSkuCode());
    }

    
    // GET PRODUCT BY ID

    @Override
    public ProductResponseDto getProductById(Long id) {

        log.info("Fetching product with ID={}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + id));

        return productMapper.toDto(product);
    }


    // GET ALL PRODUCTS (PAGINATION)
 
    @Override
    public Page<ProductResponseDto> getAllProducts(
            int page, int size, String sortBy, String sortDir) {

        log.info("Fetching products: page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        log.info("Total products fetched: {}", productPage.getTotalElements());

        return productPage.map(productMapper::toDto);
    }
    
    // SEARCH PRODUCTS
   
    @Override
    public Page<ProductResponseDto> searchProduct(
            String keyword, int page, int size) {

        log.info("Searching products: keyword='{}', page={}, size={}",
                keyword, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage =
                productRepository.searchProducts(keyword, pageable);

        return productPage.map(productMapper::toDto);
    }

    
    // FILTER PRODUCTS
   
    @Override
    public Page<ProductResponseDto> filterProducts(
            Long categoryId, double minPrice, double maxPrice,
            int page, int size) {

        log.info("Filtering products: categoryId={}, minPrice={}, maxPrice={}",
                categoryId, minPrice, maxPrice);

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage =
                productRepository.advanceFilter(
                        null, categoryId, minPrice, maxPrice, pageable);

        return productPage.map(productMapper::toDto);
    }

    
    // ADVANCED FILTER
   
    @Override
    public Page<ProductResponseDto> advanceFilter(
            String keyword, Long categoryId,
            double minPrice, double maxPrice,
            int page, int size,
            String sortBy, String sortDir) {

        log.info("Advanced filtering products");

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage =
                productRepository.advanceFilter(
                        keyword, categoryId, minPrice, maxPrice, pageable);

        return productPage.map(productMapper::toDto);
    }

     
    // UPLOAD IMAGE
     
    @Override
    public ProductResponseDto uploadImage(Long productId, MultipartFile file)
            throws IOException {

        log.info("Uploading image for product ID={}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

       
        if (file.isEmpty()) {
            throw new RuntimeException("Image file is empty");
        }

        long maxSize = 2 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new RuntimeException("File size must be less than 2MB");
        }

        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/jpg");
        if (!allowedTypes.contains(file.getContentType())) {
            throw new RuntimeException("Only JPG, JPEG, PNG allowed");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            throw new RuntimeException("Invalid file name");
        }

        String ext = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedExt = List.of("jpg", "jpeg", "png");

        if (!allowedExt.contains(ext)) {
            throw new RuntimeException("Invalid file extension");
        }

        
        Path uploadPath = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();

        
        Files.createDirectories(uploadPath);

        
        String fileName = UUID.randomUUID().toString() + "." + ext;

        
        Path filePath = uploadPath.resolve(fileName);

        
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = "/api/products/images/" + fileName;

        product.setImageUrl(imageUrl);

        Product saved = productRepository.save(product);

        log.info("Image uploaded successfully: productId={}, fileName='{}', path='{}'",
                productId, fileName, filePath);

        return productMapper.toDto(saved);
    }
}