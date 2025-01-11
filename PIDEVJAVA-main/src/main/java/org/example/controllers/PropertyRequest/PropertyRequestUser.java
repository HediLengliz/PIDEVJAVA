package org.example.controllers.PropertyRequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.models.PropretyRequest;
import org.example.services.PropertyRequestService;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class PropertyRequestUser implements Initializable {
    private final PropertyRequestService propertyRequestService = new PropertyRequestService();

    @FXML
    private Label propertyRequest;

    @FXML
    private TableView<PropretyRequest> propertyRequestTable;
    @FXML
    private final PropertyRequestService us = new PropertyRequestService();
    @FXML
    private TextField user;





    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDetails(PropretyRequest request) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la demande");
        alert.setHeaderText("Détails de la demande sélectionnée");
        alert.setContentText("ID: " + request.getId() + "\n" +
                "Age: " + request.getPropertyForm() + "\n" +
                "Profession: " + request.getNumberRooms() + "\n"+
                "Maladie chronique: " + request.getPropertyValue() + "\n"+
                "Détails: " + request.getAddress() + "\n"+
                "Operation: " + request.getSurface() + "\n"


        );
        alert.showAndWait();
    }
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<PropretyRequest> propretyRequests = us.getByUserId(1);
            System.out.println(propretyRequests);
            ObservableList<PropretyRequest> observablePropertyRequests = FXCollections.observableArrayList(propretyRequests);
            propertyRequestTable.setItems(observablePropertyRequests);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        propertyRequestTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showDetails(newSelection);
            }
        });}

}
