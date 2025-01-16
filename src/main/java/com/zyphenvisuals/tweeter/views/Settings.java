package com.zyphenvisuals.tweeter.views;

import atlantafx.base.controls.Message;
import com.zyphenvisuals.tweeter.model.PasswordChangeRequest;
import com.zyphenvisuals.tweeter.network.NetworkController;
import com.zyphenvisuals.tweeter.router.RouterController;
import com.zyphenvisuals.tweeter.router.RouterPath;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class Settings implements Initializable {

    private final Validator validator = new Validator();

    public Text usernameLabel;
    public PasswordField oldPasswordInput;
    public PasswordField newPasswordInput;
    public PasswordField newPasswordConfirmationInput;
    public Button resetButton;
    public Message successMessage;
    public Message errorMessage;

    private void hideMessages() {
        successMessage.setVisible(false);
        successMessage.setManaged(false);
        errorMessage.setVisible(false);
        errorMessage.setManaged(false);
    }

    public void resetPassword(ActionEvent actionEvent) {
        // hide messages
        hideMessages();

        // temporarily disable button
        resetButton.getParent().setDisable(true);

        // get data
        String old = oldPasswordInput.getText();
        String new1 = newPasswordInput.getText();
        String new2 = newPasswordConfirmationInput.getText();

        // send request
        HttpResponse<String> res = NetworkController.sendPostRequest("/user/change_password", new PasswordChangeRequest(old, new1, new2));

        if(res.statusCode() == 200) {
            // show success
            successMessage.setVisible(true);
            successMessage.setManaged(true);

            // clear fields
            oldPasswordInput.clear();
            newPasswordInput.clear();
            newPasswordConfirmationInput.clear();
        } else if(res.statusCode() == 403) {
            errorMessage.setVisible(true);
            errorMessage.setManaged(true);
            errorMessage.setDescription("Wrong password.");
        } else {
            errorMessage.setVisible(true);
            errorMessage.setManaged(true);
            errorMessage.setDescription("Malformed data. Impressive.");
        }

        // re-enable button
        resetButton.getParent().setDisable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // hide messages
        hideMessages();

        // set the username label
        String oldText = usernameLabel.getText();
        usernameLabel.setText(oldText + "@" + NetworkController.getUsername());

        // initialize validators
        validator.createCheck()
                .dependsOn("oldPassword", oldPasswordInput.textProperty())
                .withMethod(c -> {
                    String oldPassword = c.get("oldPassword");
                    if (oldPassword.isEmpty()) {
                        c.error("Old password cannot be empty.");
                    }
                })
                .immediate();

        validator.createCheck()
                .dependsOn("newPassword", newPasswordInput.textProperty())
                .withMethod(c -> {
                    String newPassword = c.get("newPassword");
                    if (newPassword.length() < 8) {
                        c.error("Password must contain at least 8 characters.");
                    }
                })
                .immediate();

        validator.createCheck()
                .dependsOn("newPasswordConfirmationInput", newPasswordConfirmationInput.textProperty())
                .dependsOn("newPassword", newPasswordInput.textProperty())
                .withMethod(c -> {
                    String newPassword = c.get("newPassword");
                    String newPasswordConfirmationInput = c.get("newPasswordConfirmationInput");
                    if (!newPasswordConfirmationInput.equals(newPassword)) {
                        c.error("Passwords do not match.");
                    }
                })
                .immediate();

        // wrap the login button
        Node parentOfButton = resetButton.getParent();
        TooltipWrapper<Button> signUpWrapper = new TooltipWrapper<>(
                resetButton,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot reset password:\n", validator.createStringBinding())
        );
        ((HBox)parentOfButton).getChildren().add(signUpWrapper);
    }
}
