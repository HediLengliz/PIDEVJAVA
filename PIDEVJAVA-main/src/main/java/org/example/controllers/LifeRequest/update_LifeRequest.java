package org.example.controllers.LifeRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.LifeRequest;
import org.example.services.LifeRequestService;

import java.io.IOException;
import java.sql.SQLException;

public class update_LifeRequest {
    private final LifeRequestService lifeRequestService = new LifeRequestService();
    @FXML
    private LifeRequest selectedLifeRequest;
    @FXML
    private TextField age;
    @FXML
    private TextField chronicDisease;
    @FXML
    private TextField surgery;
    @FXML
    private TextField civilStatus;
    @FXML
    private TextField occupation;
    @FXML
    private TextField chronicDiseaseDescription;

    @FXML
    private Label errorLabel;



    public void initData(LifeRequest lifeRequest) {
        selectedLifeRequest = lifeRequest;
        populateFields();
    }
    private void populateFields() {
        age.setText(selectedLifeRequest.getAge());
        chronicDisease.setText(selectedLifeRequest.getChronicDisease());
        surgery.setText(selectedLifeRequest.getSurgery());
        civilStatus.setText(selectedLifeRequest.getCivilStatus());
        chronicDiseaseDescription.setText(selectedLifeRequest.getChronicDiseaseDescription());
        occupation.setText(selectedLifeRequest.getOccupation());


    }

    @FXML
    void updateLifeRequest(ActionEvent event) throws IOException {
        String ageL = age.getText();
        String chronicDiseaseL = chronicDisease.getText();
        String surgeryL = surgery.getText();
        String civilStatusL = civilStatus.getText();
        String chronicDiseaseDescriptionL = chronicDiseaseDescription.getText();
        String occupationL = occupation.getText();


        if (ageL.isEmpty() || chronicDiseaseL.isEmpty() || surgeryL.isEmpty() || civilStatusL.isEmpty() || chronicDiseaseDescriptionL.isEmpty() || occupationL.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Fields", "Please fill in all fields.");
        } else {
            selectedLifeRequest.setAge(ageL);
            selectedLifeRequest.setChronicDisease(chronicDiseaseL);
            selectedLifeRequest.setSurgery(surgeryL);
            selectedLifeRequest.setCivilStatus(civilStatusL);
            selectedLifeRequest.setChronicDiseaseDescription(chronicDiseaseDescriptionL);
            selectedLifeRequest.setOccupation(occupationL);


            try {
                lifeRequestService.update(selectedLifeRequest);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Life request Updated", "life request updated successfully.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "Error updating life request: " + e.getMessage());
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
        root = FXMLLoader.load(getClass().getResource("/LifeRequest/LifeRequestUser.fxml"));
        stage = (Stage) occupation.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
