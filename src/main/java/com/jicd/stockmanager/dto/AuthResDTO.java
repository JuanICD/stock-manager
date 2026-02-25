package com.jicd.stockmanager.dto;

public record AuthResDTO(
        String token,
        String username,
        String role
) {
}
