package com.aws.rest.api.service;

import com.aws.rest.api.entity.User;
import com.aws.rest.api.interfaces.UserInterface;
import com.aws.rest.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DBUserService implements UserInterface {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getUser(Long uid) throws Exception {
        return userRepository.findById(uid);
    }

    @Override
    public Optional<List<User>> getAllUsers() throws Exception {
        return Optional.of(userRepository.findAll());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<User> saveUser(User userEntity) throws Exception {
        return Optional.of(userRepository.save(userEntity));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<User> updateUser(Long uid, User userEntity) throws Exception {
        return userRepository.findById(uid).map(existingUser -> {
                existingUser.setUserId(userEntity.getUserId());
                existingUser.setUsername(userEntity.getUsername());
                existingUser.setLastName(userEntity.getLastName());
                existingUser.setCntpyNum(userEntity.getCntpyNum());
                // Update other fields as necessary
                return userRepository.save(existingUser);
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteUser(Long uid) throws Exception {
        userRepository.deleteById(uid);
    }
}
