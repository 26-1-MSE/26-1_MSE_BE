package com.ajou.pettown.pet;

import java.util.List;

public interface PetService {
    List<Pet> getPets(String userId);
    Pet acquirePet(String userId, Integer petTypeId);
}