package com.zyphenvisuals.tweeter.views;

import com.zyphenvisuals.tweeter.router.RouterController;
import com.zyphenvisuals.tweeter.router.RouterPath;
import javafx.event.ActionEvent;

public class Register {
    public void goToLogin(ActionEvent actionEvent) {
        RouterController.goTo(actionEvent, RouterPath.LOGIN);
    }
}
