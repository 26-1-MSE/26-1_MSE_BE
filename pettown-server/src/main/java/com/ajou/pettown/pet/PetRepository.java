package com.ajou.pettown.pet;

// Repository for Pet entity, providing user-scoped queries with ascending pet ID ordering and ownership count.
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUser_IdOrderByPetIdAsc(Long userId);
    int countByUser_Id(Long userId);
    void deleteByUser_Id(Long userId);
}
