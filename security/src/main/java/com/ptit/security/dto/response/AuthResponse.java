package com.ptit.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String refreshToken;
    private String accessToken;
    private AccountInfoResponse accountInfo;
}
