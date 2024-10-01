package com.ptit.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoResponse {
    private String id;
    private String email;
//    private LoginTypeEnum loginType;
    private String language;
}
