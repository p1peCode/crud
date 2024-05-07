package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.repository.RoleRepository;
import com.example.crud.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "new-user-page";
        }
        if (!userDetailsServiceImpl.saveUser(user)) {
            model.addAttribute("NameError", "Пользователь с таким именем уже существует");
            return "new-user-page";
        }
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userDetailsServiceImpl.findUserById(id));
        model.addAttribute("allRoles", roleRepository.findAll());
        return "edit-user-page";
    }

    @PostMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model, Authentication authentication) {
        if (bindingResult.hasErrors() && !bindingResult.hasFieldErrors("password")) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "edit-user-page";
        }
        userDetailsServiceImpl.updateUser(user);
        if (userDetailsServiceImpl.isUser(user, authentication)) {
            return "redirect:/login";
        }
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userDetailsServiceImpl.delete(id);
        return "redirect:/admin";
    }
}
