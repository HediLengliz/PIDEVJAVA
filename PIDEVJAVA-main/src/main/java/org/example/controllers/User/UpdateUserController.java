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
import org.example.models.User;
import org.controlsfx.control.CheckListView;
import org.example.services.UserService;
import org.example.utils.CurrentUser;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateUserController  {

    private final UserService userService = new UserService();
    private User selectedUser; // Holds the selected user for updating


    @FXML
    private TextField emailTF;
    @FXML
    private Label rolesErrorLabel;

    @FXML
    private CheckListView<String> rolesLV;
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

    public void initData(User user) {
        selectedUser = user;
        populateFields();
    }
    private void populateFields() {
        emailTF.setText(selectedUser.getEmail());
        rolesLV.getCheckModel().clearChecks();
        List<String> userRoles = selectedUser.getRoles();
        List<String> cleanedRolesList = userRoles.stream()
                .map(role -> role.replaceAll("\"", "")) // Remove all double quotes from each role
                .map(role -> role.replaceAll("^\\[|\\]$", "")) // Remove square brackets only if quotes were not removed
                .flatMap(role -> Arrays.stream(role.split("-"))) // Split roles by "-" delimiter and flatten the stream
                .collect(Collectors.toList());

        System.out.println(userRoles);
        // Loop through each role in the selectedRolesList and check it in the CheckListView
        for (String role : cleanedRolesList) {
            System.out.println("user role is");
            System.out.println(role);
            int roleIndex = rolesLV.getItems().indexOf(role);
            if (roleIndex >= 0) {
                rolesLV.getCheckModel().check(roleIndex);
            }
        }

        firstNameTF.setText(selectedUser.getFirstName());
        lastNameTF.setText(selectedUser.getLastName());
        cinTF.setText(selectedUser.getCin());
        addressTF.setText(selectedUser.getAddress());
        phoneNumberTF.setText(selectedUser.getPhoneNumber());
    }

    @FXML
    void updateUser(ActionEvent event) throws IOException,SQLException {
        String email = emailTF.getText();
        ObservableList<String> selectedRoles = rolesLV.getCheckModel().getCheckedItems();
        List<String> roles = new ArrayList<>(selectedRoles);
        String firstName = firstNameTF.getText();
        String lastName = lastNameTF.getText();
        String cin = cinTF.getText();
        String address = addressTF.getText();
        String phoneNumber = phoneNumberTF.getText();
        if (email.isEmpty()) {
            emailErrorLabel.setText("Email is required.");
        } else {
            emailErrorLabel.setText(""); // Clear error if valid
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

        if (roles.size()==0){
            rolesErrorLabel.setText("role is required");
        }else {
            rolesErrorLabel.setText("");
        }
        if (emailErrorLabel.getText().isEmpty() && firstNameErrorLabel.getText().isEmpty() &&
                lastNameErrorLabel.getText().isEmpty() && cinErrorLabel.getText().isEmpty() &&
                addressErrorLabel.getText().isEmpty() && phoneNumberErrorLabel.getText().isEmpty() && rolesErrorLabel.getText().isEmpty()) {
            selectedUser.setEmail(email);
            selectedUser.setRoles(roles);
            selectedUser.setFirstName(firstName);
            selectedUser.setLastName(lastName);
            selectedUser.setCin(cin);
            selectedUser.setAddress(address);
            selectedUser.setPhoneNumber(phoneNumber);

            try {
                userService.update(selectedUser);
                showAlert(Alert.AlertType.INFORMATION, "Success", "User Updated", "User updated successfully.");
            } catch (SQLException e) {
                sqlerror.setText(e.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) throws IOException {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
        if (selectedUser == CurrentUser.getCurrentUser()) {
            backToProfile();
        }
       else backToList();

    }
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    public void backToList() throws IOException {
        root = FXMLLoader.load(getClass().getResource("/users/users.fxml"));
        stage = (Stage) emailTF.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    public void backToProfile() throws IOException {
        root = FXMLLoader.load(getClass().getResource("/users/profil.fxml"));
        stage = (Stage) emailTF.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }


}
