package com.zyphenvisuals.tweeter.views;

import atlantafx.base.controls.Message;
import com.zyphenvisuals.tweeter.model.AuthToken;
import com.zyphenvisuals.tweeter.model.RegisterRequest;
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

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class Register implements Initializable {
    private static final Validator validator = new Validator();

    public Message errorMessage;
    public PasswordField passwordConfirmationInput;
    public PasswordField passwordInput;
    public TextField usernameInput;
    public Button registerButton;
    public Message successMessage;

    private void hideMessages(){
        errorMessage.setVisible(false);
        errorMessage.setManaged(false);
        successMessage.setVisible(false);
        successMessage.setManaged(false);
    }

    public void goToLogin(ActionEvent actionEvent) {
        RouterController.goTo(actionEvent, RouterPath.LOGIN);
    }

    public void register(ActionEvent actionEvent) {
        // hide messages
        hideMessages();

        // get the form data
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        String passwordConfirmation = passwordConfirmationInput.getText();

        // TODO proper logging
        System.out.println("Registering user " + username);

        HttpResponse<String> res = NetworkController.sendPostRequest("/user/register", new RegisterRequest(username, password, passwordConfirmation));

        if(res.statusCode() == 201) {
            // auth success
            // TODO proper logging
            System.out.println("Successfully registered.");

            // inform the user
            successMessage.setVisible(true);
            successMessage.setManaged(true);

        } else if(res.statusCode() == 400) {
            // auth failure
            errorMessage.setVisible(true);
            errorMessage.setManaged(true);
            errorMessage.setDescription("Username or password is invalid. How did you do this?");
        } else if(res.statusCode() == 409) {
            // unexpected error
            errorMessage.setVisible(true);
            errorMessage.setManaged(true);
            errorMessage.setDescription("Username is already taken.");
        } else {
            // unexpected error
            errorMessage.setVisible(true);
            errorMessage.setManaged(true);
            errorMessage.setDescription("An unexpected error occurred.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                    if (passwordText.length() < 8) {
                        c.error("Password must contain at least 8 characters.");
                    }
                })
                .immediate();

        validator.createCheck()
                .dependsOn("passwordConfirmation", passwordConfirmationInput.textProperty())
                .dependsOn("password", passwordInput.textProperty())
                .withMethod(c -> {
                    String passwordText = c.get("password");
                    String passwordConfirmationText = c.get("passwordConfirmation");
                    if (!passwordConfirmationText.equals(passwordText)) {
                        c.error("Passwords do not match.");
                    }
                })
                .immediate();

        // wrap the login button
        Node parentOfButton = registerButton.getParent();
        TooltipWrapper<Button> signUpWrapper = new TooltipWrapper<>(
                registerButton,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot sign up:\n", validator.createStringBinding())
        );
        ((HBox)parentOfButton).getChildren().add(signUpWrapper);

        // hide warning
        hideMessages();
    }
}
