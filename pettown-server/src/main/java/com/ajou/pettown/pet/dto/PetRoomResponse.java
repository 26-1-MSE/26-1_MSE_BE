package com.ajou.pettown.pet.dto;

// Response for the pet room screen, containing the pet's current state and the user's usable items.
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PetRoomResponse {
    private PetInfo pet;
    private List<ItemInfo> items; // items with count > 0 that can be used on this pet

    @Getter
    @AllArgsConstructor
    public static class PetInfo {
        private Long petId;
        private Integer petTypeId;
        private String petName;
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

    @Getter
    @AllArgsConstructor
    public static class ItemInfo {
        private Long itemId;
        private Integer itemTypeId;
        private Integer count;
    }
}
