package com.marcoindev.mcshop.order.feign;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(
        name = "${product.service.name}",
        path = "${product.service.path}"
)
public interface ProductFeignClient {
    // product
    @GetMapping("/{id}")
    ProductResponse getProductById(@PathVariable("id") String id);


    @Data
    class ProductResponse {
        public ProductDTO data;
    }
    @Data
    class ProductDTO {
        public String id;
        public String name;
        public BigDecimal price;
    }

    // inventory
    @GetMapping("/inventory/{productId}")
    ProductInventoryDTO getInventory(@PathVariable("productId") String productId);

    @PostMapping("/inventory/reserve")
    boolean reserveInventory(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity);

    @PostMapping("/inventory/finalize")
    boolean finalizeInventory(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity);

    @PostMapping("/inventory/release")
    boolean releaseInventory(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity);

    @Data
    class ProductInventoryDTO {
        public String id;
        public Integer quantity;
        public Integer reservedQuantity;
        public String productId;
    }
} 