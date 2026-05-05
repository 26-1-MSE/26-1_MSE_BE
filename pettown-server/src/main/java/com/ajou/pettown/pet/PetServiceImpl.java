package com.ajou.pettown.pet;

import com.ajou.pettown.auth.User;
import com.ajou.pettown.auth.UserRepository;
import com.ajou.pettown.item.ItemRepository;
import com.ajou.pettown.pet.dto.PetAcquireResponse;
import com.ajou.pettown.pet.dto.PetRoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {


    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Pet> getPets(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        return petRepository.findByUser_IdOrderByPetIdAsc(user.getId());
    }

    @Override
    public PetAcquireResponse acquirePet(String userId, Integer petTypeId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        int count = (int) petRepository.countByUser_Id(user.getId());
        if (count >= 4) {
            throw new RuntimeException("펫은 최대 4마리까지만 보유할 수 있습니다.");
        }

        Pet pet = Pet.builder()
                .user(user)
                .petTypeId(petTypeId)
                .build();
        petRepository.save(pet);

        return new PetAcquireResponse(
                true,
                new PetAcquireResponse.PetInfo(pet.getPetId(), pet.getPetTypeId(), pet.getLevel()),
                user.getUserId(),
                count + 1);
    }

    @Override
    public PetRoomResponse getPetRoom(String userId, Long petId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 펫입니다."));

        if (!pet.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("본인의 펫이 아닙니다.");
        }

        int level = pet.getLevel();
        PetRoomResponse.Stat food = new PetRoomResponse.Stat(pet.getFood(), getFoodMax(level));
        PetRoomResponse.Stat water = new PetRoomResponse.Stat(pet.getWater(), getWaterMax(level));

        PetRoomResponse.PetInfo petInfo = new PetRoomResponse.PetInfo(
                pet.getPetId(), pet.getPetTypeId(), level, food, water);

        List<PetRoomResponse.ItemInfo> items = itemRepository.findByUser_Id(user.getId())
                .stream()
                .filter(item -> item.getCount() > 0)
                .map(item -> new PetRoomResponse.ItemInfo(item.getItemId(), item.getItemTypeId(), item.getCount()))
                .collect(Collectors.toList());

        return new PetRoomResponse(petInfo, items);
    }

    private int getFoodMax(int level) {
        return switch (level) {
            case 2 -> 10;
            case 3 -> 15;
            default -> 5;
        };
    }

    private int getWaterMax(int level) {
        return switch (level) {
            case 2 -> 6;
            case 3 -> 10;
            default -> 3;
        };
    }
}
