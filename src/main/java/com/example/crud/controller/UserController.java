package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String allUsersForm(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "main";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("user", user);
        return "user-form";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/";
    }
}