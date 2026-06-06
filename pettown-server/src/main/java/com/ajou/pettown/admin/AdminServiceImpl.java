package com.ajou.pettown.admin;

// Implementation of AdminService providing user listing, search, and deletion.
import com.ajou.pettown.admin.dto.AdminUserDetailDto;
import com.ajou.pettown.admin.dto.AdminUserDto;
import com.ajou.pettown.auth.User;
import com.ajou.pettown.auth.UserRepository;
import com.ajou.pettown.item.Item;
import com.ajou.pettown.item.ItemRepository;
import com.ajou.pettown.mail.MailRepository;
import com.ajou.pettown.pet.PetNameMapper;
import com.ajou.pettown.pet.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final ItemRepository itemRepository;
    private final MailRepository mailRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserDto> getAllUsers(String keyword) {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(u -> keyword == null || keyword.isBlank()
                        || u.getUserId().contains(keyword)
                        || u.getNickname().contains(keyword))
                .map(u -> new AdminUserDto(
                        u.getId(),
                        u.getUserId(),
                        u.getNickname(),
                        u.getShopName(),
                        petRepository.countByUser_Id(u.getId()),
                        // Sum counts across all item types (not number of types)
                        itemRepository.findByUser_Id(u.getId()).stream().mapToInt(item -> item.getCount()).sum(),
                        mailRepository.findByUser_IdOrderByCreatedAtDesc(u.getId()).size(),
                        u.getLastActiveAt()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserDetailDto getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        List<AdminUserDetailDto.PetDetail> pets = petRepository.findByUser_IdOrderByPetIdAsc(userId)
                .stream()
                .map(p -> new AdminUserDetailDto.PetDetail(
                        p.getPetId(),
                        PetNameMapper.getName(p.getPetIndex()),
                        getPetTypeName(p.getPetTypeId()),
                        p.getLevel(),
                        p.getFood(),
                        p.getWater()
                ))
                .toList();

        List<AdminUserDetailDto.ItemDetail> items = itemRepository.findByUser_Id(userId)
                .stream()
                .filter(item -> item.getCount() > 0)
                .map(item -> new AdminUserDetailDto.ItemDetail(
                        getItemName(item.getItemTypeId()),
                        item.getCount()
                ))
                .toList();

        return new AdminUserDetailDto(
                user.getId(),
                user.getUserId(),
                user.getNickname(),
                user.getShopName(),
                user.getLastActiveAt(),
                pets,
                items
        );
    }

    @Override
    @Transactional
    public void addItems(Long userId, Map<Integer, Integer> itemCounts) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        for (Map.Entry<Integer, Integer> entry : itemCounts.entrySet()) {
            int itemTypeId = entry.getKey();
            int count = entry.getValue();
            if (count <= 0) continue;

            Item item;
            try {
                item = itemRepository.findByUser_IdAndItemTypeId(userId, itemTypeId)
                        .orElseGet(() -> itemRepository.save(Item.builder()
                                .user(user)
                                .itemTypeId(itemTypeId)
                                .build()));
            } catch (DataIntegrityViolationException e) {
                item = itemRepository.findByUser_IdAndItemTypeId(userId, itemTypeId)
                        .orElseThrow(() -> new RuntimeException("아이템 조회 실패"));
            }
            item.addCount(count);
            itemRepository.save(item);
        }
    }

    private String getPetTypeName(Integer petTypeId) {
        return switch (petTypeId) {
            case 1 -> "Judy";
            case 2 -> "Nick";
            case 3 -> "Bambi";
            case 4 -> "Pumba";
            default -> "Unknown";
        };
    }

    private String getItemName(Integer itemTypeId) {
        return switch (itemTypeId) {
            case 1 -> "Pumpkin";
            case 2 -> "Banana";
            case 3 -> "Apple";
            case 4 -> "Carrot";
            case 5 -> "Water";
            default -> "Unknown";
        };
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        // Delete child records first to satisfy foreign key constraints
        mailRepository.deleteByUser_Id(userId);
        itemRepository.deleteByUser_Id(userId);
        petRepository.deleteByUser_Id(userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void deleteAllUsers() {
        // Order matters: child tables must be cleared before the users table
        mailRepository.deleteAll();
        itemRepository.deleteAll();
        petRepository.deleteAll();
        userRepository.deleteAll();
    }
}
