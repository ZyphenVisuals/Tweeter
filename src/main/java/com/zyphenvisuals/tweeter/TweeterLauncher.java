package com.zyphenvisuals.tweeter;

import atlantafx.base.theme.PrimerDark;

import com.zyphenvisuals.tweeter.background.DateUpdater;
import com.zyphenvisuals.tweeter.background.TokenUpdater;
import com.zyphenvisuals.tweeter.router.RouterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TweeterLauncher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // set up the application
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        // set up the stage
        stage.setMinHeight(540);
        stage.setMinWidth(960);
        stage.setTitle("Tweeter");
        RouterController.setStage(stage);

        // set up the initial scene
        FXMLLoader fxmlLoader = new FXMLLoader(TweeterLauncher.class.getResource("views/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);

        // show the stage
        stage.show();
    }

    public static void main(String[] args) {
        // TODO proper logging
        System.out.println("Starting app with java version: "+System.getProperty("java.version") + ", javafx.version: " + System.getProperty("javafx.version"));

        // start background threads
        DateUpdater.start();
        TokenUpdater.start();

        // launch app
        launch();

        // stop background threads
        DateUpdater.stop();
        TokenUpdater.stop();
    }
}