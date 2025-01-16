package com.zyphenvisuals.tweeter.components;

import com.zyphenvisuals.tweeter.network.NetworkController;
import com.zyphenvisuals.tweeter.router.RouterController;
import com.zyphenvisuals.tweeter.router.RouterPath;
import javafx.event.ActionEvent;
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

    public void goToHome(ActionEvent actionEvent) {
        RouterController.goTo(actionEvent, RouterPath.HOME);
    }

    public void goToSearch(ActionEvent actionEvent) {
        RouterController.goTo(actionEvent, RouterPath.SEARCH);
    }

    public void goToSettings(ActionEvent actionEvent) {
        RouterController.goTo(actionEvent, RouterPath.SETTINGS);
    }

    public void goToLogin(ActionEvent actionEvent) {
        NetworkController.resetToken();
        RouterController.goTo(actionEvent, RouterPath.LOGIN);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    }
}
