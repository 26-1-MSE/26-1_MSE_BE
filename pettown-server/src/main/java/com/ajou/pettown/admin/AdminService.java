package com.ajou.pettown.admin;

// Service interface for admin user management operations.
import com.ajou.pettown.admin.dto.AdminUserDto;

import java.util.List;

public interface AdminService {
    List<AdminUserDto> getAllUsers(String keyword); // keyword filters by userId or nickname; null returns all
    void deleteUser(Long userId);    // deletes user and all associated data (pets, items, mails)
    void deleteAllUsers();           // wipes every user from the database
}
