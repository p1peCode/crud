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

import java.util.Collections;

@Controller
@RequestMapping()
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(value = "/")
    public String homePage() {
        return "home-page";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register-page";
    }

    @PostMapping()
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register-page";
        }
        user.setRoles(Collections.singleton(roleRepository.findByRole("ROLE_USER")));
        if (!userDetailsServiceImpl.saveUser(user)) {
            model.addAttribute("NameError", "Пользователь с таким именем уже существует");
            return "register-page";
        }
        return "redirect:/";
    }

    @GetMapping()
    public String userPage(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user-page";
    }
}
