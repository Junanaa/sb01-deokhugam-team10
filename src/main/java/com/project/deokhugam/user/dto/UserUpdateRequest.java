package com.project.deokhugam.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @NotBlank(message = "닉네임은 필수 입니다.")
    private String nickname;
}
