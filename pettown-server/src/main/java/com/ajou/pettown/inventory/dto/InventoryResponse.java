package com.ajou.pettown.inventory.dto;

// Response containing the user's full list of pets and items.
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InventoryResponse {
    private List<PetInfo> pets;
    private List<ItemInfo> items;

    // Pet snapshot including current food/water stats
    @Getter
    @AllArgsConstructor
    public static class PetInfo {
        private Long petId;
        private Integer petTypeId;
        private Integer level;
        private Integer food;
        private Integer water;
    }

    // Item snapshot with remaining count
    @Getter
    @AllArgsConstructor
    public static class ItemInfo {
        private Long itemId;
        private Integer itemTypeId;
        private Integer count;
    }
}
