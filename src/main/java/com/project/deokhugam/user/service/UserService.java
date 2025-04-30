package com.project.deokhugam.user.service;

import com.project.deokhugam.global.exception.CustomException;
import com.project.deokhugam.global.exception.ErrorCode;
import com.project.deokhugam.user.dto.UserLoginRequest;
import com.project.deokhugam.user.dto.UserRegisterRequest;
import com.project.deokhugam.user.dto.UserResponse;
import com.project.deokhugam.user.dto.UserUpdateRequest;
import com.project.deokhugam.user.entity.User;
import com.project.deokhugam.user.exception.EmailAlreadyExistsException;
import com.project.deokhugam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .email(userRegisterRequest.getEmail())
                .nickname(userRegisterRequest.getNickname())
                .password(userRegisterRequest.getPassword())
                .build();

        user = userRepository.save(user);
        userRepository.flush(); // optional, 즉시 DB에 반영

        // 다시 조회해서 createdAt 채움
        user = userRepository.findByUserIdAndIsDeletedFalse(user.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        return UserResponse.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }


    @Transactional
    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findByUserIdAndIsDeletedFalse(userId).orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        return UserResponse.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();

    }



    @Transactional
    public UserResponse updateUser(UUID userId, UserUpdateRequest request) {

        User user = userRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));


        user.setNickname(request.getNickname());

        return UserResponse.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }


    @Transactional(readOnly = true)
    public UserResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        return UserResponse.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }


    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        user.setDeleted(true);
    }


    @Transactional
    public void deleteUserPermanently(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        userRepository.delete(user);
    }
}
