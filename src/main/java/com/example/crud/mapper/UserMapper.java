package com.example.crud.mapper;

import com.example.crud.dto.UserDTO;
import com.example.crud.model.Role;
import com.example.crud.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO entityToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .email(user.getEmail())
                .address(user.getAddress())
                .roles(user.getRoles())
                .build();
    }

    public static User DTOToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setAge(userDTO.getAge());
        user.setAddress(userDTO.getAddress());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRoles(rolesMapper(userDTO.getRoles()));
        return user;
    }

    private static Set<Role> rolesMapper(Set<Role> roleSet) {
        return roleSet.stream().peek(role -> {
            switch (role.getRole()) {
                case "ROLE_ADMIN":
                    role.setId(1L);
                    break;
                case "ROLE_USER":
                    role.setId(2L);
                    break;
            }
        }).collect(Collectors.toSet());
    }
}