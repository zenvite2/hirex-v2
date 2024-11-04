package com.ptit.hirex.controller;

import com.ptit.hirex.dto.UserInfoDto;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-info")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserInfoDto>> getUserInfo(@PathVariable Long userId) {
        return userInfoService.getUserInfoById(userId);
    }
}
