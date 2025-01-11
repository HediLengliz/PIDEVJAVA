package org.example.controllers.ContratVehicule;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.ContratVehicule;
import org.example.services.ContratVehiculeService;
import org.example.services.VehicleRequestService;

import java.io.IOException;
import java.sql.SQLException;

public class update_ContratVehicule {
    private final ContratVehiculeService contratVehiculeService = new ContratVehiculeService();
    private final VehicleRequestService vehicleRequestService = new VehicleRequestService();

    @FXML
    private ContratVehicule selectedContratVehicule;
    @FXML
    private TextField request;

    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;

    @FXML
    private TextField description;
    @FXML
    private TextField matriculeAgent;
    @FXML
    private TextField prix;

    @FXML
    private Label errorLabel;



    public void initData(ContratVehicule contratVehicule) {
        selectedContratVehicule = contratVehicule;
        populateFields();
    }
    private void populateFields() {
        java.sql.Date debut = java.sql.Date.valueOf(selectedContratVehicule.getDateDebut());
        java.sql.Date fin = java.sql.Date.valueOf(selectedContratVehicule.getDateFin());

        dateDebut.setValue(debut.toLocalDate());
        dateFin.setValue(fin.toLocalDate());
        description.setText(selectedContratVehicule.getDescription());
        prix.setText(selectedContratVehicule.getPrix());
        matriculeAgent.setText(selectedContratVehicule.getMatriculeAgent());


    }

    @FXML
    void updateContratVehicule(ActionEvent event) throws IOException, SQLException {
        java.sql.Date dateDebutV = java.sql.Date.valueOf(selectedContratVehicule.getDateDebut());
        java.sql.Date dateFinV = java.sql.Date.valueOf(selectedContratVehicule.getDateDebut());

        String descriptionV = description.getText();
        String prixV = prix.getText();
        String matriculeAgentV = matriculeAgent.getText();


        if (descriptionV.isEmpty() || prixV.isEmpty() || matriculeAgentV.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Fields", "Please fill in all fields.");
        } else {
            selectedContratVehicule.setDateDebut(dateDebutV.toLocalDate());
            selectedContratVehicule.setDateFin(dateFinV.toLocalDate());
            selectedContratVehicule.setDescription(descriptionV);
            selectedContratVehicule.setPrix(prixV);
            selectedContratVehicule.setMatriculeAgent(matriculeAgentV);


            try {
                contratVehiculeService.update(selectedContratVehicule);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Contrat vehicule Updated", "Contrat vehicule updated successfully.");
                closePopup();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "Error updating contrat vehicule: " + e.getMessage());
            }

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
        root = FXMLLoader.load(getClass().getResource("/ContratVehicule/get_deleteContratVehicule.fxml"));
        stage = (Stage) description.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void closePopup() throws SQLException {
        // Code to close the popup/stage
        // You can get the stage from the root node of the FXML scene
        description.getScene().getWindow().hide();
    }
}
