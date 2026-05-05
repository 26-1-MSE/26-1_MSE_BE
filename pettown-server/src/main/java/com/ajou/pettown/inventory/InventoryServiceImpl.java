package com.ajou.pettown.inventory;

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
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        List<InventoryResponse.PetInfo> pets = petRepository.findByUser_IdOrderByPetIdAsc(user.getId())
                .stream()
                .map(pet -> new InventoryResponse.PetInfo(
                        pet.getPetId(), pet.getPetTypeId(), pet.getLevel(), pet.getFood(), pet.getWater()))
                .collect(Collectors.toList());

        List<InventoryResponse.ItemInfo> items = itemRepository.findByUser_Id(user.getId())
                .stream()
                .filter(item -> item.getCount() > 0)
                .map(item -> new InventoryResponse.ItemInfo(
                        item.getItemId(), item.getItemTypeId(), item.getCount()))
                .collect(Collectors.toList());

        return new InventoryResponse(pets, items);
    }
}
