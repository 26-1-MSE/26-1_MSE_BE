package com.ajou.pettown.pet;

// Service interface for pet ownership, acquisition, and pet room data.
import com.ajou.pettown.pet.dto.PetAcquireResponse;
import com.ajou.pettown.pet.dto.PetRoomResponse;

public interface PetService {
    PetAcquireResponse acquirePet(String userId, Integer petTypeId); // enforces 4-pet limit
    PetRoomResponse getPetRoom(String userId, Long petId);
}
