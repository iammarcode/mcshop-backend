package com.marcoindev.mcshop.product.controller;

import com.marcoindev.mcshop.common.payload.ApiResponse;
import com.marcoindev.mcshop.product.entity.ProductEntity;
import com.marcoindev.mcshop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(ApiResponse.<String>builder().data(userId).build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<ProductEntity>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductEntity> products = productService.getAllProducts(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.<Page<ProductEntity>>builder().data(products).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductEntity>> getProductById(@PathVariable String id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(ApiResponse.<ProductEntity>builder().data(product).build()))
                .orElse(ResponseEntity.notFound().build());
    }
}