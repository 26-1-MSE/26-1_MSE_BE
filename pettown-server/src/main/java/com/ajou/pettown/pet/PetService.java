package com.ajou.pettown.pet;

// Service interface for pet ownership, acquisition, and pet room data.
import com.ajou.pettown.auth.dto.LoginResponse;
import com.ajou.pettown.pet.dto.PetAcquireResponse;
import com.ajou.pettown.pet.dto.PetRoomResponse;
import java.util.List;

public interface PetService {
    List<LoginResponse.OwnedPet> getPets(String userId);
    PetAcquireResponse acquirePet(String userId, Integer petTypeId); // enforces 4-pet limit
    PetRoomResponse getPetRoom(String userId, Long petId);
}
