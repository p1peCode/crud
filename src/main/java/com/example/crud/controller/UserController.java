package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.repository.RoleRepository;
import com.example.crud.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("")
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
    public String registerUser(@ModelAttribute("user") User user) {
        user.setRoles(Collections.singleton(roleRepository.findByRole("ROLE_USER")));
        userDetailsServiceImpl.saveUser(user);
        return "redirect:/";
    }

    @GetMapping(value = "/user")
    public String userPage(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user-page";
    }
}
