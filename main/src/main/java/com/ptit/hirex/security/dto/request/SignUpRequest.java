package com.ptit.hirex.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank", groups = {SignUpRequest.Register.class})
    private String password;

    @NotNull(message = "No role is specified", groups = {SignUpRequest.Register.class})
    private Long roleId;

    public interface Validate{};

    public interface Register{};
}
