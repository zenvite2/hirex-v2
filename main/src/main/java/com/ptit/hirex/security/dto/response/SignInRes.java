package com.ptit.hirex.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInRes {
    private String token;
    private String role;
    private Long id;
    private String fullname;
}