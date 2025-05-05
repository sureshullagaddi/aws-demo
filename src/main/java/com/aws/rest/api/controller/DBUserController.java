package com.aws.rest.api.controller;

import com.aws.rest.api.entity.User;
import com.aws.rest.api.jwt.JwtUtil;
import com.aws.rest.api.model.UserRequest;
import com.aws.rest.api.service.DBUserService;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class DBUserController {

    private final DBUserService userService;

    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<Optional<List<User>>> getAllUsers(@RequestHeader("Authorization") String authHeader) throws Exception {
        log.info("Getting all users >>>: ");
        // Check if the Authorization header is missing or doesn't contain Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // Extract token
        String token = authHeader.substring(7);
        // Validate token
        if (!jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable Long id) throws Exception {
        log.info("Getting user by id >>>:");
        if(id == null || id > 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        // Check if the Authorization header is missing or doesn't contain Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // Extract token
        String token = authHeader.substring(7);
        // Validate token
        if (!jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return userService.getUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Optional<User>> createUser(@RequestHeader("Authorization") String authHeader,
                                                     @RequestBody @Valid UserRequest userRequest) {
        log.info("Creating user >>>: ");
        try {
            if(userRequest == null || userRequest.toString().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            // Check if the Authorization header is missing or doesn't contain Bearer token
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            // Extract token
            String token = authHeader.substring(7);
            // Validate token
            if (!jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Validate and convert the request
            User userEntity = convertToUserEntity(userRequest);
            // Save to the database
            Optional<User> savedUser = userService.saveUser(userEntity);
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

    @PutMapping("/{id}")
    public ResponseEntity<Optional<User>> updateUser(@RequestHeader("Authorization") String authHeader,
                                                     @PathVariable Long id,
                                                     @RequestBody @Valid UserRequest userRequest) throws Exception {
        log.info("Updating user");

        if(userRequest == null || userRequest.toString().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Check if the Authorization header is missing or doesn't contain Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // Extract token
        String token = authHeader.substring(7);
        // Validate token
        if (!jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // Validate and convert the request
        User userEntity = updateUserEntity(userRequest);
        Optional<User> user = userService.updateUser(id,userEntity);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable Long id) throws Exception {
        log.info("Deleting user by id >>>: ");

        if(id == null || id > 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Check if the Authorization header is missing or doesn't contain Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // Extract token
        String token = authHeader.substring(7);
        // Validate token
        if (!jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (userService.getUser(id).isPresent()) {
            userService.deleteUser(id);
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

    private User updateUserEntity(UserRequest userRequest){
        log.info("converting user to update");
        return User.builder()
                .userId(Long.valueOf(userRequest.getUid()))
                .username(userRequest.getFirst_name())
                .lastName(userRequest.getLast_name())
                .cntpyNum(userRequest.getCntpy_num()).build();
    }
}
