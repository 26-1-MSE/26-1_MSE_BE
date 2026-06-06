package com.ajou.pettown.admin;

// Service interface for admin user management operations.
import com.ajou.pettown.admin.dto.AdminUserDetailDto;
import com.ajou.pettown.admin.dto.AdminUserDto;

import java.util.List;

public interface AdminService {
    List<AdminUserDto> getAllUsers(String keyword); // keyword filters by userId or nickname; null returns all
    AdminUserDetailDto getUserDetail(Long userId);  // full detail: pets stats + items
    void addItems(Long userId, java.util.Map<Integer, Integer> itemCounts); // itemTypeId → count
    void deleteUser(Long userId);    // deletes user and all associated data (pets, items, mails)
    void deleteAllUsers();           // wipes every user from the database
}
