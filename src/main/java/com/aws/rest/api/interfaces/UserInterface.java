package com.aws.rest.api.interfaces;


import com.aws.rest.api.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserInterface {

    Optional<UserEntity> getSingleUser(Long uid) throws Exception;

    Optional<List<UserEntity>> getAllUsers() throws Exception;

    Optional<UserEntity> saveUser(UserEntity userEntity) throws Exception;

    void deleteUser(Long uid) throws Exception;
}
