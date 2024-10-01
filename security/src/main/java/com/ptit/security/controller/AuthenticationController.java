package com.ptit.security.controller;

import com.ptit.data.base.User;
import com.ptit.security.dto.request.RefreshTokenRequest;
import com.ptit.security.dto.request.SignInRequest;
import com.ptit.security.dto.request.SignUpRequest;
import com.ptit.security.dto.response.RegisterResponse;
import com.ptit.security.dto.response.TokenResponse;
import com.ptit.security.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.OK;


@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/access")
    public ResponseEntity<TokenResponse> login(@RequestBody SignInRequest request) {
        return new ResponseEntity<>(authenticationService.authenticate(request), OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ResponseEntity<>(authenticationService.refreshToken(refreshTokenRequest), OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(@Validated @RequestBody SignUpRequest signInRequest) {

        System.out.println("SignInRequest: " + signInRequest.toString());
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            User user = authenticationService.createUser(signInRequest);
            registerResponse.setUser(user);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(registerResponse);
        }

    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        return new ResponseEntity<>(authenticationService.logout(request), OK);
//    }
}
