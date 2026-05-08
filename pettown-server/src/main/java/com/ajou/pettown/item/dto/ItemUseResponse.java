package com.ajou.pettown.item.dto;

// Response returned after using an item, reflecting the pet's updated stats and level.
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemUseResponse {
    private boolean success;
    private PetInfo pet;

    @Getter
    @AllArgsConstructor
    public static class PetInfo {
        private Long petId;
        private Integer level;
        private Stat food;
        private Stat water;
    }

    // Current and maximum value for a pet stat (food or water)
    @Getter
    @AllArgsConstructor
    public static class Stat {
        private Integer current;
        private Integer max;
    }
}
