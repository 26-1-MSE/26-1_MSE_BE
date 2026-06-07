package com.ajou.pettown.admin;

// Service interface for admin user management operations.
import com.ajou.pettown.admin.dto.AdminUserDetailDto;
import com.ajou.pettown.admin.dto.AdminUserDto;

import java.util.List;

public interface AdminService {
    List<AdminUserDto> getAllUsers(String keyword); // keyword filters by userId or nickname; null returns all
    AdminUserDetailDto getUserDetail(Long userId);  // full detail: pets stats + items
    void addItems(Long userId, java.util.Map<Integer, Integer> itemCounts); // itemTypeId → count
    void resetPetStats(Long petId);                  // level, food, water → 0
    void deletePet(Long petId);                      // 펫 삭제 (메일 로그 포함)
    void addPet(Long userId, Integer petTypeId);     // 새 펫 추가 (4마리 제한 적용)
    void deleteUser(Long userId);    // deletes user and all associated data (pets, items, mails)
    void deleteAllUsers();           // wipes every user from the database
}
