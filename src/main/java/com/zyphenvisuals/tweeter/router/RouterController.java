package com.zyphenvisuals.tweeter.router;

import com.zyphenvisuals.tweeter.TweeterLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RouterController {
    public static void goTo(ActionEvent event, RouterPath path) {
        // TODO proper logging
        System.out.println("Going to " + path);

        // figure out where to go
        String fxmlFilename;
        switch (path) {
            case LOGIN -> {
                fxmlFilename = "views/login.fxml";
            }
            case REGISTER -> {
                fxmlFilename = "views/register.fxml";
            }
            default -> throw new IllegalStateException("Unexpected value: " + path);
        }

        // get the stage - horribly cursed code from https://www.youtube.com/watch?v=hcM-R-YOKkQ
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        // load the scene
        try {
            Scene newScene = new Scene(FXMLLoader.load(Objects.requireNonNull(TweeterLauncher.class.getResource(fxmlFilename))));

            // set it
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load fxml file: " + fxmlFilename);
        }
    }
}
