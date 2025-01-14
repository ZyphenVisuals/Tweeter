package com.zyphenvisuals.tweeter.views;

import com.zyphenvisuals.tweeter.model.LoginRequest;
import com.zyphenvisuals.tweeter.network.NetworkController;
import com.zyphenvisuals.tweeter.router.RouterController;
import com.zyphenvisuals.tweeter.router.RouterPath;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {
    private final Validator validator = new Validator();

    public TextField usernameInput;
    public PasswordField passwordInput;
    public Button loginButton;

    public void goToRegister(ActionEvent actionEvent) {
            RouterController.goTo(actionEvent, RouterPath.REGISTER);
    }

    public void login(ActionEvent actionEvent) {
        // get the form data
        String username = usernameInput.getText();
        String password = passwordInput.getText();

        // TODO proper logging
        System.out.println("Logging in user " + username);

        String res = NetworkController.sendPostRequest("/user/login", new LoginRequest(username, password));

        System.out.println(res);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        validator.createCheck()
                .dependsOn("username", usernameInput.textProperty())
                .withMethod(c -> {
                    String userName = c.get("username");
                    if (userName.isEmpty()) {
                        c.error("Empty username not allowed.");
                    }
                })
                .immediate();

        validator.createCheck()
                .dependsOn("password", passwordInput.textProperty())
                .withMethod(c -> {
                    String passwordText = c.get("password");
                    if (passwordText.isEmpty() ) {
                        c.error("Empty password not allowed.");
                    }
                })
                .immediate();

        // wrap the login button
        Node parentOfButton = loginButton.getParent();
        TooltipWrapper<Button> signUpWrapper = new TooltipWrapper<>(
                loginButton,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot sign up:\n", validator.createStringBinding())
        );
        ((HBox)parentOfButton).getChildren().add(signUpWrapper);
    }
}
