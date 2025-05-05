package com.aws.rest.api.interfaces;


import com.aws.rest.api.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserInterface {

    Optional<User> getUser(Long uid) throws Exception;

    Optional<List<User>> getAllUsers() throws Exception;

    Optional<User> saveUser(User userEntity) throws Exception;

    Optional<User> updateUser(Long uid, User userEntity) throws Exception;

    void deleteUser(Long uid) throws Exception;
}
