package com.artichourey.ecommerce.productservice.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "Product Images", description = "Endpoints for serving product images")
public class ImageServeController {

    @Value("${file.upload-dir}")
    private String uploadPath;

    private Path uploadDir;

    @PostConstruct
    public void init() throws IOException {
        // Normalize path and create folder if it doesn't exist
        uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);
    }

    @Operation(summary = "Get product image by file name", description = "Retrieve product image by file name. Returns appropriate image type.")
    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> getImages(@PathVariable String fileName) throws IOException {

        // Resolve and normalize file path
        Path filePath = uploadDir.resolve(fileName).normalize();

        // Prevent path traversal attacks
        if (!filePath.startsWith(uploadDir)) {
            return ResponseEntity.badRequest().build();
        }
        Resource resource = new UrlResource(filePath.toUri());

        // Return 404 if file not found or unreadable
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream"; // fallback
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}