package com.example.crud.dto;

import com.example.crud.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String lastName;
    private int age;
    private String email;
    private String password;
    private Set<Role> roles;
}
