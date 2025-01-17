package com.zyphenvisuals.tweeter.components;

import com.zyphenvisuals.tweeter.background.DateUpdater;
import com.zyphenvisuals.tweeter.model.TweetModel;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Tweet extends Pane {

    @FXML
    private Text username;

    @FXML
    private Text text;

    @FXML
    private Text date;

    private ZonedDateTime createdAt;

    public Tweet(TweetModel data) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tweet.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();

            initializeData(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFormattedDate(ZonedDateTime date) {
        String format;

        if(date.isAfter(ZonedDateTime.now().minusMinutes(1))){
            // within the last minute
            Duration duration = Duration.between(date, ZonedDateTime.now());
            format = duration.toSeconds() + "s";
        } else if(date.isAfter(ZonedDateTime.now().minusHours(1))){
            // within the last hour
            Duration duration = Duration.between(date, ZonedDateTime.now());
            format = duration.toMinutes() + "m";
        } else if(date.isAfter(ZonedDateTime.now().minusDays(1))){
            // within the last day
            Duration duration = Duration.between(date, ZonedDateTime.now());
            format = duration.toHours() + "h";
        } else if(date.getYear() == ZonedDateTime.now().getYear()){
            // within the same year
            String dayOfMonth = String.valueOf(date.getDayOfMonth());
            String month = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault());
            format = dayOfMonth + " " + month;
        } else {
            // older than a year
            String dayOfMonth = String.valueOf(date.getDayOfMonth());
            String month = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault());
            String year = String.valueOf(date.getYear());
            format = dayOfMonth + " " + month + " " + year;
        }

        return format;
    }

    private void updateDate(){
        date.setText(getFormattedDate(createdAt));
    }

    private void initializeData(TweetModel data){
        // set username
        username.setText("@"+data.getCreator().getUsername());

        // set text
        text.setText(data.getText());

        // set date
        // handle null (this happens when you post a tweet)
        Timestamp created = data.getCreated();
        if(created == null){
            created = new Timestamp(System.currentTimeMillis());
        }
        createdAt = created.toLocalDateTime().atZone(ZoneId.systemDefault());
        date.setText(getFormattedDate(createdAt));

        // submit the date for updates
        Runnable updateMethod = this::updateDate;
        DateUpdater.addUpdateTask(updateMethod);
    }
}
