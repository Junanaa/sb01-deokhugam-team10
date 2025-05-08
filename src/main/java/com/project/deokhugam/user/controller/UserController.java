package com.project.deokhugam.user.controller;

import com.project.deokhugam.global.response.CustomApiResponse;
import com.project.deokhugam.user.dto.UserLoginRequest;
import com.project.deokhugam.user.dto.UserRegisterRequest;
import com.project.deokhugam.user.dto.UserResponse;
import com.project.deokhugam.user.dto.UserUpdateRequest;
import com.project.deokhugam.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid  @RequestBody UserRegisterRequest userRegisterRequest
    ) {
        UserResponse userResponse = userService.createUser(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody UserLoginRequest request
    ) {
        UserResponse userResponse = userService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable UUID userId
    ) {
        UserResponse userResponse = userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }


    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UserUpdateRequest request
            ) {
        UserResponse userResponse = userService.updateUser(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }



    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{userId}/hard")
    public ResponseEntity<Void> deleteUserPermanently(
            @PathVariable UUID userId
    ) {
        userService.deleteUserPermanently(userId);
        return ResponseEntity.noContent().build();
    }
}
