package com.jicd.stockmanager.dto;

public record UserResDTO(
        Long id,
        String username,
        String email,
        String password,
        String role
) {
}

