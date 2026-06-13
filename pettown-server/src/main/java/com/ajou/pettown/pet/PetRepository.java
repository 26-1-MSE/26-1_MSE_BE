package com.ajou.pettown.pet;

// Repository for Pet entity, providing user-scoped queries with ascending pet ID ordering and ownership count.
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUser_IdOrderByPetIdAsc(Long userId);
    int countByUser_Id(Long userId);
    void deleteByUser_Id(Long userId);

    // Pessimistic write lock, serializes concurrent useItem requests for the same pet
    // so feed/drink/levelUp updates can't be lost to a race (lost update / duplicate level-up)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Pet p where p.petId = :petId")
    Optional<Pet> findByIdForUpdate(@Param("petId") Long petId);
}
