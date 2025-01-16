package com.zyphenvisuals.tweeter.router;

import com.zyphenvisuals.tweeter.TweeterLauncher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;

public class RouterController {
    @Getter
    private static RouterPath currentPath;

    @Setter
    private static Stage stage;

    public static void goTo(RouterPath path) {
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
