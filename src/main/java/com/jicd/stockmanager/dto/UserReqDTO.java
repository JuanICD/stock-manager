package com.jicd.stockmanager.dto;

public record UserReqDTO(
        String username,
        String email,
        String password,
        String role
) {
}

