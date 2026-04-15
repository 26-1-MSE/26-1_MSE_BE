package com.ajou.pettown.pet.dto;

public class PetAcquireResponse {
    private boolean success;
    private PetInfo pet;
    private String userId;
    private int totalPetCount;

    // 성공용
    public PetAcquireResponse(boolean success, PetInfo pet, String userId, int totalPetCount) {
        this.success = success;
        this.pet = pet;
        this.userId = userId;
        this.totalPetCount = totalPetCount;
    }

    // 실패용 (기존 유지)
    public PetAcquireResponse(boolean success, PetInfo pet) {
        this.success = success;
        this.pet = pet;
    }

    public boolean isSuccess() {
        return success;
    }

    public PetInfo getPet() {
        return pet;
    }

    public String getUserId() { return userId; }

    public int getTotalPetCount() {
        return totalPetCount;
    }

    public static class PetInfo {
        private Long petId;
        private Integer petTypeId;
        private Integer level;

        public PetInfo(Long petId, Integer petTypeId, Integer level) {
            this.petId = petId;
            this.petTypeId = petTypeId;
            this.level = level;
        }

        public Long getPetId() {
            return petId;
        }

        public Integer getPetTypeId() {
            return petTypeId;
        }

        public Integer getLevel() {
            return level;
        }
    }
}