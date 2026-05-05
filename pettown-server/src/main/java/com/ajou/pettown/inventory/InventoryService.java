package com.ajou.pettown.inventory;

import com.ajou.pettown.inventory.dto.InventoryResponse;

public interface InventoryService {
    InventoryResponse getInventory(String userId);
}
