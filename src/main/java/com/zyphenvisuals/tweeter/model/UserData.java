package com.zyphenvisuals.tweeter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class UserData {
    private String username;
    private Timestamp created;
}
