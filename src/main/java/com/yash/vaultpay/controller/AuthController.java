package com.yash.vaultpay.controller;

import com.yash.vaultpay.dto.RegisterRequest;
import com.yash.vaultpay.model.User;
import com.yash.vaultpay.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request);
            return ResponseEntity.ok("User registered successfully! ID: " + user.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}