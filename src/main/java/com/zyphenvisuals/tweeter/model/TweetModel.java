package com.zyphenvisuals.tweeter.model;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class TweetModel {
    private int id;
    private TweetCreator creator;
    private String text;
    private Timestamp created;
}
