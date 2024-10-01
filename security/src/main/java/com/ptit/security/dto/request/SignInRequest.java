package com.ptit.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInRequest {

    @NotBlank(message = "username must be not null")
    private String email;

    @NotBlank(message = "username must be not blank")
    private String password;
}
