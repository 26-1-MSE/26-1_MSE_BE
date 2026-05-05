package com.ajou.pettown.pet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PetRoomResponse {
    private PetInfo pet;
    private List<ItemInfo> items;

    @Getter
    @AllArgsConstructor
    public static class PetInfo {
        private Long petId;
        private Integer petTypeId;
        private Integer level;
        private Stat food;
        private Stat water;
    }

    @Getter
    @AllArgsConstructor
    public static class Stat {
        private Integer current;
        private Integer max;
    }

    @Getter
    @AllArgsConstructor
    public static class ItemInfo {
        private Long itemId;
        private Integer itemTypeId;
        private Integer count;
    }
}
