package com.ajou.pettown.inventory;

import com.ajou.pettown.common.dto.ApiResponse;
import com.ajou.pettown.inventory.dto.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventory(
            @RequestAttribute("userId") String userId) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(inventoryService.getInventory(userId)));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.fail(e.getMessage()));
        }
    }
}
