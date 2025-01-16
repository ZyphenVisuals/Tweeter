package com.zyphenvisuals.tweeter.model;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class TweetCreator {
    private String username;
    private Timestamp created;
}
