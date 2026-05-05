package com.ajou.pettown.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InventoryResponse {
    private List<PetInfo> pets;
    private List<ItemInfo> items;

    @Getter
    @AllArgsConstructor
    public static class PetInfo {
        private Long petId;
        private Integer petTypeId;
        private Integer level;
        private Integer food;
        private Integer water;
    }

    @Getter
    @AllArgsConstructor
    public static class ItemInfo {
        private Long itemId;
        private Integer itemTypeId;
        private Integer count;
    }
}
