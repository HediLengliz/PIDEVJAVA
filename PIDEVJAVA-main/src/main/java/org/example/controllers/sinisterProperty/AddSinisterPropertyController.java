package org.example.controllers.sinisterProperty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.models.SinisterProperty;
import org.example.services.SinisterPropertyService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddSinisterPropertyController {
    private final SinisterPropertyService sps = new SinisterPropertyService();

    @FXML
    private TextField LocationTF;

    @FXML
    private DatePicker Date_of_sinisterPicker;

    @FXML
    private TextField Damage_TypeTF;

    @FXML
    private TextField Damage_descriptionTF;



    @FXML
   public void addSinisterProperty(ActionEvent event) {
        if (LocationTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a location.");
            return;
        }

        if (Date_of_sinisterPicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a date of sinister.");
            return;
        }

        if (Damage_TypeTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a damage type.");
            return;
        }

        if (Damage_descriptionTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a damage description.");
            return;
        }

        try {
            String location = LocationTF.getText();
            LocalDate dateOfSinister = Date_of_sinisterPicker.getValue();
            String typeDegat = Damage_TypeTF.getText();
            String descriptionDegat = Damage_descriptionTF.getText();

            SinisterProperty sinisterProperty = new SinisterProperty(dateOfSinister, location, typeDegat, descriptionDegat);
            sps.add(sinisterProperty);
            showSuccessMessage();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add SinisterProperty: " + e.getMessage());
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Sinister added successfully!");
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
