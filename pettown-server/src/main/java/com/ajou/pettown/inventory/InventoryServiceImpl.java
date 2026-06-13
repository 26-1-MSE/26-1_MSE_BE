package com.ajou.pettown.inventory;

// Implementation of InventoryService that aggregates pet and item data for a user.
import com.ajou.pettown.auth.User;
import com.ajou.pettown.auth.UserRepository;
import com.ajou.pettown.inventory.dto.InventoryResponse;
import com.ajou.pettown.item.ItemRepository;
import com.ajou.pettown.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public InventoryResponse getInventory(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Map pets preserving order by petId
        List<InventoryResponse.PetInfo> pets = petRepository.findByUser_IdOrderByPetIdAsc(user.getId())
                .stream()
                .map(pet -> new InventoryResponse.PetInfo(
                        pet.getPetId(), pet.getPetTypeId(), pet.getLevel(), pet.getFood(), pet.getWater()))
                .collect(Collectors.toList());

        // Exclude items with zero count from the response
        List<InventoryResponse.ItemInfo> items = itemRepository.findByUser_Id(user.getId())
                .stream()
                .filter(item -> item.getCount() > 0)
                .map(item -> new InventoryResponse.ItemInfo(
                        item.getItemId(), item.getItemTypeId(), item.getCount()))
                .collect(Collectors.toList());

        return new InventoryResponse(pets, items);
    }
}
