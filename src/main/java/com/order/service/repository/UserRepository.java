package com.order.service.repository;

import com.order.service.exception.GenericException;
import com.order.service.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class UserRepository {
    private final List<User> users = new CopyOnWriteArrayList<>();

    public User save(User user) {
        users.add(user);
        return user;
    }

    public Optional<User> findById(String id) {
        synchronized (users) {
            return users.stream()
                    .filter(u -> u.getId().equals(id))
                    .findFirst();
        }
    }

    public Optional<User> findByUsername(String username) {
        synchronized (users) {
            return users.stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst();
        }
    }

    public List<User> findAll() {
        synchronized (users) {
            return new ArrayList<>(users);
        }
    }

    public boolean existsByUsername(String username) {
        synchronized (users) {
            return users.stream()
                    .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
        }
    }

    public void deleteById(String id) {
        synchronized (users) {
            if (users.stream().noneMatch(user -> user.getId().equals(id))) {
                throw new GenericException(HttpStatus.NOT_FOUND.value(), "User not found");
            }
            users.removeIf(u -> u.getId().equals(id));
        }
    }
}
