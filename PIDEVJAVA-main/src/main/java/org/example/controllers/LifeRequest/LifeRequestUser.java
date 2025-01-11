package org.example.controllers.LifeRequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.models.LifeRequest;
import org.example.services.LifeRequestService;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class LifeRequestUser implements Initializable {
    private final LifeRequestService lifeRequestService = new LifeRequestService();

    @FXML
    private Label lifeRequest;

    @FXML
    private TableView<LifeRequest> lifeRequestTable;
    @FXML
    private final LifeRequestService us = new LifeRequestService();
    @FXML
    private TextField user;





    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDetails(LifeRequest request) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la demande");
        alert.setHeaderText("Détails de la demande sélectionnée");
        alert.setContentText("ID: " + request.getId_life() + "\n" +
                "Age: " + request.getAge() + "\n" +
                "Profession: " + request.getOccupation() + "\n"+
                "Maladie chronique: " + request.getChronicDisease() + "\n"+
                "Détails: " + request.getChronicDiseaseDescription() + "\n"+
                "Operation: " + request.getSurgery() + "\n"+
                "Etat civile: " + request.getCivilStatus() + "\n"


        );
        alert.showAndWait();
    }
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<LifeRequest> lifeRequests = us.getByUserId(8);
            System.out.println(lifeRequests);
            ObservableList<LifeRequest> observableLifeRequests = FXCollections.observableArrayList(lifeRequests);
            lifeRequestTable.setItems(observableLifeRequests);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        lifeRequestTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showDetails(newSelection);
            }
        });}
}
