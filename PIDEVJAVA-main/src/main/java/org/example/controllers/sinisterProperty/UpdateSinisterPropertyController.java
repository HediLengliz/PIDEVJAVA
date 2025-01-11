package org.example.controllers.sinisterProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.example.models.SinisterProperty;
import org.example.services.SinisterPropertyService;

import java.sql.SQLException;

public class UpdateSinisterPropertyController {

    private final SinisterPropertyService sinisterPropertyService = new SinisterPropertyService();
    private SinisterProperty sinisterPropertyToUpdate;

    @FXML
    private void handleUpdate(ActionEvent event) {
        if (sinisterPropertyToUpdate == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No sinister property selected for update.");
            return;
        }

        try {

            sinisterPropertyService.update(sinisterPropertyToUpdate);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Sinister property updated successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update sinister property: " + e.getMessage());
        }
    }

    public void setSinisterPropertyToUpdate(SinisterProperty sinisterProperty) {
        this.sinisterPropertyToUpdate = sinisterProperty;
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
