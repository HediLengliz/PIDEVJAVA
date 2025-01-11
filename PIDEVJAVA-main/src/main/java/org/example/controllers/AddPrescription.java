package org.example.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.models.Prescription;
import org.example.models.User;
import org.example.services.ChatGPTService;
import org.example.services.PrescriptionService;
import org.example.services.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddPrescription {
    private static final Logger LOGGER = Logger.getLogger(AddPrescription.class.getName());

    @FXML
    private DatePicker datePrescriptionPicker;
    @FXML
    private TextArea medicationsField;
    @FXML
    private TextArea additionalNotesField;
    @FXML
    private TextField validityDurationField;
    @FXML
    private Label validityDurationLabel ;
    @FXML
    private ComboBox<User> userCINComboBox;
    @FXML
    private TableView<Prescription> prescriptionsTableView;
    @FXML
    private TextField searchField;
    @FXML
    private Label responseLabel;
    private PrescriptionService prescriptionService = new PrescriptionService();
    private UserService userService = new UserService();


    @FXML
    private void onSearch() {
        String query = searchField.getText();
        System.out.println("Searching for: " + query);
        CompletableFuture.supplyAsync(() -> new ChatGPTService().callChatGPT(query))
                .thenAccept(response -> Platform.runLater(() -> {
                    System.out.println("Response received: " + response);
                    responseLabel.setText(response);
                }));
    }



    public void initialize() {
        datePrescriptionPicker.setValue(LocalDate.now());
        datePrescriptionPicker.setDisable(true);
        try {
            List<User> users = userService.getAll();
            userCINComboBox.getItems().addAll(users);
            userCINComboBox.setCellFactory(lv -> new ListCell<User>(){
                        @Override
                        protected void updateItem(User user, boolean empty) {
                            super.updateItem(user, empty);
                            setText(empty ? null : user.getFirstName() + " " + user.getLastName());
                        }
                    }


            );

            userCINComboBox.setButtonCell(new ListCell<User>(){
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);
                    setText(empty ? null : user.getFirstName() + " " + user.getLastName());
                }
            });

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to load users.", e);
            showAlert("Error", "Failed to load users.");
        }

        setupRealTimeValidation(validityDurationField, validityDurationLabel);
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
        Label failureLabel = new Label("Failed to add the Prescription. Please check your inputs and try again.");
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
    @FXML
    private void onSavePrescription() {

        try {

            if (medicationsField.getText().isEmpty()) {
                showAlert("Erreur", "Le champ des médicaments ne peut pas être vide.");
                return;
            }

            if (validityDurationField.getText().isEmpty()) {
                showAlert("Erreur", "Le champ de la durée de validité ne peut pas être vide.");
                return;
            }

            int validityDuration;
            try {
                validityDuration = Integer.parseInt(validityDurationField.getText());
            } catch (NumberFormatException e) {
                showAlert("Erreur", "La durée de validité doit être un nombre.");
                return;
            }

            if (userCINComboBox.getValue() == null) {
                showAlert("Erreur", "Vous devez sélectionner un utilisateur.");
                return;
            }
            Prescription prescription = new Prescription();
            prescription.setDatePrescription(datePrescriptionPicker.getValue());
            prescription.setMedications(medicationsField.getText());
            prescription.setStatusPrescription("Active");
            prescription.setAdditionalNotes(additionalNotesField.getText());
            prescription.setValidityDuration(Integer.parseInt(validityDurationField.getText()));
            prescription.setUserCIN(userCINComboBox.getValue());

            prescriptionService.add(prescription);
            List<Prescription> relatedPrescriptions = prescriptionService.getPrescriptionsByUser(prescription.getUserCIN().getId());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPrescription.fxml"));
            Parent root = loader.load();
            UserPrescriptionsController controller = loader.getController();
            controller.setUserRelatedPrescriptions(relatedPrescriptions);

            Stage stage = new Stage();
            stage.setTitle("Related Prescriptions");
            stage.setScene(new Scene(root));
            stage.show();
            showSuccessPopup();

//            showAlert("Success", "Prescription added successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to add prescription.", e);
            showFailurePopup();
//            showAlert("Error", "Failed to add prescription.");
        }
    }

    private void loadPrescriptionsForUser(User user) {
        try {
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByUser(user.getId());
            ObservableList<Prescription> observablePrescriptions = FXCollections.observableArrayList(prescriptions);
            prescriptionsTableView.setItems(observablePrescriptions);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to load prescriptions for user.", e);
            showAlert("Error", "Failed to load prescriptions for user.");
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

    @FXML
    private void goToTable() {
        try {




            root = FXMLLoader.load(getClass().getResource("/ShowPrescriptionList.fxml"));
            Stage stage = (Stage) medicationsField.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




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
