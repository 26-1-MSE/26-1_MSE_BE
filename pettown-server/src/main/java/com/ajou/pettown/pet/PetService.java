package com.ajou.pettown.pet;

import com.ajou.pettown.pet.dto.PetAcquireResponse;
import java.util.List;

public interface PetService {
    List<Pet> getPets(String userId);
    PetAcquireResponse acquirePet(String userId, Integer petTypeId);
}