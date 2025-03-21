package com.aws.rest.api.controller;

import com.aws.rest.api.entity.User;
import com.aws.rest.api.model.UserRequest;
import com.aws.rest.api.service.DbUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/db-users")
@RequiredArgsConstructor
public class DBUserController {

    private final DbUserService dbUserService;

    @GetMapping
    public Optional<List<User>> getAllUsers() throws Exception {
        log.info("Getting all db users >>>: ");
        return dbUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws Exception {
        log.info("Getting user by id >>>:");
        return dbUserService.getSingleUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Optional<User>> createUser(@RequestBody @Valid UserRequest userRequest) {
        log.info("Creating user >>>: ");
        try {
            // Validate and convert the request
            User userEnt = convertToUserEntity(userRequest);
            // Save to the database
            Optional<User> savedUser = dbUserService.saveUser(userEnt);
            // Return ResponseEntity with proper status code
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IllegalArgumentException ex) {
            // Handle validation-related exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            // General error handling (should be used cautiously)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws Exception {
        log.info("Deleting user by id >>>: ");
        if (dbUserService.getSingleUser(id).isPresent()) {
            dbUserService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private User convertToUserEntity(UserRequest userRequest) {
        log.info("Converting user request to user entity >>>: ");
        return User.builder()
                .uid(userRequest.getUid())
                .cntpyNum(userRequest.getCntpy_num())
                .username(userRequest.getFirst_name())
                .lastName(userRequest.getLast_name())
                .build();
    }
}
