package org.example.controllers.ContratHabitat;

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
import org.example.models.ContratHabitat;
import org.example.services.ContratHabitatService;
import org.example.services.PropertyRequestService;

import java.io.IOException;
import java.sql.SQLException;

public class update_ContratHabitat {
    private final ContratHabitatService contratHabitatService = new ContratHabitatService();
    private final PropertyRequestService propertyRequestService = new PropertyRequestService();

    @FXML
    private ContratHabitat selectedContratHabitat;
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



    public void initData(ContratHabitat contratHabitat) {
        selectedContratHabitat = contratHabitat;
        populateFields();
    }
    private void populateFields() {
        java.sql.Date debut = java.sql.Date.valueOf(selectedContratHabitat.getDateDebut());
        java.sql.Date fin = java.sql.Date.valueOf(selectedContratHabitat.getDateFin());
        dateDebut.setValue(debut.toLocalDate());
        dateFin.setValue(fin.toLocalDate());
        description.setText(selectedContratHabitat.getDescription());
        prix.setText(selectedContratHabitat.getPrix());
        matriculeAgent.setText(selectedContratHabitat.getMatriculeAgent());


    }

    @FXML
    void updateContratHabitat(ActionEvent event) throws IOException, SQLException {
        java.sql.Date dateDebutH = java.sql.Date.valueOf(selectedContratHabitat.getDateDebut());
        java.sql.Date dateFinH = java.sql.Date.valueOf(selectedContratHabitat.getDateDebut());

        String descriptionH = description.getText();
        String prixH = prix.getText();
        String matriculeAgentH = matriculeAgent.getText();


        if (  descriptionH.isEmpty() || prixH.isEmpty() || matriculeAgentH.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Fields", "Please fill in all fields.");
        } else {
            selectedContratHabitat.setDateDebut(dateDebutH.toLocalDate());
            selectedContratHabitat.setDateFin(dateFinH.toLocalDate());
            selectedContratHabitat.setDescription(descriptionH);
            selectedContratHabitat.setPrix(prixH);
            selectedContratHabitat.setMatriculeAgent(matriculeAgentH);


            try {
                contratHabitatService.update(selectedContratHabitat);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Contrat habitat Updated", "Contrat habitat updated successfully.");
                closePopup();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "Error updating contrat habitat: " + e.getMessage());
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
        root = FXMLLoader.load(getClass().getResource("/ContratHabitat/get_deleteContratHabitat.fxml"));
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
