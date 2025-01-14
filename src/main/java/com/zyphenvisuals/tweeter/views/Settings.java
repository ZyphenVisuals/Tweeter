package com.zyphenvisuals.tweeter.views;

import com.zyphenvisuals.tweeter.network.NetworkController;
import com.zyphenvisuals.tweeter.router.RouterController;
import com.zyphenvisuals.tweeter.router.RouterPath;
import javafx.event.ActionEvent;

public class Settings {
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
}
