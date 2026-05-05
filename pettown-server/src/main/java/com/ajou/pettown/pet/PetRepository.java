package com.ajou.pettown.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUser_IdOrderByPetIdAsc(Long userId);
    int countByUser_Id(Long userId);
}