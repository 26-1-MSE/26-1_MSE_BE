package com.ajou.pettown.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String root() {
        return "redirect:/admin/users";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/users")
    public String userList(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("users", adminService.getAllUsers(keyword));
        model.addAttribute("keyword", keyword);
        return "admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id,
                             @RequestParam(required = false) String keyword) {
        adminService.deleteUser(id);
        if (keyword != null && !keyword.isBlank()) {
            return "redirect:/admin/users?keyword=" + keyword;
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete-all")
    public String deleteAllUsers() {
        adminService.deleteAllUsers();
        return "redirect:/admin/users";
    }
}
