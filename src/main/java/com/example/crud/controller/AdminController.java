package com.example.crud.controller;

import com.example.crud.dto.UserDTO;
import com.example.crud.mapper.UserMapper;
import com.example.crud.model.User;
import com.example.crud.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userDetailsServiceImpl.findUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/allUsers")
    @ResponseBody
    public List<UserDTO> getAllUsers() {
        return userDetailsServiceImpl.findAllUsers().stream().map(UserMapper::entityToDTO).toList();
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        User user = UserMapper.DTOToEntity(userDTO);
        boolean isSaved = userDetailsServiceImpl.saveUser(user);
        if (!isSaved) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        userDetailsServiceImpl.updateUser(UserMapper.DTOToEntity(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userDetailsServiceImpl.delete(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
}