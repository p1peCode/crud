package com.example.crud.dto;

import com.example.crud.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String lastName;
    private int age;
    private String address;
    private boolean humidity;
    private String email;
    private String password;
    private Set<Role> roles;
}