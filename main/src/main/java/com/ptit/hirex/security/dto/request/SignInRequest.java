package com.ptit.hirex.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SignInRequest {

    @NotEmpty(message = "username must be not null")
    private String email;

    @NotBlank(message = "username must be not blank")
    private String password;
}
