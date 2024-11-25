package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.EmailRequest;
import com.ptit.hirex.dto.request.OTPVerificationRequest;
import com.ptit.hirex.service.MailService;
import com.ptit.hirex.service.OTPService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;
    private final MailService mailService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateOTP(@RequestBody EmailRequest request) throws MessagingException {
        String otp = otpService.generateOTP(request.getEmail());
        mailService.sendOTPEmail(request.getEmail(), otp);
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOTP(@RequestBody OTPVerificationRequest request) {
        boolean isValid = otpService.validateOTP(request.getEmail(), request.getOtp());
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        }
        return ResponseEntity.badRequest().body("Invalid OTP");
    }
}
