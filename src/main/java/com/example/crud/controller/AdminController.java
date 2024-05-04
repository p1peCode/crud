package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.repository.RoleRepository;
import com.example.crud.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(value = "/info")
    public String adminInfoPage(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user-page";
    }

    @GetMapping
    public String getAdminPage(Model model) {
        model.addAttribute("users", userDetailsServiceImpl.findAllUsers());
        return "admin-page";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("allRoles", roleRepository.findAll());
        return "new-user-page";
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute("user") User user) {
        userDetailsServiceImpl.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        userDetailsServiceImpl.findUserById(id).ifPresent(o -> model.addAttribute("user", o));
        model.addAttribute("allRoles", roleRepository.findAll());
        return "edit-user-page";
    }

    @PostMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user) {
        userDetailsServiceImpl.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userDetailsServiceImpl.delete(id);
        return "redirect:/admin";
    }
}
