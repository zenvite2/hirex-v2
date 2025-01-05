package com.ptit.hirex.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {
    @NotEmpty(message = "Username is required", groups = {ForgotPasswordRequest.Create.class})
    private String username;

    @NotEmpty(message = "New password is required", groups = {ForgotPasswordRequest.Apply.class})
    private String newPassword;

    @NotEmpty(message = "Token is required", groups = {ForgotPasswordRequest.Apply.class})
    private String token;

    public interface Create{};

    public interface Apply{};
}
