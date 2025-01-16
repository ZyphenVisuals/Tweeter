package com.zyphenvisuals.tweeter.views;

import atlantafx.base.controls.Message;
import com.google.gson.Gson;
import com.zyphenvisuals.tweeter.model.AuthToken;
import com.zyphenvisuals.tweeter.model.LoginRequest;
import com.zyphenvisuals.tweeter.network.NetworkController;
import com.zyphenvisuals.tweeter.router.RouterController;
import com.zyphenvisuals.tweeter.router.RouterPath;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class Login implements Initializable {
    private final Validator validator = new Validator();
    private final Gson gson = new Gson();

    public TextField usernameInput;
    public PasswordField passwordInput;
    public Button loginButton;
    public Message errorMessage;

    public void goToRegister() {
            RouterController.goTo(RouterPath.REGISTER);
    }

    private void hideMessages(){
        errorMessage.setVisible(false);
        errorMessage.setManaged(false);
    }

    public void login() {
        // hide messages
        hideMessages();

        // get the form data
        String username = usernameInput.getText();
        String password = passwordInput.getText();

        // TODO proper logging
        System.out.println("Logging in user " + username);

        HttpResponse<String> res = NetworkController.sendPostRequest("/user/login", new LoginRequest(username, password));

        if(res.statusCode() == 200) {
            // auth success
            // TODO proper logging
            System.out.println("Token received");

            // deserialize
            AuthToken token = gson.fromJson(res.body(), AuthToken.class);

            // set the token
            NetworkController.setToken(token.getToken());

            // go home
            RouterController.goTo(RouterPath.HOME);
        } else if(res.statusCode() == 403) {
            // auth failure
            errorMessage.setVisible(true);
            errorMessage.setManaged(true);
            errorMessage.setDescription("Incorrect username or password.");
        } else {
            // unexpected error
            errorMessage.setVisible(true);
            errorMessage.setManaged(true);
            errorMessage.setDescription("An unexpected error occurred.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize validators
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
                Bindings.concat("Cannot log in:\n", validator.createStringBinding())
        );
        ((HBox)parentOfButton).getChildren().add(signUpWrapper);

        // hide warning
        hideMessages();
    }
}
