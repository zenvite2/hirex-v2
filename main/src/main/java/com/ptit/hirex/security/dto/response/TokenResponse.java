package com.ptit.hirex.security.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private String accessToken;

    private String refreshToken;

    private String username;

    private String role;

    private Long userId;
}
