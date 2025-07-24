package com.marcoindev.mcshop.order.feign;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "${product.service.name}", url = "${product.service.path}")
public interface ProductFeignClient {
    @GetMapping("/api/v1/product/{id}")
    ProductDTO getProductById(@PathVariable("id") String id);

    @Data
    class ProductDTO {
        public String id;
        public BigDecimal price;
    }
} 