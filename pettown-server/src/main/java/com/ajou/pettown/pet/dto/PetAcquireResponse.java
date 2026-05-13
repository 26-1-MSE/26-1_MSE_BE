package com.ajou.pettown.pet.dto;

// Response returned after a pet is successfully acquired, including updated ownership count.
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PetAcquireResponse {
    private boolean success;
    private PetInfo pet;
    private String userId;
    private int totalPetCount; // total number of pets the user now owns

    public PetAcquireResponse(boolean success, PetInfo pet, String userId, int totalPetCount) {
        this.success = success;
        this.pet = pet;
        this.userId = userId;
        this.totalPetCount = totalPetCount;
    }

    public PetAcquireResponse(boolean success, PetInfo pet) {
        this.success = success;
        this.pet = pet;
    }

    @Getter
    @AllArgsConstructor
    public static class PetInfo {
        private Long petId;
        private Integer petTypeId;
        private String petName;
        private Integer level;
    }
}
