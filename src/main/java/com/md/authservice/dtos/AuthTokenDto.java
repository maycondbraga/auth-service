package com.md.authservice.dtos;

public class AuthTokenDto {
    private String token;

    private Long expiresIn;

    public String getToken() {
        return token;
    }

    public AuthTokenDto setToken(String token) {
        this.token = token;
        return this;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public AuthTokenDto setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}
