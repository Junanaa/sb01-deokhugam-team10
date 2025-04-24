package com.project.deokhugam.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterRequest {

    @Email(message = "유효한 이메일 형식이여야 합니다.")
    @NotBlank(message = "이메일은 필수 입니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수 입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 10자 이하여야 합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이여야 합니다.")
    private String password;
}
