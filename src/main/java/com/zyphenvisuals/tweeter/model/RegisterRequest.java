package com.zyphenvisuals.tweeter.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String password_confirmation;
}
