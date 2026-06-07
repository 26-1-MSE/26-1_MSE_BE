package com.ajou.pettown.admin;

// Thymeleaf web controller for the admin user management pages.
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Redirect root admin path to the user list page
    @GetMapping
    public String root() {
        return "redirect:/admin/users";
    }

    // Render the login page (Spring Security processes the POST to /admin/login)
    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    // Display all users, optionally filtered by a search keyword
    @GetMapping("/users")
    public String userList(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("users", adminService.getAllUsers(keyword));
        model.addAttribute("keyword", keyword);
        return "admin/users";
    }

    // Render the user detail page showing pet stats and item inventory
    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        model.addAttribute("user", adminService.getUserDetail(id));
        return "admin/user-detail";
    }

    // Add items to a user via admin panel
    @PostMapping("/users/{id}/items")
    public String addItems(@PathVariable Long id,
                           @RequestParam(defaultValue = "0") int item1,
                           @RequestParam(defaultValue = "0") int item2,
                           @RequestParam(defaultValue = "0") int item3,
                           @RequestParam(defaultValue = "0") int item4,
                           @RequestParam(defaultValue = "0") int item5) {
        Map<Integer, Integer> itemCounts = new HashMap<>();
        itemCounts.put(1, item1);
        itemCounts.put(2, item2);
        itemCounts.put(3, item3);
        itemCounts.put(4, item4);
        itemCounts.put(5, item5);
        adminService.addItems(id, itemCounts);
        return "redirect:/admin/users/" + id;
    }

    // Reset a pet's level, food, and water stats to initial values
    @PostMapping("/users/{id}/pets/{petId}/reset")
    public String resetPetStats(@PathVariable Long id, @PathVariable Long petId) {
        adminService.resetPetStats(petId);
        return "redirect:/admin/users/" + id;
    }

    // Delete a specific pet
    @PostMapping("/users/{id}/pets/{petId}/delete")
    public String deletePet(@PathVariable Long id, @PathVariable Long petId) {
        adminService.deletePet(petId);
        return "redirect:/admin/users/" + id;
    }

    // Add a new pet to a user
    @PostMapping("/users/{id}/pets")
    public String addPet(@PathVariable Long id,
                         @RequestParam Integer petTypeId,
                         Model model) {
        try {
            adminService.addPet(id, petTypeId);
        } catch (RuntimeException e) {
            model.addAttribute("user", adminService.getUserDetail(id));
            model.addAttribute("error", "펫을 추가할 수 없습니다: " + e.getMessage());
            return "admin/user-detail";
        }
        return "redirect:/admin/users/" + id;
    }

    // Delete a single user and redirect back, preserving the active search keyword
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id,
                             @RequestParam(required = false) String keyword) {
        adminService.deleteUser(id);
        if (keyword != null && !keyword.isBlank()) {
            return "redirect:/admin/users?keyword=" + keyword;
        }
        return "redirect:/admin/users";
    }

    // Delete all users and redirect to the (now empty) user list
    @PostMapping("/users/delete-all")
    public String deleteAllUsers() {
        adminService.deleteAllUsers();
        return "redirect:/admin/users";
    }
}
