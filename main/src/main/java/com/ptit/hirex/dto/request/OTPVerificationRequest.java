package com.ptit.hirex.dto.request;

import lombok.Data;

@Data
public class OTPVerificationRequest {
    private String email;
    private String otp;
}
