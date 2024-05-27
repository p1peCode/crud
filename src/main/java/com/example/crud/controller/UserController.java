package com.example.crud.controller;

import com.example.crud.dto.UserDTO;
import com.example.crud.mapper.UserMapper;
import com.example.crud.model.User;
import com.example.crud.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping()
    public String userPage(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user-page";
    }

    @ResponseBody
    @GetMapping("/me")
    public UserDTO thisUser(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        User user = userDetailsServiceImpl.findUserById(principal.getId());
        return UserMapper.entityToDTO(user);
    }
}
