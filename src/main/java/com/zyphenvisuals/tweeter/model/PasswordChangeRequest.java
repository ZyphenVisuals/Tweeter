package com.zyphenvisuals.tweeter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordChangeRequest {
    private String old_password;
    private String new_password;
    private String new_password_confirmation;
}
