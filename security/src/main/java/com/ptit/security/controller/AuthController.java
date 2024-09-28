package com.ptit.security.controller;


import com.ptit.data.base.User;
import com.ptit.hirex.model.ApiResponse;
import com.ptit.security.dto.UserDto;
import com.ptit.security.dto.request.SignInReq;
import com.ptit.security.dto.response.SignInRes;
import com.ptit.security.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userServiceImpl;

//    @PostMapping("/sign-up")
//    public ResponseEntity<ApiResponse<?>> createUser(@Validated @RequestBody UserDto userDTO, BindingResult result) {
//
//        SignUpRes signUpRes = new SignUpRes();
//
//        if (result.hasErrors()) {
//            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
//            registerResponse.setMessage(errorMessages.toString());
//            return ResponseEntity.badRequest().body(registerResponse);
//        }
//
//        try {
//            User user = userServiceImpl.createUser(userDTO);
//            registerResponse.setMessage("Register success");
//            registerResponse.setUser(user);
//            return ResponseEntity.ok(registerResponse);
//        } catch (Exception e) {
//            registerResponse.setMessage(e.getMessage());
//            return ResponseEntity.badRequest().body(registerResponse);
//        }
//
//    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<?>> signIn(@Validated @RequestBody SignInReq signInReq) {
        try {
            SignInRes signInRes = userServiceImpl.login(signInReq.getPhoneNumber(),
                    signInReq.getPassword());
            return ResponseEntity.ok(ApiResponse.builder().code(200).message("Sign in successfully.").data(signInRes).build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(401).body(ApiResponse.builder().code(401).message("Sign in failed.").build());
        }
    }
}