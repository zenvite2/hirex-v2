package com.ptit.hirex.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInReq {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}