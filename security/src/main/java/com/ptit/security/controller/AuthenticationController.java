package com.ptit.security.controller;

import com.ptit.data.base.User;
import com.ptit.hirex.model.ResponseDto;
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
    public ResponseEntity<ResponseDto<Object>> login(@RequestBody SignInRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto<Object>> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authenticationService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<Object>> createUser(@Validated @RequestBody SignUpRequest signInRequest){

        return authenticationService.createUser(signInRequest);

    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        return new ResponseEntity<>(authenticationService.logout(request), OK);
//    }
}
