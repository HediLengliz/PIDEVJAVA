package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.models.SinisterLife;
import org.example.models.User;
import org.example.services.SinisterLifeService;
import org.example.services.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import org.example.utils.CurrentUser;

import java.sql.SQLException;
import java.util.List;

public class AddSinisterLife {
    private static final Logger LOGGER = Logger.getLogger(SinisterLife.class.getName());

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField amountSinisterField;
    @FXML
    private Label amountSinisterLabel ;
    @FXML
    private TextField beneficiaryNameField;
    @FXML
    private TextField currentUserTextField;
    @FXML
    private DatePicker dateSinisterPicker;
    private SinisterLifeService sinisterLifeService;
    private UserService userService;


    public AddSinisterLife() {
        this.sinisterLifeService = new SinisterLifeService();
        this.userService = new UserService();


    }

    public void initialize() {
        User user = CurrentUser.getCurrentUser();  // Assuming CurrentUser class provides current user
        currentUserTextField.setText(user.getFirstName() + " " + user.getLastName());
        currentUserTextField.setEditable(false);

        dateSinisterPicker.setValue(LocalDate.now());
        dateSinisterPicker.setDisable(true);

        setupRealTimeValidation(amountSinisterField, amountSinisterLabel);
    }


    private void setupRealTimeValidation(TextField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Regular expression to allow only digits
                textField.setText(newValue.replaceAll("[^\\d]", ""));  // Remove non-digits
                errorLabel.setText("Only numeric values are allowed.");
            } else {
                errorLabel.setText("");  // Clear error message when input is valid
            }
        });
    }

    private void showSuccessPopup() {
        // Create a new stage for the popup
        Stage popupStage = new Stage(StageStyle.TRANSPARENT);

        // Create the content of the popup
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");

        // Create and style the label and buttons
        Label completedLabel = new Label("Completed");
        completedLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        Label successLabel = new Label("Sinister life added successfully!.");
        successLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        Button okButton = new Button("Ok, Close");
        okButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");


        // Add a checkmark icon as an ImageView if you have an image
        Image checkmarkImage = new Image(getClass().getResourceAsStream("/images/checkmark.png"));
        ImageView checkmarkImageView = new ImageView(checkmarkImage);

        checkmarkImageView.setFitHeight(50);
        checkmarkImageView.setFitWidth(50);

        // Add all components to the VBox
        content.getChildren().addAll(checkmarkImageView, completedLabel, successLabel, okButton);

        // Event handler for the OK button
        okButton.setOnAction(e -> popupStage.close());

        // Add the VBox to a new scene and set it on the stage
        Scene scene = new Scene(content);
        scene.setFill(Color.TRANSPARENT); // Make the scene background transparent
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block input events to other windows
        popupStage.showAndWait(); // Show the popup and wait for it to be closed
    }
    private void showFailurePopup() {
        // Create a new stage for the popup
        Stage popupStage = new Stage(StageStyle.TRANSPARENT);

        // Create the content of the popup
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");

        // Create and style the label and buttons
        Label completedLabel = new Label("Failed");
        completedLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #c0392b; -fx-font-weight: bold;");
        Label failureLabel = new Label("Failed to add sinister life. Please check your inputs and try again.");
        failureLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        Button okButton = new Button("Ok, Close");
        okButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        // Add an error icon as an ImageView if you have an image
        Image errorImage = new Image(getClass().getResourceAsStream("/images/error.png"));
        ImageView errorImageView = new ImageView(errorImage);

        errorImageView.setFitHeight(50);
        errorImageView.setFitWidth(50);

        // Add all components to the VBox
        content.getChildren().addAll(errorImageView, completedLabel, failureLabel, okButton);

        // Event handler for the OK button
        okButton.setOnAction(e -> popupStage.close());

        // Add the VBox to a new scene and set it on the stage
        Scene scene = new Scene(content);
        scene.setFill(Color.TRANSPARENT); // Make the scene background transparent
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block input events to other windows
        popupStage.showAndWait(); // Show the popup and wait for it to be closed
    }

    public void onSaveSinisterLife() {
        String description = descriptionField.getText();
        String location = locationField.getText();
        String amountStr = amountSinisterField.getText();
        String beneficiaryName = beneficiaryNameField.getText();
        User sinisterUser = CurrentUser.getCurrentUser(); // Directly use the current user

        if (description.isEmpty() || location.isEmpty() || amountStr.isEmpty() || beneficiaryName.isEmpty()) {
            showAlert("Error", "Please complete all fields.");
            return;
        }

        float amount;
        try {
            amount = Float.parseFloat(amountStr);
            if (amount <= 0) {
                showAlert("Error", "The Amount should be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "The Amount should be a valid number.");
            return;
        }

        try {
            SinisterLife sinisterLife = new SinisterLife();
            sinisterLife.setDescription(description);
            sinisterLife.setLocation(location);
            sinisterLife.setAmountSinister(amount);
            sinisterLife.setBeneficiaryName(beneficiaryName);
            sinisterLife.setDateSinister(Date.from(dateSinisterPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            sinisterLife.setStatusSinister("ongoing");
            sinisterLife.setSinisterUser(sinisterUser);

            sinisterLifeService.add(sinisterLife);
            showSuccessPopup();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showSinisterLifeDetail.fxml"));
            Parent root = loader.load();

            ShowSinisterLifeDetailsController controller = loader.getController();
            controller.setSinisterLife(sinisterLifeService.getLastAddedSinisterLife());

            Stage stage = new Stage();
            stage.setTitle("Sinister Life Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to add sinister life or load the details view.", e);
            showFailurePopup();
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void backHome(ActionEvent event) {
        openWindow("/home.fxml", "List Vehicle Request");
    }
    @FXML
    void goProfile(ActionEvent event) {
        openWindow("/users/userInfo.fxml", "List Life Request");
    }


    @FXML
    private Pane pnItems = null;
    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    private void openWindow(String fxmlFilePath, String title) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFilePath));
            Stage stage = (Stage) pnItems.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
