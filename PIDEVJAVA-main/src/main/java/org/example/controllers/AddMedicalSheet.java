package org.example.controllers;

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
import org.example.models.MedicalSheet;
import org.example.models.SinisterLife;
import org.example.models.User;
import org.example.services.MedicalSheetService;
import org.example.services.SinisterLifeService;
import org.example.services.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddMedicalSheet {
    private static final Logger LOGGER = Logger.getLogger(MedicalSheet.class.getName());

    @FXML
    private TextField medicalDiagnosisField;
    @FXML
    private TextField treatmentPlanField;
    @FXML
    private TextField medicalReportsField;
    @FXML
    private TextField durationOfIncapacityField;
    @FXML
    private TextField procedurePerformedField;
    @FXML
    private TextField sickLeaveDurationField;
    @FXML
    private TextField hospitalizationPeriodField;
    @FXML
    private TextField rehabilitationPeriodField;
    @FXML
    private TextField medicalInformationField;
    @FXML
    private Label durationOfIncapacityError, sickLeaveDurationError, hospitalizationPeriodError, rehabilitationPeriodError;
    @FXML
    private ComboBox<User> userCINComboBox;
    @FXML
    private ComboBox<SinisterLife> sinisterLifeComboBox;

    private UserService userService;
    private SinisterLifeService sinisterLifeService;
    private MedicalSheetService medicalSheetService;

    public AddMedicalSheet() {
        userService = new UserService();
        sinisterLifeService = new SinisterLifeService();
        medicalSheetService = new MedicalSheetService();
    }
    @FXML
    private void goToTable() {
        try {




            root = FXMLLoader.load(getClass().getResource("/ShowMedicalSheetList.fxml"));
            Stage stage = (Stage) hospitalizationPeriodField.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize() {
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
            List<SinisterLife> sinisterLives = sinisterLifeService.getAll();
            sinisterLifeComboBox.getItems().addAll(sinisterLives);
            sinisterLifeComboBox.setCellFactory(lv -> new ListCell<SinisterLife>(){
                @Override
                protected void updateItem(SinisterLife sinisterLife, boolean empty) {
                    super.updateItem(sinisterLife, empty);
                    setText(empty ? null : sinisterLife.getDescription() );
                }
            });
            sinisterLifeComboBox.setButtonCell(new ListCell<SinisterLife>(){
                @Override
                protected void updateItem(SinisterLife sinisterLife, boolean empty) {
                    super.updateItem(sinisterLife, empty);
                    setText(empty ? null : sinisterLife.getDescription() );
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load users or sinister lives.", e);
            showAlert("Error", "Failed to load users or sinister lives.");
        }
        setupRealTimeValidation(durationOfIncapacityField, durationOfIncapacityError);
        setupRealTimeValidation(sickLeaveDurationField, sickLeaveDurationError);
        setupRealTimeValidation(hospitalizationPeriodField, hospitalizationPeriodError);
        setupRealTimeValidation(rehabilitationPeriodField, rehabilitationPeriodError);
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
    public void onSaveMedicalSheet() {
        User userCIN = userCINComboBox.getValue();
        SinisterLife sinisterLife = sinisterLifeComboBox.getValue();
        String medicalDiagnosis = medicalDiagnosisField.getText();
        String treatmentPlan = treatmentPlanField.getText();
        String medicalReports = medicalReportsField.getText();
        String durationStr = durationOfIncapacityField.getText();
        String procedurePerformed = procedurePerformedField.getText();
        String sickLeaveDurationStr = sickLeaveDurationField.getText();
        String hospitalizationPeriodStr = hospitalizationPeriodField.getText();
        String rehabilitationPeriodStr = rehabilitationPeriodField.getText();
        String medicalInformation = medicalInformationField.getText();


        if ( userCIN == null || sinisterLife == null || medicalDiagnosis.isEmpty() || treatmentPlan.isEmpty()) {
            showAlert("Error", "Please fill in all mandatory fields.");
            return;
        }

        int durationOfIncapacity, sickLeaveDuration, hospitalizationPeriod, rehabilitationPeriod;
        try {
            durationOfIncapacity = Integer.parseInt(durationStr);
            sickLeaveDuration = Integer.parseInt(sickLeaveDurationStr);
            hospitalizationPeriod = Integer.parseInt(hospitalizationPeriodStr);
            rehabilitationPeriod = Integer.parseInt(rehabilitationPeriodStr);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des nombres valides pour la durée.");
            return;
        }

        try {
            MedicalSheet medicalSheet = new MedicalSheet();
            medicalSheet.setUserCIN(userCINComboBox.getValue());
            medicalSheet.setSinisterLife(sinisterLifeComboBox.getValue());
            medicalSheet.setMedicalDiagnosis(medicalDiagnosisField.getText());
            medicalSheet.setTreatmentPlan(treatmentPlanField.getText());
            medicalSheet.setMedicalReports(medicalReportsField.getText());
            medicalSheet.setDurationOfIncapacity(Integer.parseInt(durationOfIncapacityField.getText()));
            medicalSheet.setProcedurePerformed(procedurePerformedField.getText());
            medicalSheet.setSickLeaveDuration(Integer.parseInt(sickLeaveDurationField.getText()));
            medicalSheet.setHospitalizationPeriod(Integer.parseInt(hospitalizationPeriodField.getText()));
            medicalSheet.setRehabilitationPeriod(Integer.parseInt(rehabilitationPeriodField.getText()));
            medicalSheet.setMedicalInformation(medicalInformationField.getText());

            medicalSheetService.add(medicalSheet);
            showSuccessPopup();
            // Custom Alert Implementation
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Success");
//            alert.setHeaderText(null);
//            alert.setContentText("Medical Sheet added successfully.");
//
//            DialogPane dialogPane = alert.getDialogPane();
//            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/alert.css")).toExternalForm());
//            dialogPane.getStyleClass().add("myDialog");
//
//            alert.showAndWait();
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowMedicalSheetDetail.fxml"));
//            Parent root = loader.load();
//
//            ShowMedicalSheetDetailsController controller = loader.getController();
//            controller.setMedicalSheet(medicalSheetService.getLastAddedMedicalSheet());
//
//            Stage stage = new Stage();
//            stage.setTitle("Medical Sheet Details");
//            stage.setScene(new Scene(root));
//            stage.show();
        } catch (  SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to add the medical sheet or load the details view.", e);
            showFailurePopup();
            // Show the error using a custom alert as well
//            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//            errorAlert.setTitle("Error");
//            errorAlert.setHeaderText(null);
//            errorAlert.setContentText("Failed to add the medical sheet or load the details view.");
//            DialogPane errorDialogPane = errorAlert.getDialogPane();
//            errorDialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/alert.css")).toExternalForm());
//            errorDialogPane.getStyleClass().add("myDialog");
//            errorAlert.showAndWait();
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
