package org.example.controllers.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.util.Random;

import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;
import org.example.models.User;
import netscape.javascript.JSObject;
import org.example.services.UserService;
import org.example.utils.CurrentUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.utils.CurrentUser.getCurrentUser;


public class LoginController extends Application {

    @FXML
    private AnchorPane doubleAuthPane;

    @FXML
    private Label labelresend1;

    @FXML
    private TextField verificationCodeField;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Label sqlErrorLabel;

    @FXML
    private Button btnBack;

    @FXML
    private Label labelresend2;

    @FXML
    private Button btnsendverifcode;

    @FXML
    private Label labelresend3;

    @FXML
    private Label btnresend;

    @FXML
    private AnchorPane main_form;
    @FXML
    private AnchorPane register_form;

    @FXML
    private AnchorPane right_form;
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
    Button forgetPasswordButton;

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
        String firstName = firstNameField.getText();
        String cin = cinField.getText();
        String lastName = lastNameField.getText();
        String email = emailF.getText();
        String address = addressField.getText();
        String password = passwordF.getText();
        String phoneNumber = this.phoneNumber.getText();
        firstNameLabelfield.setText("");
        cinLabelField.setText("");
        lastNameLabelfield.setText("");
        emailLabelField.setText("");
        addresssLabelField.setText("");
        passwordLabelfield.setText("");
        phoneNumberLabelfield.setText("");

        // Validate fields
        boolean isValid = true;
        String userInputCaptcha = captchaTextField.getText();

        System.out.println(isValid);
        String captcha = captchaText.getText();
        if (!userInputCaptcha.equalsIgnoreCase(captcha)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("CAPTCHA Error");
            alert.setHeaderText(null);
            alert.setContentText("CAPTCHA verification failed. Please try again.");
            alert.showAndWait();
            isValid = false;
            generateCaptcha();
        }
        // Validate First Name
        if (firstName.isEmpty()) {
            firstNameLabelfield.setText("First Name is required.");
            System.out.println("first name");
            isValid = false;
        }

        // Validate CIN
        if (cin.isEmpty() ) {
            cinLabelField. setText("CIN must be 10 digits.");
            System.out.println("cin ");

            isValid = false;
        }

        // Validate Last Name
        if (lastName.isEmpty()) {
            lastNameLabelfield.setText("Last Name is required.");
            System.out.println("last name");

            isValid = false;
        }

        // Validate Email
        if (email.isEmpty() || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            emailLabelField.setText("Invalid Email format.");
            System.out.println("email");
            isValid = false;

        }

        // Validate Address
        if (address.isEmpty()) {
            addresssLabelField.setText("Address is required.");
            System.out.println("address");

            isValid = false;
        }

        // Validate Password
        if (password.isEmpty() || password.length() < 3) {
            passwordLabelfield.setText("Password must be at least 3 characters.");
            System.out.println("password");
            isValid = false;
        }

        // Validate Phone Number
        if (phoneNumber.isEmpty() || phoneNumber.length() < 8 ) {
            phoneNumberLabelfield.setText("Invalid Phone Number.");
            System.out.println("phone number");
            isValid = false;
        }

        // If all fields are valid, proceed with registration

