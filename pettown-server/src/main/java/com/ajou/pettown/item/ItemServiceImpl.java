package com.ajou.pettown.item;

// Implementation of ItemService handling item stacking, usage, and pet level-up logic.
import com.ajou.pettown.auth.User;
import com.ajou.pettown.auth.UserRepository;
import com.ajou.pettown.item.dto.ItemAcquireResponse;
import com.ajou.pettown.item.dto.ItemUseResponse;
import com.ajou.pettown.mail.PetMailService;
import com.ajou.pettown.pet.Pet;
import com.ajou.pettown.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetMailService petMailService;

    @Override
    @Transactional
    public ItemAcquireResponse acquireItem(String userId, Integer itemTypeId, Integer count) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        // Create a new item row if this type doesn't exist for the user yet
        Item item = itemRepository.findByUser_IdAndItemTypeId(user.getId(), itemTypeId)
                .orElseGet(() -> itemRepository.save(Item.builder()
                        .user(user)
                        .itemTypeId(itemTypeId)
                        .build()));

        item.addCount(count);
        itemRepository.save(item);

        petMailService.sendItemMails(user, itemTypeId);

        return new ItemAcquireResponse(true,
                new ItemAcquireResponse.ItemInfo(item.getItemId(), item.getItemTypeId(), item.getCount()));
    }

    @Override
    @Transactional
    public ItemUseResponse useItem(String userId, Long petId, Integer itemTypeId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 펫입니다."));

        if (!pet.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("본인의 펫이 아닙니다.");
        }

        Item item = itemRepository.findByUser_IdAndItemTypeId(user.getId(), itemTypeId)
                .orElseThrow(() -> new RuntimeException("보유하지 않은 아이템입니다."));

        if (item.getCount() <= 0) {
            throw new RuntimeException("아이템 수량이 부족합니다.");
        }

        // itemTypeId 1-4: food, 5: water
        if (itemTypeId == 5) {
            pet.drinkWater();
        } else {
            pet.feedFood();
        }

        // Level up when both food and water reach their max values; capped at level 3
        if (pet.getLevel() < 3
                && pet.getFood() >= getFoodMax(pet.getLevel())
                && pet.getWater() >= getWaterMax(pet.getLevel())) {
            pet.levelUp();
            petMailService.sendLevelUpMail(user, pet);
        }

        item.useOne();
        petRepository.save(pet);
        itemRepository.save(item);

        int level = pet.getLevel();
        ItemUseResponse.Stat food = new ItemUseResponse.Stat(pet.getFood(), getFoodMax(level));
        ItemUseResponse.Stat water = new ItemUseResponse.Stat(pet.getWater(), getWaterMax(level));

        return new ItemUseResponse(
                true,
                new ItemUseResponse.PetInfo(pet.getPetId(), level, food, water));
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
