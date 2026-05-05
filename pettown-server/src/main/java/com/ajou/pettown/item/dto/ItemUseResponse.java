package com.ajou.pettown.item.dto;

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

    @Getter
    @AllArgsConstructor
    public static class Stat {
        private Integer current;
        private Integer max;
    }

}