        if (isValid) {
            System.out.println("All fields are valid. Proceeding with registration...");

            // Create User object and register
            User newUser = new User(email, List.of("User"), password, firstName, lastName, cin, address, phoneNumber,"");
            UserService userService = new UserService();
            try {

                userService.register(newUser);
                System.out.println("User added: " + newUser.toString());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.setContentText("Registration completed successfully!");
                alert.showAndWait();
                previous();
            }

            catch (SQLException | IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Failed");
                alert.setHeaderText(null);
                alert.setContentText("Error updating user: " + e.getMessage());
                alert.showAndWait();

            }



        } else {
            System.out.println("Please correct the errors in the form.");
        }
    }

    private void doubleAuthInvisible() {
        doubleAuthPane.setVisible(false);
        labelresend1.setVisible(false);
        verificationCodeField.setVisible(false);
        smsError.setVisible(false);
        btnBack.setVisible(false);
        labelresend2.setVisible(false);
        btnsendverifcode.setVisible(false);
        labelresend3.setVisible(false);
        btnresend.setVisible(false);
    }

    private void doubleAuthVisible() {
        doubleAuthPane.setVisible(true);
        labelresend1.setVisible(true);
        verificationCodeField.setVisible(true);
        smsError.setVisible(true);
        btnBack.setVisible(true);
        labelresend2.setVisible(true);
        btnsendverifcode.setVisible(true);
        labelresend3.setVisible(true);
        btnresend.setVisible(true);
    }



    @FXML
    TextField emailForget;
    String  twilio_account_sid="AC8f64273573895ceb3ee295e4fb7424d1";
    String        twilio_auth_token="594e62a3c73cb811d2406ea2d99713c2";
    String   twilio_from_number="+18563473035";
    String twilio_to_number="+21652021389";
    private boolean nextFieldsVisible = false;
    private String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000); // Generates a random number between 100000 and 999999
        return String.valueOf(randomNumber);
    }
    @FXML
    private void sendSms() {
        try {
            String s= generateRandomNumber();
            CurrentUser.setUserSecretKey(s);
            Message sentMessage = Message.creator(
                            new com.twilio.type.PhoneNumber(twilio_to_number),
                            new com.twilio.type.PhoneNumber(twilio_from_number), // This should be your Twilio number
                            "votre code est : "+s)
                    .create();

            System.out.println("Sent message with SID: " + sentMessage.getSid());
        } catch (com.twilio.exception.ApiException e) {
            System.out.println("Failed to send SMS: " + e.getMessage());
        }
    }

    String htmlContent = "<html><head>" +
            "<script src='https://www.google.com/recaptcha/api.js' async defer></script>" + // Include reCAPTCHA API
            "</head><body>" +
            "<div class='g-recaptcha' data-sitekey='6LeDJcEpAAAAANB3CQOHBSfDPHyFFUALjqrcRJU4'></div>" + // Replace 'your_site_key' with your reCAPTCHA site key
            "</body></html>";

    public void goToForgetPassword(){
        this.register_form.setVisible(false);
        forgetEmail_form.setVisible(true);
        right_form.setVisible(false);
    }
    @FXML
    Label forgetEmailErrorLabel;
    public void forgetPassword() throws SQLException {
        UserService userService = new UserService();
        try {
            if (emailForget.getText().equals("") || emailForget.getText() ==null){
                forgetEmailErrorLabel.setText("Email field is required");
                return;
            }
            if (!isValidEmail(emailForget.getText())){
                forgetEmailErrorLabel.setText("Invalid email");
                return;
            }
            userService.updatePasswordByEmail(emailForget.getText());
            // Show success alert
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("An email has been sent to reset your password.");
            successAlert.showAndWait();
        } catch (SQLException e) {
            // Show error alert
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Error updating password: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void initialize() {
        doubleAuthInvisible();
        Twilio.init(twilio_account_sid,twilio_auth_token);
        generateCaptcha();
        System.out.println("aa");
    }



    private boolean validateCaptchaResponse(String captchaResponse) {
        // Perform validation logic, such as calling Google's reCAPTCHA API
        // Return true if the CAPTCHA is valid, false otherwise
        // Example:
        // return CaptchaService.isValidCaptcha(captchaResponse);
        return true; // For demonstration purposes
    }
    @FXML
    AnchorPane forgetEmail_form;

    @FXML
    private void previous()
    {
        register_form.setVisible(false);
        main_form.setVisible(true);
        right_form.setVisible(true);
        this.forgetEmail_form.setVisible(false);
        doubleAuthInvisible();


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
    public void registerForm(){
        this.main_form.setVisible(false);
        this.right_form.setVisible(false);
        this.register_form.setVisible(true);
    }
    private double x = 0;
    private double y = 0;
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
    private boolean isCurrentUserBlocked() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            String claimsJson = currentUser.getClaims();
            System.out.println(claimsJson+"aaaaaaaaaaaaaa");
            if (claimsJson!=null){
                JsonObject jsonObject = JsonParser.parseString(claimsJson).getAsJsonObject();


                return jsonObject.has("blocked") && jsonObject.get("blocked").getAsBoolean();
            }
        }
        return false;
    }
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
            sqlerror.setText("");
            return; // Exit the method if email format is invalid
        }

        try {
            sqlerror.setText("");
            if (userService.login(email, password)) {
                User currentUser = userService.getUserByEmail(email);
                System.out.println(currentUser);
                if (currentUser != null) {
                    CurrentUser.setCurrentUser(currentUser);
                    System.out.println(isCurrentUserBlocked());


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
    @FXML
    private Label captchaText;

    @FXML
    private TextField captchaTextField;


    public void generateCaptcha() {
        String captcha = generateRandomString(6); // Change 6 to your desired length
        captchaText.setText(captcha);
    }

    public void verifyCaptcha() {
        String userInput = captchaTextField.getText();
        String captcha = captchaText.getText();
        if (userInput.equalsIgnoreCase(captcha)) {
            // CAPTCHA correct
            System.out.println("CAPTCHA Correct!");
            // Add your logic for what happens when CAPTCHA is correct
        } else {
            // CAPTCHA incorrect
            System.out.println("CAPTCHA Incorrect!");
            // Add your logic for what happens when CAPTCHA is incorrect
        }
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }

        return stringBuilder.toString();
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
        if (isCurrentUserBlocked()) {
            sqlerror.setText("user is blocked");
            return ;

        }
        User currentUser = getCurrentUser();
        if (currentUser!=null){
            List<String> rolesList = currentUser.getRoles();

            // Remove square brackets from roles list
            List<String> cleanedRolesList = rolesList.stream()
                    .map(role -> role.replaceAll("\"", "")) // Remove all double quotes from each role
                    .map(role -> role.replaceAll("^\\[|\\]$", "")) // Remove square brackets only if quotes were not removed

                    .collect(Collectors.toList());
            System.out.println(cleanedRolesList);
            if (cleanedRolesList.contains("Admin") || cleanedRolesList.contains("Super Admin")  )
            {
                root = FXMLLoader.load(getClass().getResource("/dashboard.fxml"));
                stage = (Stage) emailField.getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }

            else if (cleanedRolesList.contains("Doctor") || cleanedRolesList.contains("Pharmacist")  )
            {
                root = FXMLLoader.load(getClass().getResource("/interfaceMedecin.fxml"));
                stage = (Stage) emailField.getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }

            else {
                sendSms();
                doubleAuthVisible();

            }
        }

    }
    @FXML
    Label smsError;
    public void afterDoubleAuth() throws IOException {
        if (verificationCodeField.getText().equals(CurrentUser.getUserSecretKey())) {
            root = FXMLLoader.load(getClass().getResource("/home.fxml"));
            stage = (Stage) emailField.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            smsError.setText("Code incorrect");
        }
    }

    public void register() throws IOException {


        root = FXMLLoader.load(getClass().getResource("/users/Registration.fxml"));
        stage = (Stage) emailField.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/users/login.fxml"));

        Scene scene = new Scene(root);


        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}