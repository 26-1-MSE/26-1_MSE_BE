package com.ajou.pettown.item;

// REST controller for acquiring items and using them on pets.
import com.ajou.pettown.common.dto.ApiResponse;
import com.ajou.pettown.item.dto.ItemAcquireRequest;
import com.ajou.pettown.item.dto.ItemAcquireResponse;
import com.ajou.pettown.item.dto.ItemUseRequest;
import com.ajou.pettown.item.dto.ItemUseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Add a quantity of an item to the user's inventory
    @PostMapping("/acquire")
    public ResponseEntity<ApiResponse<ItemAcquireResponse>> acquireItem(
            @RequestAttribute("userId") String userId,
            @RequestBody ItemAcquireRequest request) {
        try {
            ItemAcquireResponse response = itemService.acquireItem(userId, request.getItemTypeId(), request.getCount());
            return ResponseEntity.ok(ApiResponse.ok(response));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.fail(e.getMessage()));
        }
    }

    // Use one item on a specific pet, applying its stat effect and triggering level-up if eligible
    @PostMapping("/use")
    public ResponseEntity<ApiResponse<ItemUseResponse>> useItem(
            @RequestAttribute("userId") String userId,
            @RequestBody ItemUseRequest request) {
        try {
            ItemUseResponse response = itemService.useItem(userId, request.getPetId(), request.getItemTypeId());
            return ResponseEntity.ok(ApiResponse.ok(response));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.fail(e.getMessage()));
        }
    }
}
