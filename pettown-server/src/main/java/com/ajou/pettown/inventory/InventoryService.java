package com.ajou.pettown.inventory;

// Service interface for retrieving a user's combined pet and item inventory.
import com.ajou.pettown.inventory.dto.InventoryResponse;

public interface InventoryService {
    InventoryResponse getInventory(String userId);
}
