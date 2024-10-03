package com.ptit.hirex.security.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ptit.data.base.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;
}
