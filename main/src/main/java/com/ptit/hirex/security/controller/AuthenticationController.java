package com.ptit.hirex.security.controller;

import com.ptit.hirex.dto.request.ForgotPasswordRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.dto.request.ChangePasswordRequest;
import com.ptit.hirex.security.dto.request.SignInRequest;
import com.ptit.hirex.security.dto.request.SignUpRequest;
import com.ptit.hirex.security.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Object>> login(@Valid @RequestBody SignInRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<Object>> createUser(@Validated(SignUpRequest.Register.class) @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.createUser(signUpRequest);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDto<Object>> forgotPassword(@Validated(ForgotPasswordRequest.Create.class) @RequestBody ForgotPasswordRequest request) {
        return authenticationService.createForgotPasswordRequest(request);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseDto<Object>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return authenticationService.changePassword(request);
    }

    @PostMapping("/apply-forgot-password")
    public ResponseEntity<?> applyForgotPassword(@Validated(ForgotPasswordRequest.Apply.class) @RequestBody ForgotPasswordRequest request) {
        authenticationService.applyForgotPassword(request);
        return ResponseEntity.ok("Forgot Password Applied");
    }

    @PostMapping("/validate-register")
    public ResponseEntity<?> validateRegister(@Validated(SignUpRequest.Validate.class) @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.validateRegisterRequest(signUpRequest);
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        return new ResponseEntity<>(authenticationService.logout(request), OK);
//    }
}
