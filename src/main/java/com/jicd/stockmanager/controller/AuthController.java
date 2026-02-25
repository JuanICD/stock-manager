package com.jicd.stockmanager.controller;

import com.jicd.stockmanager.dto.AuthResDTO;
import com.jicd.stockmanager.dto.UserReqDTO;
import com.jicd.stockmanager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<AuthResDTO> login(@RequestBody UserReqDTO loginRequest) {
        return ResponseEntity.ok(service.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserReqDTO registerRequest) {
        return ResponseEntity.ok(service.register(registerRequest));
    }

    @PostMapping("/refresh-token")
    public String refreshToken() {
        return "refresh-token";
    }
}
