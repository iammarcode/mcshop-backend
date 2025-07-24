package com.marcoindev.mcshop.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${product.inventory.service.name}", url = "${product.inventory.service.path}")
public interface ProductInventoryClient {
    @GetMapping("/api/v1/product/inventory/{productId}")
    ProductInventoryDTO getInventory(@PathVariable("productId") String productId);

    @PostMapping("/api/v1/product/inventory/reserve")
    boolean reserveInventory(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity);

    @PostMapping("/api/v1/product/inventory/finalize")
    boolean finalizeInventory(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity);

    @PostMapping("/api/v1/product/inventory/release")
    boolean releaseInventory(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity);

    class ProductInventoryDTO {
        public String id;
        public Integer quantity;
        public Integer reservedQuantity;
        public String productId;
    }
} 