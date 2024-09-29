package com.ptit.security.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInRes {
    private String token;
    private String role;
    private String id;
    private String fullname;
}