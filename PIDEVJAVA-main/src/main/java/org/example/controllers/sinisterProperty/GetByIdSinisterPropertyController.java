package org.example.controllers.sinisterProperty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.models.SinisterProperty;
import org.example.services.SinisterPropertyService;

import java.sql.SQLException;

public class GetByIdSinisterPropertyController {

    private final SinisterPropertyService sinisterPropertyService = new SinisterPropertyService();

    @FXML
    private TextField idTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField dateTextField;

    @FXML
    private TextField typeTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    void getById(ActionEvent event) {
        try {
            int id = Integer.parseInt(idTextField.getText());
            SinisterProperty sinisterProperty = sinisterPropertyService.getById(id);
            if (sinisterProperty != null) {
                locationTextField.setText(sinisterProperty.getLocation());
                dateTextField.setText(sinisterProperty.getDate_sinister().toString());
                typeTextField.setText(sinisterProperty.getType_degat());
                descriptionTextField.setText(sinisterProperty.getDescription_degat());
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Information", "Sinister Property not found for ID: " + id);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid ID.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve Sinister Property: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
