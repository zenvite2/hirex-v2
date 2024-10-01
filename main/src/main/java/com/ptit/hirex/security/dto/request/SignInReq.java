package com.ptit.hirex.security.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInReq {
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Min(value = 1, message = "You must enter role's Id")
    private Long roleId;
}
