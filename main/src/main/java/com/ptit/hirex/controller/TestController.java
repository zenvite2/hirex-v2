package com.ptit.hirex.controller;

import com.ptit.hirex.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<?>> test() {
        return ResponseEntity.ok(ApiResponse.builder().code(200).message("ok dc r").build());
    }
}
