package com.aws.rest.api.controller;

import com.aws.rest.api.entity.User;
import com.aws.rest.api.exceptions.UnAutherizedException;
import com.aws.rest.api.jwt.JwtUtil;
import com.aws.rest.api.model.UserRequest;
import com.aws.rest.api.service.DbUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<Optional<List<User>>> getAllUsers(@RequestHeader("Authorization") String authHeader) throws Exception {
        log.info("Getting all db users >>>: ");

        // Check if the Authorization header is missing or doesn't contain Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            throw new UnAutherizedException("UNAUTHORIZED",new Exception("Authorization error"));
        }
        // Extract token from header
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);

        // Validate token
        if (!jwtUtil.validateToken(token,username)) {
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            throw new UnAutherizedException("Invalid token",new Exception("UNAUTHORIZED"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(dbUserService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws Exception {
        log.info("Getting user by id >>>:");
        return dbUserService.getUser(id)
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
        if (dbUserService.getUser(id).isPresent()) {
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
