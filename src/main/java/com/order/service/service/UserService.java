package com.order.service.service;

import com.order.service.exception.GenericException;
import com.order.service.model.User;
import com.order.service.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new GenericException(HttpStatus.ALREADY_REPORTED.value(), "Username already exists");
        }
        user.setId(UUID.randomUUID().toString());
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public User getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND.value(), "User not found"));
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND.value(), "User not found"));
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public void deleteUser(String id) {
        repository.deleteById(id);
    }

    public boolean matches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
