package com.zyphenvisuals.tweeter.router;

import com.zyphenvisuals.tweeter.TweeterLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.Objects;

public class RouterController {
    @Getter
    private static RouterPath currentPath;

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
            case HOME -> {
                fxmlFilename = "views/home.fxml";
            }
            case SEARCH -> {
                fxmlFilename = "views/search.fxml";
            }
            case SETTINGS -> {
                fxmlFilename = "views/settings.fxml";
            }
            default -> throw new IllegalStateException("Unexpected value: " + path);
        }

        // get the stage - horribly cursed code from https://www.youtube.com/watch?v=hcM-R-YOKkQ
        Scene scene = ((Node)event.getSource()).getScene();
        Stage stage = (Stage)scene.getWindow();

        // save stage size
        double width = stage.getWidth();
        double height = stage.getHeight();

        // set the path
        RouterPath oldPath = currentPath;
        currentPath = path;

        try {
            // load the scene
            Scene newScene = new Scene(FXMLLoader.load(Objects.requireNonNull(TweeterLauncher.class.getResource(fxmlFilename))));

            // set it
            stage.setScene(newScene);

            // restore stage size
            stage.setWidth(width);
            stage.setHeight(height);

            stage.show();
        } catch (IOException e) {
            currentPath = oldPath;
            System.err.println("Failed to load fxml file: " + fxmlFilename);
            e.printStackTrace();
        }
    }
}
