package org.example.controllers.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.models.User;
import org.example.services.UserService;

import java.sql.SQLException;
import java.util.List;

public class RegistrationController {
    @FXML
    private TextField firstNameField;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label firstNameLabelfield;
    @FXML
    private TextField cinField;
    @FXML

    private Label cinLabel;


    @FXML
    private Label cinLabelField;
    @FXML

    private TextField lastNameField;
    @FXML

    private Label lastNameLabel;

    @FXML
    private Label lastNameLabelfield;
    @FXML
    private TextField emailF;
    @FXML

    private Label emailLabel;

    @FXML
    private Label emailLabelField;
    @FXML
    private TextField addressField;
    @FXML

    private Label addressLabel;

    @FXML
    private Label addresssLabelField;
    @FXML
    private PasswordField passwordF;
    @FXML
    private Label passwordLabelfield;
    @FXML

    private Label passwordLabel;

    @FXML
    private TextField phoneNumber;
    @FXML

    private Label phoneNumberLabel;

    @FXML
    private Label phoneNumberLabelfield;
    @FXML
    private Button submitButton;

    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private void submit() {
        // Get input values
        String firstName = firstNameField.getText();
        String cin = cinField.getText();
        String lastName = lastNameField.getText();
        String email = emailF.getText();
        String address = addressField.getText();
        String password = passwordF.getText();
        String phoneNumber = this.phoneNumber.getText();

        // Initialize error message labels
        firstNameLabelfield.setText("");
        cinLabelField.setText("");
        lastNameLabelfield.setText("");
        emailLabelField.setText("");
        addresssLabelField.setText("");
        passwordLabelfield.setText("");
        phoneNumberLabelfield.setText("");

        // Validate fields
        boolean isValid = true;

        // Validate First Name
        if (firstName.isEmpty()) {
            firstNameLabelfield.setText("First Name is required.");
            System.out.println("first name");
            isValid = false;
        }

        // Validate CIN
        if (cin.isEmpty() ) {
            cinLabelField. setText("CIN must be 10 digits.");
            System.out.println("first name");

            isValid = false;
        }

        // Validate Last Name
        if (lastName.isEmpty()) {
            lastNameLabelfield.setText("Last Name is required.");
            isValid = false;
        }

        // Validate Email
        if (email.isEmpty() || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            emailLabelField.setText("Invalid Email format.");
            isValid = false;
        }

        // Validate Address
        if (address.isEmpty()) {
            addresssLabelField.setText("Address is required.");
            isValid = false;
        }

        // Validate Password
        if (password.isEmpty() || password.length() < 3) {
            passwordLabelfield.setText("Password must be at least 3 characters.");
            isValid = false;
        }

        // Validate Phone Number
        if (phoneNumber.isEmpty() || phoneNumber.length() < 8 ) {
            phoneNumberLabelfield.setText("Invalid Phone Number.");
            isValid = false;
        }

        // If all fields are valid, proceed with registration
        System.out.println(isValid);
        if (isValid) {
            System.out.println("All fields are valid. Proceeding with registration...");

            // Create User object and register
            User newUser = new User(email, List.of("User"), password, firstName, lastName, cin, address, phoneNumber,"");
            UserService userService = new UserService();
            try {

                userService.register(newUser);
                System.out.println("User added: " + newUser.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle registration failure
            }
        } else {
            System.out.println("Please correct the errors in the form.");
        }
    }

    private boolean nextFieldsVisible = false;

    @FXML
    public void initialize() {
        firstNameLabelfield.setText("");
        cinLabelField.setText("");
        emailLabelField.setText("");
        passwordLabelfield.setText("");
        phoneNumberLabelfield.setText("");
        lastNameLabelfield.setText("");
        addresssLabelField.setText("");

        // Set visibility of new labels to false
        firstNameLabel.setVisible(true);
        cinLabel.setVisible(false);
        emailLabel.setVisible(false);
        passwordLabel.setVisible(false);
        phoneNumberLabel.setVisible(true);
        lastNameLabel.setVisible(true);
        addressLabel.setVisible(true);

        cinField.setVisible(false);
        emailF.setVisible(false);
        passwordF.setVisible(false);
        submitButton.setVisible(false);
        previousButton.setVisible(false);
        nextButton.setText("Next");
    }


    @FXML
    private void previous()
    {


        firstNameLabel.setVisible(true);
        cinLabel.setVisible(false);
        emailLabel.setVisible(false);
        passwordLabel.setVisible(false);
        phoneNumberLabel.setVisible(true);
        lastNameLabel.setVisible(true);
        addressLabel.setVisible(true);






            firstNameField.setVisible(true);
            firstNameLabelfield.setVisible(true);
            lastNameField.setVisible(true);
            lastNameLabelfield.setVisible(true);
            addressField.setVisible(true);
            addresssLabelField.setVisible(true);
            phoneNumber.setVisible(true);
            phoneNumberLabelfield.setVisible(true);
        passwordF.setVisible(false);
            passwordLabelfield.setVisible(false);
            cinField.setVisible(false);
            cinLabelField.setVisible(false);
        emailF.setVisible(false);
            emailLabelField.setVisible(false);
            submitButton.setVisible(false);
            nextButton.setVisible(true);
            nextFieldsVisible=false;

    }



    @FXML
    private void showNextFields() {
        if (!nextFieldsVisible) {
            firstNameField.setVisible(false);
            firstNameLabelfield.setVisible(false);
            lastNameField.setVisible(false);
            lastNameLabelfield.setVisible(false);
            addressField.setVisible(false);
            addresssLabelField.setVisible(false);
            phoneNumber.setVisible(false);
            phoneNumberLabelfield.setVisible(false);
            passwordF.setVisible(true);
            passwordLabelfield.setVisible(true);
            cinField.setVisible(true);
            cinLabelField.setVisible(false);
            emailF.setVisible(true);
            emailLabelField.setVisible(true);
            submitButton.setVisible(true);
            nextButton.setVisible(false);
            nextFieldsVisible = true;
            firstNameLabel.setVisible(false);
            cinLabel.setVisible(true);
            emailLabel.setVisible(true);
            passwordLabel.setVisible(true);
            phoneNumberLabel.setVisible(false);
            lastNameLabel.setVisible(false);
            addressLabel.setVisible(false);





            previousButton.setVisible(true);
        } else {
            submit();
        }
    }

}
