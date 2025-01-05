package com.aws.rest.api.service;

import com.aws.rest.api.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final Map<Long, User> userStore = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<User> getAllUsers() {
        return new ArrayList<>(userStore.values());
    }

    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }

    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setId(idCounter.getAndIncrement());
        }
        userStore.put(user.getId(), user);
        return user;
    }

    public void deleteUser(Long id) {
        userStore.remove(id);
    }
}

