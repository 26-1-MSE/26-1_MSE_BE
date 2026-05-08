package com.ajou.pettown.admin;

// Implementation of AdminService providing user listing, search, and deletion.
import com.ajou.pettown.admin.dto.AdminUserDto;
import com.ajou.pettown.auth.User;
import com.ajou.pettown.auth.UserRepository;
import com.ajou.pettown.item.ItemRepository;
import com.ajou.pettown.mail.MailRepository;
import com.ajou.pettown.pet.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
