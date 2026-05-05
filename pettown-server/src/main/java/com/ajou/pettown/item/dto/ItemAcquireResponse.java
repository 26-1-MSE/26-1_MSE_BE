package com.ajou.pettown.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemAcquireResponse {
    private boolean success;
    private ItemInfo item;

    @Getter
    @AllArgsConstructor
    public static class ItemInfo {
        private Long itemId;
        private Integer itemTypeId;
        private Integer count;
    }
}
