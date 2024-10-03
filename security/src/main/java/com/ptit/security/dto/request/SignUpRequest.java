package com.ptit.security.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
