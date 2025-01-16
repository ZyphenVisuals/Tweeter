package com.zyphenvisuals.tweeter.components;

import com.zyphenvisuals.tweeter.background.DateUpdater;
import com.zyphenvisuals.tweeter.network.NetworkController;
import com.zyphenvisuals.tweeter.router.RouterController;
import com.zyphenvisuals.tweeter.router.RouterPath;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Sidebar implements Initializable {

    @FXML
    private Button homeButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button logoutButton;

    public void goToHome() {
        DateUpdater.reset();
        RouterController.goTo(RouterPath.HOME);
    }

    public void goToSearch() {
        DateUpdater.reset();
        RouterController.goTo(RouterPath.SEARCH);
    }

    public void goToSettings() {
        DateUpdater.reset();
        RouterController.goTo(RouterPath.SETTINGS);
    }

    public void goToLogin() {
        NetworkController.resetToken();
        DateUpdater.reset();
        RouterController.goTo(RouterPath.LOGIN);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // highlight path
        RouterPath currentPath = RouterController.getCurrentPath();
        switch (currentPath) {
            case HOME -> {
                homeButton.getStyleClass().add("accent");
            }
            case SEARCH -> {
                searchButton.getStyleClass().add("accent");
            }
            case SETTINGS -> {
                settingsButton.getStyleClass().add("accent");
            }
        }

        // add username to sign out
        String oldText = logoutButton.getText();
        logoutButton.setText(oldText + " (@" + NetworkController.getUsername() + ")");
    }
}
