package org.example.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.models.SinisterLife;
import org.example.models.User;
import org.example.services.SinisterLifeService;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;


public class SinisterLifeListController implements Initializable {

    @FXML
    private TableView<SinisterLife> tableViewSinisterLives;
    @FXML
    private TableColumn<SinisterLife, Number> columnId;
    @FXML
    private TableColumn<SinisterLife, String> columnDateSinister;
    @FXML
    private TableColumn<SinisterLife, String> columnDescription;
    @FXML
    private TableColumn<SinisterLife, String> columnLocation;
    @FXML
    private TableColumn<SinisterLife, String> columnStatusSinister;
    @FXML
    private TableColumn<SinisterLife, Double> columnAmountSinister;
    @FXML
    private TableColumn<SinisterLife, String> columnBeneficiaryName;
    @FXML
    private TableColumn<SinisterLife, Number> columnSinisterUserId;
    @FXML
    private TextField textFieldDateSinister;
    @FXML
    private TextField textFieldDescription;
    @FXML
    private TextField textFieldLocation;
    @FXML
    private TextField textFieldAmountSinister;
    @FXML
    private TextField textFieldBeneficiaryName;
    @FXML
    private TextField textFieldStatusSinister;
    @FXML
    private TextField textFieldSinisterUserId;
    @FXML
    private Button deleteButton;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private TextField searchField;
    private SinisterLifeService sinisterLifeService = new SinisterLifeService();
    private ObservableList<SinisterLife> masterData = FXCollections.observableArrayList();
    private FilteredList<SinisterLife> filteredData;
    public static final String ACCOUNT_SID = "ACa17e2e399a9bf7973af6a8098004ca3c";
    public static final String AUTH_TOKEN = "7a250afb6435341126c3f8d39a16e791";

    public SinisterLifeListController() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    public String formatPhoneNumber(String phoneNumber) {
        if (!phoneNumber.startsWith("+")) {
            return "+216" + phoneNumber;
        }
        return phoneNumber;
    }

