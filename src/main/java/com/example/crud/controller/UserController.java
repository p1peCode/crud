package com.example.crud.controller;

import com.example.crud.dto.UserDTO;
import com.example.crud.model.User;
import com.example.crud.service.GeoCodeService;
import com.example.crud.service.UserDetailsServiceImpl;
import com.example.crud.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private GeoCodeService geoCodeService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping()
    public String userPage(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user-page";
    }

    @ResponseBody
    @GetMapping("/me")
    public UserDTO thisUser(Authentication authentication) throws JsonProcessingException {
        User principal = (User) authentication.getPrincipal();
        User user = userDetailsServiceImpl.findUserById(principal.getId());
        String coordinates = geoCodeService.getCoordinates(user.getAddress());
        boolean humidity = weatherService.isHumidity(coordinates);
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .address(user.getAddress())
                .humidity(humidity)
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}