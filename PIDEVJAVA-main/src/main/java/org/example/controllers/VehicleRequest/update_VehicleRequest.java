package org.example.controllers.VehicleRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.VehicleRequest;
import org.example.services.VehicleRequestService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class update_VehicleRequest {
    private final VehicleRequestService vehicleRequestService = new VehicleRequestService();

    @FXML
    private VehicleRequest selectedVehicleRequest;
    @FXML
    private TextField marque;
    @FXML
    private TextField modele;
    @FXML
    private TextField matriculNumber;
    @FXML
    private TextField serialNumber;
    @FXML
    private TextField vehiclePrice;
    @FXML
    private DatePicker fabYear;


    public void initData(VehicleRequest vehicleRequest) {
        selectedVehicleRequest = vehicleRequest;
        populateFields();
    }
    private void populateFields() {
        marque.setText(selectedVehicleRequest.getMarque());
        modele.setText(selectedVehicleRequest.getModele());
        matriculNumber.setText(selectedVehicleRequest.getMatriculNumber());
        serialNumber.setText(selectedVehicleRequest.getSerialNumber());
        vehiclePrice.setText(selectedVehicleRequest.getVehiclePrice());
        LocalDate selectedDates = fabYear.getValue();
        selectedVehicleRequest.setFabYear(selectedDates);
    }

    @FXML
    void updateVehicleRequest(ActionEvent event) throws IOException {
        String marqueV = marque.getText();
        String modeleV = modele.getText();
        String matriculNumberV = matriculNumber.getText();
        String serialNumberV = serialNumber.getText();
        String vehiclePriceV = vehiclePrice.getText();
        LocalDate fabYearV = fabYear.getValue();

        if (marqueV.isEmpty() || modeleV.isEmpty() || matriculNumberV.isEmpty() || serialNumberV.isEmpty() || vehiclePriceV.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Fields", "Please fill in all fields.");
        } else {
            selectedVehicleRequest.setMarque(marqueV);
            selectedVehicleRequest.setModele(modeleV);
            selectedVehicleRequest.setMatriculNumber(matriculNumberV);
            selectedVehicleRequest.setSerialNumber(serialNumberV);
            selectedVehicleRequest.setVehiclePrice(vehiclePriceV);
            selectedVehicleRequest.setFabYear(fabYearV);

            try {
                vehicleRequestService.update(selectedVehicleRequest);
                showAlert(Alert.AlertType.INFORMATION, "Success", "User Updated", "User updated successfully.");
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
        root = FXMLLoader.load(getClass().getResource("/VehicleRequest/VehicleRequestUser.fxml"));
        stage = (Stage) modele.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
