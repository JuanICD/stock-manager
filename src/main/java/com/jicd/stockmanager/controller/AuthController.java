package com.jicd.stockmanager.controller;

import com.jicd.stockmanager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/refresh-token")
    public String refreshToken(){
        return "refresh-token";
    }
}
