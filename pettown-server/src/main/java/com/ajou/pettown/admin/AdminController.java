package com.ajou.pettown.admin;

// Thymeleaf web controller for the admin user management pages.
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
