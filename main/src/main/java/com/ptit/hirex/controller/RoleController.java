package com.ptit.hirex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

    @PostMapping("/select")
    public ResponseEntity<String> selectRole(@RequestParam("roleId") int roleId) {
        String redirectUrl;
        if (roleId == 1) {
            redirectUrl = "/employee/create";
        } else if (roleId == 2) {
            redirectUrl = "/employer/create";
        } else {
            return ResponseEntity.badRequest().body("Invalid role ID");
        }
        return ResponseEntity.ok(redirectUrl);
    }
}
