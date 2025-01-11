package org.example.controllers.PropertyRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.PropretyRequest;
import org.example.services.PropertyRequestService;

import java.io.IOException;
import java.sql.SQLException;

public class update_PropertyRequest {
    private final PropertyRequestService propertyRequestService = new PropertyRequestService();

    @FXML
    private PropretyRequest selectedPropertyeRequest;
    @FXML
    private TextField numberRooms;
    @FXML
    private TextField propertyForm;
    @FXML
    private TextField propertyValue;
    @FXML
    private TextField surface;
    @FXML
    private TextField address;

    @FXML
    private Label errorLabel;



    public void initData(PropretyRequest propretyRequest) {
        selectedPropertyeRequest = propretyRequest;
        populateFields();
    }
    private void populateFields() {
        numberRooms.setText(selectedPropertyeRequest.getNumberRooms());
        propertyForm.setText(selectedPropertyeRequest.getPropertyForm());
        propertyValue.setText(selectedPropertyeRequest.getPropertyValue());
        surface.setText(selectedPropertyeRequest.getSurface());
        address.setText(selectedPropertyeRequest.getAddress());

    }

    @FXML
    void updatePropertyRequest(ActionEvent event) throws IOException {
        String numberRoomsP = numberRooms.getText();
        String propertyFormP = propertyForm.getText();
        String propertyValueP = propertyValue.getText();
        String surfaceP = surface.getText();
        String addressP = address.getText();

        if (numberRoomsP.isEmpty() || propertyFormP.isEmpty() || propertyValueP.isEmpty() || surfaceP.isEmpty() || addressP.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Fields", "Please fill in all fields.");
        } else {
            selectedPropertyeRequest.setNumberRooms(numberRoomsP);
            selectedPropertyeRequest.setPropertyForm(propertyFormP);
            selectedPropertyeRequest.setPropertyValue(propertyValueP);
            selectedPropertyeRequest.setSurface(surfaceP);
            selectedPropertyeRequest.setAddress(addressP);

            try {
                propertyRequestService.update(selectedPropertyeRequest);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Property request Updated", "Property updated successfully.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "Error updating user: " + e.getMessage());
            }
            switchToSceneone();

        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    @FXML
    public void switchToSceneone() throws IOException {
        root = FXMLLoader.load(getClass().getResource("/PropertyRequest/PropertyRequestUser.fxml"));
        stage = (Stage) surface.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
