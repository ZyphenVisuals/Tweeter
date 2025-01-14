package com.zyphenvisuals.tweeter;

import atlantafx.base.theme.PrimerDark;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TweeterLauncher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        FXMLLoader fxmlLoader = new FXMLLoader(TweeterLauncher.class.getResource("views/home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setMinHeight(540);
        stage.setMinWidth(960);
        stage.setTitle("Tweeter");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        // TODO proper logging
        System.out.println("Starting app with java version: "+System.getProperty("java.version") + ", javafx.version: " + System.getProperty("javafx.version"));
        launch();
    }
}