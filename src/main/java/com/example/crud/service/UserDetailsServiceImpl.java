package com.example.crud.service;

import com.example.crud.model.User;
import com.example.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        } else {
            return user.get();
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow();
    }

    public boolean saveUser(User user) {
        Optional<User> optionalUser = userRepository.findByName(user.getName());
        if (optionalUser.isPresent()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public void updateUser(User updatedUser) {
        User newUser = userRepository.findById(updatedUser.getId()).get();
        newUser.setName(updatedUser.getName());
        newUser.setLastName(updatedUser.getLastName());
        newUser.setAge(updatedUser.getAge());
        newUser.setEmail(updatedUser.getEmail());
        newUser.setRoles(updatedUser.getRoles());
        userRepository.save(newUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