    private void sendSms(String phoneNumber, String textMessage) {
        try {
            phoneNumber = formatPhoneNumber(phoneNumber);
            Message sentMessage = Message.creator(
                            new com.twilio.type.PhoneNumber(phoneNumber),
                            new com.twilio.type.PhoneNumber("+12242057963"), // This should be your Twilio number
                            textMessage)
                    .create();

            System.out.println("Sent message with SID: " + sentMessage.getSid());
        } catch (com.twilio.exception.ApiException e) {
            System.out.println("Failed to send SMS: " + e.getMessage());
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnDateSinister.setCellValueFactory(new PropertyValueFactory<>("dateSinisterFormatted"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        columnAmountSinister.setCellValueFactory(new PropertyValueFactory<>("amountSinister"));
        columnStatusSinister.setCellValueFactory(new PropertyValueFactory<>("statusSinister"));
        columnBeneficiaryName.setCellValueFactory(new PropertyValueFactory<>("beneficiaryName"));
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnSinisterUserId.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(Math.toIntExact(cellData.getValue().getSinisterUser().getId())));
        loadSinisterLives();
        tableViewSinisterLives.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textFieldDateSinister.setText(newSelection.getDateSinisterFormatted());
                textFieldDescription.setText(newSelection.getDescription());
                textFieldLocation.setText(newSelection.getLocation());
                textFieldAmountSinister.setText(String.valueOf(newSelection.getAmountSinister()));
                textFieldStatusSinister.setText(newSelection.getStatusSinister());
                textFieldBeneficiaryName.setText(newSelection.getBeneficiaryName());
            }
        });
        initializeSortComboBox();
        loadSinisterLives();
        setupSearchFilter();
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(sinisterLife -> {
                // If filter text is empty, display all data.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare description, location, status, etc. of every sinisterLife with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (sinisterLife.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches description.
                } else if (sinisterLife.getLocation().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches location.
                } else if (sinisterLife.getStatusSinister().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches status.
                } else if (String.valueOf(sinisterLife.getAmountSinister()).contains(lowerCaseFilter)) {
                    return true; // Filter matches amount.
                }
                return false; // Does not match.
            });
        });
    }

    private void showSuccessPopupValidate() {
        // Create a new stage for the popup
        Stage popupStage = new Stage(StageStyle.TRANSPARENT);

        // Create the content of the popup
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");

        // Create and style the label and buttons
        Label completedLabel = new Label("Completed");
        completedLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        Label successLabel = new Label("Sinister life Validated successfully!.");
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
    private void showFailurePopupValidate() {
        // Create a new stage for the popup
        Stage popupStage = new Stage(StageStyle.TRANSPARENT);

        // Create the content of the popup
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");

        // Create and style the label and buttons
        Label completedLabel = new Label("Failed");
        completedLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #c0392b; -fx-font-weight: bold;");
        Label failureLabel = new Label(" Please select an ongoing sinister life to validate.");
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
    private void loadSinisterLives() {
        try {
            List<SinisterLife> sinisterLives = sinisterLifeService.getAll();
            masterData.setAll(sinisterLives);
            filteredData = new FilteredList<>(masterData, p -> true);
            tableViewSinisterLives.setItems(filteredData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void clearTextFields() {
        textFieldDateSinister.setText("");
        textFieldDescription.setText("");
        textFieldLocation.setText("");
        textFieldAmountSinister.setText("");
        textFieldStatusSinister.setText("");
        textFieldBeneficiaryName.setText("");
    }


    @FXML
    private void deleteSingleSLfromList() {
        SinisterLife selectedSinisterLife = tableViewSinisterLives.getSelectionModel().getSelectedItem();
        if (selectedSinisterLife != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected sinister life?", ButtonType.YES, ButtonType.NO);
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        sinisterLifeService.delete(selectedSinisterLife.getId());
                        loadSinisterLives();
                        clearTextFields();
                    } catch (SQLException ex) {
                        Logger.getLogger(SinisterLifeListController.class.getName()).log(Level.SEVERE, null, ex);
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to delete sinister life.");
                        errorAlert.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a sinister life to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void clearTextFieldsevenNotSelected() {
        textFieldDateSinister.setText("");
        textFieldDescription.setText("");
        textFieldLocation.setText("");
        textFieldAmountSinister.setText("");
        textFieldStatusSinister.setText("");
        textFieldBeneficiaryName.setText("");
    }

    @FXML
    private void validateSinisterLifeStatus() {
        SinisterLife selectedSinisterLife = tableViewSinisterLives.getSelectionModel().getSelectedItem();
        if (selectedSinisterLife != null && "ongoing".equalsIgnoreCase(selectedSinisterLife.getStatusSinister())) {
            try {
                System.out.println("Validating SinisterLife with ID: " + selectedSinisterLife.getId());
                selectedSinisterLife.setStatusSinister("Processed");
                sinisterLifeService.update(selectedSinisterLife);
                tableViewSinisterLives.refresh();
                clearTextFields();
                System.out.println("TableView refreshed with updated status.");
                showSuccessPopupValidate();
                User user = selectedSinisterLife.getSinisterUser();
                if (user != null && user.getPhoneNumber() != null) {
                    String formattedPhoneNumber = formatPhoneNumber(user.getPhoneNumber());
                    System.out.println("Formatted Phone Number: " + formattedPhoneNumber);
                    sendSms(formattedPhoneNumber, "Your Sinister Life claim has been validated successfully.");
                } else {
                    System.out.println("User or phone number is null.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SinisterLifeListController.class.getName()).log(Level.SEVERE, null, ex);
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to validate sinister life.");
                errorAlert.showAndWait();
            }
        } else {
//            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an ongoing sinister life to validate.");
//            alert.showAndWait();
            showFailurePopupValidate();
        }
    }

    @FXML
    private void declineSinisterLifeStatus() {
        SinisterLife selectedSinisterLife = tableViewSinisterLives.getSelectionModel().getSelectedItem();
        if (selectedSinisterLife != null && "ongoing".equals(selectedSinisterLife.getStatusSinister())) {
            try {
                System.out.println("Declining SinisterLife with ID: " + selectedSinisterLife.getId());
                selectedSinisterLife.setStatusSinister("Refused");
                sinisterLifeService.update(selectedSinisterLife);
                tableViewSinisterLives.refresh();
                System.out.println("TableView refreshed with declined status.");
                showSuccessPopupRefused();
                User user = selectedSinisterLife.getSinisterUser();
                if (user != null && user.getPhoneNumber() != null) {
                    String formattedPhoneNumber = formatPhoneNumber(user.getPhoneNumber());
                    System.out.println("Formatted Phone Number: " + formattedPhoneNumber);
                    sendSms(formattedPhoneNumber, "Your Sinister Life claim has been refused, please check with your responsible.");
                } else {
                    System.out.println("User or phone number is null.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SinisterLifeListController.class.getName()).log(Level.SEVERE, null, ex);

//                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to decline sinister life status.");
//                errorAlert.showAndWait();

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an ongoing sinister life to decline.");
            showFailurePopupRefused();
//            alert.showAndWait();
        }
    }
    private void initializeSortComboBox() {
        sortComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void onSortOptionSelected() {
        if (masterData == null || masterData.isEmpty()) return;

        String selectedOption = sortComboBox.getSelectionModel().getSelectedItem();

        switch (selectedOption) {
            case "Least Amount":
                masterData.sort(Comparator.comparingDouble(SinisterLife::getAmountSinister));
                break;
            case "Highest Amount":
                masterData.sort(Comparator.comparingDouble(SinisterLife::getAmountSinister).reversed());
                break;
            case "None":
                // If 'None' is selected, you might want to reload the original list or shuffle it
                break;
            default:
                break;
        }

        // Since masterData is the base of filteredData, changes there will reflect automatically
        tableViewSinisterLives.refresh(); // This line ensures the UI updates with new sort
    }
    private void showSuccessPopupRefused() {
        // Create a new stage for the popup
        Stage popupStage = new Stage(StageStyle.TRANSPARENT);

        // Create the content of the popup
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");

        // Create and style the label and buttons
        Label completedLabel = new Label("Completed");
        completedLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        Label successLabel = new Label("Sinister life Refused successfully!.");
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
    private void showFailurePopupRefused() {
        // Create a new stage for the popup
        Stage popupStage = new Stage(StageStyle.TRANSPARENT);

        // Create the content of the popup
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");

        // Create and style the label and buttons
        Label completedLabel = new Label("Failed");
        completedLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #c0392b; -fx-font-weight: bold;");
        Label failureLabel = new Label(" Please select an ongoing sinister life to refuse.");
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

}
