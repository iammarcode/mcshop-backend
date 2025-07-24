package com.marcoindev.mcshop.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marcoindev.mcshop.product.entity.ProductInventoryEntity;
import com.marcoindev.mcshop.product.repository.ProductInventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product/inventory")
@RequiredArgsConstructor
public class ProductInventoryController {
    private final ProductInventoryMapper inventoryMapper;

    @GetMapping("/{productId}")
    public ProductInventoryEntity getInventory(@PathVariable String productId) {
        return inventoryMapper.selectOne(
            new QueryWrapper<ProductInventoryEntity>().eq("product_id", productId)
        );
    }

    // Reserve inventory (increase reservedQuantity, decrease quantity)
    @PostMapping("/reserve")
    public boolean reserveInventory(@RequestParam String productId, @RequestParam int quantity) {
        ProductInventoryEntity inventory = inventoryMapper.selectOne(
            new QueryWrapper<ProductInventoryEntity>().eq("product_id", productId)
        );
        if (inventory == null || inventory.getQuantity() < quantity) return false;
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setReservedQuantity(
            (inventory.getReservedQuantity() == null ? 0 : inventory.getReservedQuantity()) + quantity
        );
        return inventoryMapper.updateById(inventory) > 0;
    }

    // Finalize inventory (decrease reservedQuantity)
    @PostMapping("/finalize")
    public boolean finalizeInventory(@RequestParam String productId, @RequestParam int quantity) {
        ProductInventoryEntity inventory = inventoryMapper.selectOne(
            new QueryWrapper<ProductInventoryEntity>().eq("product_id", productId)
        );
        if (inventory == null || (inventory.getReservedQuantity() == null ? 0 : inventory.getReservedQuantity()) < quantity) return false;
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        return inventoryMapper.updateById(inventory) > 0;
    }

    // Release inventory (rollback reservation)
    @PostMapping("/release")
    public boolean releaseInventory(@RequestParam String productId, @RequestParam int quantity) {
        ProductInventoryEntity inventory = inventoryMapper.selectOne(
            new QueryWrapper<ProductInventoryEntity>().eq("product_id", productId)
        );
        if (inventory == null) return false;
        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventory.setReservedQuantity(
            (inventory.getReservedQuantity() == null ? 0 : inventory.getReservedQuantity()) - quantity
        );
        return inventoryMapper.updateById(inventory) > 0;
    }
} 