package com.azki.example.reservation.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.security")
public record SecurityProperties(String secret, long accessExp, long refreshExp, AdminAreaProperties[] admins) {

    public record AdminAreaProperties(boolean securityEnabled, String username, String password, String[] roles) {
    }
}
