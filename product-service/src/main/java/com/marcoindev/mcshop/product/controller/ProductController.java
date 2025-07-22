package com.marcoindev.mcshop.product.controller;

import com.marcoindev.mcshop.common.payload.ApiResponse;
import com.marcoindev.mcshop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcoindev.mcshop.product.entity.ProductEntity;
import java.util.List;

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
    public ResponseEntity<ApiResponse<List<ProductEntity>>> getAllProducts() {
        List<ProductEntity> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.<List<ProductEntity>>builder().data(products).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductEntity>> getProductById(@PathVariable String id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(ApiResponse.<ProductEntity>builder().data(product).build()))
                .orElse(ResponseEntity.notFound().build());
    }
}