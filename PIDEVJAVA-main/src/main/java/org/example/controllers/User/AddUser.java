package org.example.controllers.User;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckListView;
import org.example.models.User;
import org.example.services.UserService;
import org.example.utils.EmailSender;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddUser {

    private final UserService userService = new UserService();

    @FXML
    private TextField emailTF;
    @FXML
    private Label rolesErrorLabel;

    @FXML
    private CheckComboBox<String> rolesLV;
    @FXML
    private Label emailErrorLabel;

    @FXML
    private TextField firstNameTF;

    @FXML
    private Label firstNameErrorLabel;

    @FXML
    private TextField lastNameTF;

    @FXML
    private Label lastNameErrorLabel;

    @FXML
    private TextField cinTF;

    @FXML
    private Label cinErrorLabel;

    @FXML
    private TextField addressTF;

    @FXML
    private Label addressErrorLabel;

    @FXML
    private TextField phoneNumberTF;

    @FXML
    private Label phoneNumberErrorLabel;

    @FXML
    private Label sqlerror;
    @FXML
    void addUser() throws IOException, SQLException {
        String email = emailTF.getText();
        String firstName = firstNameTF.getText();
        String lastName = lastNameTF.getText();
        String cin = cinTF.getText();
        String address = addressTF.getText();
        String phoneNumber = phoneNumberTF.getText();

        // Validate email, first name, last name, CIN, address, and phone number
        if (email.isEmpty()) {
            emailErrorLabel.setText("Email is required.");
        } else {
            if (!isValidEmail(email)) {

                emailErrorLabel.setText("Email is not valid.");
            } else {
                emailErrorLabel.setText(""); // Clear error if valid
            }
        }


        if (firstName.isEmpty()) {
            firstNameErrorLabel.setText("First Name is required.");
        } else {
            firstNameErrorLabel.setText(""); // Clear error if valid
        }

        if (lastName.isEmpty()) {
            lastNameErrorLabel.setText("Last Name is required.");
        } else {
            lastNameErrorLabel.setText(""); // Clear error if valid
        }

        if (cin.isEmpty()) {
            cinErrorLabel.setText("CIN is required.");
        } else {
            cinErrorLabel.setText(""); // Clear error if valid
        }

        if (address.isEmpty()) {
            addressErrorLabel.setText("Address is required.");
        } else {
            addressErrorLabel.setText(""); // Clear error if valid
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberErrorLabel.setText("Phone Number is required.");
        } else {
            phoneNumberErrorLabel.setText(""); // Clear error if valid
        }
        ObservableList<String> selectedRoles = rolesLV.getCheckModel().getCheckedItems();
        List<String> roles = new ArrayList<>(selectedRoles);
        if (roles.size()==0){
            rolesErrorLabel.setText("role is required");
        }else {
            rolesErrorLabel.setText("");
        }
        // If all validations pass, proceed with adding user
        if (emailErrorLabel.getText().isEmpty() && firstNameErrorLabel.getText().isEmpty() &&
                lastNameErrorLabel.getText().isEmpty() && cinErrorLabel.getText().isEmpty() &&
                addressErrorLabel.getText().isEmpty() && phoneNumberErrorLabel.getText().isEmpty() && rolesErrorLabel.getText().isEmpty()) {
            String plainPassword = generateRandomPassword();

            // Encrypt the password
            String encryptedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

            // Create a new User object with the encrypted password
            User user = new User(0, email, roles, encryptedPassword, firstName, lastName, cin, address, phoneNumber,null,"");

            // Send the encrypted credentials via email
            String emailBody = "Your temporary credentials:\n\nEmail: " + email + "\nPassword: " + plainPassword;
            EmailSender.sendEmail(user.getEmail(), "Protechtini credentials",emailBody);
//            User user = new User(email, roles, firstName, lastName, cin, address, phoneNumber);

            try {
                System.out.println(user.getRoles());
                userService.add(user);
                System.out.println("User added successfully: " + user);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User created successfully.");
                alert.showAndWait();
                closePopup();
//                backToList();
            }
            catch(SQLException e){
                sqlerror.setText(e.getMessage());

            }

        }
    }
    private static String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String generateRandomPassword() {
        int bits = 10;
        Random random = new Random();
        StringBuilder passwordBuilder = new StringBuilder();

        for (int i = 0; i < bits; i++) {
            char randomChar = (char) (random.nextInt(26) + 'a'); // Generate a lowercase character
            passwordBuilder.append(randomChar);
        }

        return passwordBuilder.toString();
    }
    private void closePopup() throws SQLException {
        // Code to close the popup/stage
        // You can get the stage from the root node of the FXML scene
        dashboardController dashboardController = new dashboardController();
        emailTF.getScene().getWindow().hide();
    }
    private boolean isValidEmail(String email) {
        // Simple email validation using regex pattern
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailPattern);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Simple phone number validation using regex pattern
        String phonePattern = "^\\d{10}$"; // Assuming 10-digit phone numbers
        return phoneNumber.matches(phonePattern);
    }

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    public void backToList() throws IOException, SQLException {
        closePopup();

    }
}