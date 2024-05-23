package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping
    public String getAdminPage(Model model) {
        model.addAttribute("users", userDetailsServiceImpl.findAllUsers());
        return "admin-page";
    }

    @GetMapping("/allUsers")
    @ResponseBody
    public List<User> getAllUsers() {
        return userDetailsServiceImpl.findAllUsers().stream().toList();
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        userDetailsServiceImpl.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        userDetailsServiceImpl.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userDetailsServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }
}