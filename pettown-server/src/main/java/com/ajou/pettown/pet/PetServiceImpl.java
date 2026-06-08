package com.ajou.pettown.pet;

// Implementation of PetService with ownership validation and level-based stat cap calculation.
import com.ajou.pettown.auth.User;
import com.ajou.pettown.auth.UserRepository;
import com.ajou.pettown.item.ItemRepository;
import com.ajou.pettown.pet.dto.PetAcquireResponse;
import com.ajou.pettown.pet.dto.PetRoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public PetAcquireResponse acquirePet(String userId, Integer petTypeId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        java.util.List<Pet> existingPets = petRepository.findByUser_IdOrderByPetIdAsc(user.getId());
        int count = existingPets.size();
        if (count >= 4) {
            throw new RuntimeException("PET_LIMIT_EXCEEDED");
        }

        // 비어있는 가장 작은 petIndex(1~4)를 찾아 사용 — 중간 인덱스의 펫이 삭제된 경우에도
        // (user_id, pet_index) 유니크 제약 충돌 없이 자리를 채울 수 있도록 함
        java.util.Set<Integer> usedIndexes = existingPets.stream()
                .map(Pet::getPetIndex)
                .collect(java.util.stream.Collectors.toSet());
        int newIndex = 1;
        while (usedIndexes.contains(newIndex) && newIndex <= 4) {
            newIndex++;
        }
        if (newIndex > 4) {
            throw new RuntimeException("PET_LIMIT_EXCEEDED");
        }

        Pet pet;
        try {
            pet = Pet.builder()
                    .user(user)
                    .petTypeId(petTypeId)
                    .petIndex(newIndex)
                    .build();
            petRepository.save(pet);
        } catch (DataIntegrityViolationException e) {
            // race condition: 동시 요청이 같은 petIndex로 insert 시도
            throw new RuntimeException("PET_LIMIT_EXCEEDED");
        }

        return new PetAcquireResponse(
                true,
                new PetAcquireResponse.PetInfo(pet.getPetId(), pet.getPetTypeId(), PetNameMapper.getName(pet.getPetIndex()), pet.getLevel()),
                user.getUserId(),
                count + 1);
    }

    @Override
    public PetRoomResponse getPetRoom(String userId, Long petId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 펫입니다."));

        // Ensure the pet belongs to the requesting user
        if (!pet.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("본인의 펫이 아닙니다.");
        }

        int level = pet.getLevel();
        PetRoomResponse.Stat food = new PetRoomResponse.Stat(pet.getFood(), getFoodMax(level));
        PetRoomResponse.Stat water = new PetRoomResponse.Stat(pet.getWater(), getWaterMax(level));

        PetRoomResponse.PetInfo petInfo = new PetRoomResponse.PetInfo(
                pet.getPetId(), pet.getPetTypeId(), PetNameMapper.getName(pet.getPetIndex()), level, food, water);

        // Include only items with remaining count so the UI shows usable items
        List<PetRoomResponse.ItemInfo> items = itemRepository.findByUser_Id(user.getId())
                .stream()
                .filter(item -> item.getCount() > 0)
                .map(item -> new PetRoomResponse.ItemInfo(item.getItemId(), item.getItemTypeId(), item.getCount()))
                .collect(Collectors.toList());

        return new PetRoomResponse(petInfo, items);
    }

    // Max food value per level: Lv1=5, Lv2=10, Lv3=15
    private int getFoodMax(int level) {
        return switch (level) {
            case 2 -> 10;
            case 3 -> 15;
            default -> 5;
        };
    }

    // Max water value per level: Lv1=3, Lv2=6, Lv3=10
    private int getWaterMax(int level) {
        return switch (level) {
            case 2 -> 6;
            case 3 -> 10;
            default -> 3;
        };
    }
}
