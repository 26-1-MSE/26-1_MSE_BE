package com.ajou.pettown.item;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUser_Id(Long userId);
    Optional<Item> findByUser_IdAndItemTypeId(Long userId, Integer itemTypeId);
}