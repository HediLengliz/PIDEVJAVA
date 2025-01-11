package org.example.controllers.ContratVie;

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
import org.example.models.ContratVie;
import org.example.models.InsuranceRequest;
import org.example.services.ContratVieService;
import org.example.services.LifeRequestService;

import java.io.IOException;
import java.sql.SQLException;

public class update_ContratVie{
    private final ContratVieService contratVieService = new ContratVieService();
    private final LifeRequestService lifeRequestService = new LifeRequestService();

    @FXML
    private ContratVie selectedContratVie;
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



    public void initData(ContratVie contratVie) {
        selectedContratVie = contratVie;
        populateFields();
    }
    private void populateFields() {


        java.sql.Date debut = java.sql.Date.valueOf(selectedContratVie.getDateDebut());
        java.sql.Date fin = java.sql.Date.valueOf(selectedContratVie.getDateFin());

        dateDebut.setValue(debut.toLocalDate());
        dateFin.setValue(fin.toLocalDate());
        description.setText(selectedContratVie.getDescription());
        prix.setText(selectedContratVie.getPrix());
        matriculeAgent.setText(selectedContratVie.getMatriculeAgent());


    }

    @FXML
    void updateContratVie(ActionEvent event) throws IOException, SQLException {
        java.sql.Date dateDebutVie = java.sql.Date.valueOf(selectedContratVie.getDateDebut());
        java.sql.Date dateFinVie = java.sql.Date.valueOf(selectedContratVie.getDateDebut());

        String descriptionVie = description.getText();
        String prixVie = prix.getText();
        String matriculeAgentVie = matriculeAgent.getText();


        if (descriptionVie.isEmpty() || prixVie.isEmpty() || matriculeAgentVie.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Fields", "Please fill in all fields.");
        } else {

            selectedContratVie.setDateDebut(dateDebutVie.toLocalDate());
            selectedContratVie.setDateFin(dateFinVie.toLocalDate());
            selectedContratVie.setDescription(descriptionVie);
            selectedContratVie.setPrix(prixVie);
            selectedContratVie.setMatriculeAgent(matriculeAgentVie);


            try {
                contratVieService.update(selectedContratVie);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Contrat vie Updated", "Contrat vie updated successfully.");
                closePopup();

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "Error updating contrat vie: " + e.getMessage());
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
        root = FXMLLoader.load(getClass().getResource("/ContratVie/get_deleteContratVie.fxml"));
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
