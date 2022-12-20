package com.sparta.spartaposthomework.dto;

import com.sparta.spartaposthomework.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String username;
    private String password;

    private UserRoleEnum userRoleEnum;
}