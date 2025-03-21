package com.aws.rest.api.service;

import com.aws.rest.api.model.User;
import com.aws.rest.api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.aws.rest.api.entity.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password("{noop}"+user.getPassword())
                .roles(user.getRole())
                .build();
    }


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

