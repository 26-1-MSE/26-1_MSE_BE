package com.ajou.pettown.admin;

import com.ajou.pettown.admin.dto.AdminUserDto;

import java.util.List;

public interface AdminService {
    List<AdminUserDto> getAllUsers(String keyword);
    void deleteUser(Long userId);
    void deleteAllUsers();
}
