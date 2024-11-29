package com.ptit.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String avatar;
}