package org.example.controllers.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.User;
import org.example.services.UserService;
import org.example.utils.CurrentUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class Login {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final UserService userService = new UserService();
    @FXML
    private Label emailErrorLabel; // Inject the email error label

    @FXML
    private Label passwordErrorLabel; // Inject the password error label

    @FXML
    private Label sqlerror;

    @FXML
    public void loginAction() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            sqlerror.setText("Please enter both email and password.");
            emailErrorLabel.setText("");
            passwordErrorLabel.setText(""); // Clear password error label
            return; // Exit the method if fields are empty
        }

        if (!isValidEmail(email)) {
            emailErrorLabel.setText("Invalid email format.");
            passwordErrorLabel.setText(""); // Clear password error label
            return; // Exit the method if email format is invalid
        }

        try {
            sqlerror.setText("");
            if (userService.login(email, password)) {
                User currentUser = userService.getUserByEmail(email);
                if (currentUser != null) {
                    CurrentUser.setCurrentUser(currentUser);
                    System.out.println("Login successful!");
                    switchToSceneOne();
                } else {
                    emailErrorLabel.setText("Error: User not found.");
                    passwordErrorLabel.setText(""); // Clear password error label
                }
            } else {

                System.out.println("Invalid email or password.");
                emailErrorLabel.setText("");
                passwordErrorLabel.setText(""); // Clear password error label
            }
        } catch (SQLException e) {
       sqlerror.setText(e.getMessage());
            System.out.println("An error occurred during login: " + e.getMessage());
        } catch (IOException e) {
            emailErrorLabel.setText("Error loading scene.");
            passwordErrorLabel.setText(""); // Clear password error label
            System.out.println("Error loading scene: " + e.getMessage());
        }
    }
    private boolean isValidEmail(String email) {
        // Simple email validation using regex pattern
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailPattern);
    }
    @FXML
    public void logoutAction() {
        CurrentUser.clearCurrentUser();
        System.out.println("Logout successful!");
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void switchToSceneOne() throws IOException {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser!=null){
            List<String> rolesList = currentUser.getRoles();

            // Remove square brackets from roles list
            List<String> cleanedRolesList = rolesList.stream()
                    .map(role -> role.replaceAll("\"", "")) // Remove all double quotes from each role
                    .map(role -> role.replaceAll("^\\[|\\]$", "")) // Remove square brackets only if quotes were not removed

                    .collect(Collectors.toList());
            System.out.println(cleanedRolesList);
            if (cleanedRolesList.contains("Admin") || cleanedRolesList.contains("Super Admin")  ){
                root = FXMLLoader.load(getClass().getResource("/users/users.fxml"));
                stage = (Stage) emailField.getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else {
                root = FXMLLoader.load(getClass().getResource("/users/profil.fxml"));
                stage = (Stage) emailField.getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }

    }


    public void register() throws IOException {


        root = FXMLLoader.load(getClass().getResource("/users/Registration.fxml"));
        stage = (Stage) emailField.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
