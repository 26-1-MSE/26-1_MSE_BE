package com.ajou.pettown.pet.dto;

public class PetAcquireResponse {
    private boolean success;
    private PetInfo pet;

    public PetAcquireResponse(boolean success, PetInfo pet) {
        this.success = success;
        this.pet = pet;
    }

    public boolean isSuccess() { return success; }
    public PetInfo getPet() { return pet; }

    public static class PetInfo {
        private Long petId;
        private Integer petTypeId;
        private Integer level;

        public PetInfo(Long petId, Integer petTypeId, Integer level) {
            this.petId = petId;
            this.petTypeId = petTypeId;
            this.level = level;
        }

        public Long getPetId() { return petId; }
        public Integer getPetTypeId() { return petTypeId; }
        public Integer getLevel() { return level; }
    }
}