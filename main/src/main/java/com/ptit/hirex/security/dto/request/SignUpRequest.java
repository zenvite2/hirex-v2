package com.ptit.hirex.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignUpRequest {

    private String email;

    private String userName;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
