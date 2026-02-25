package com.jicd.stockmanager.service;

import com.jicd.stockmanager.security.configs.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProperties jwtProperties;

    private String secretKey = jwtProperties.getSecretKey();
    private long expiration = jwtProperties.getExpiration();
    private long refreshTokenExpiration = jwtProperties.getRefreshToken().getExpiration();






}
