package org.example.controllers.VehicleRequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import org.example.models.VehicleRequest;
import org.example.services.VehicleRequestService;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class VehicleRequestUser implements Initializable {
    private final VehicleRequestService vehicleRequestService = new VehicleRequestService();

    @FXML
    private Label vehicleRequest;
    @FXML
    private TableView<VehicleRequest> vehicleRequestTable;


    @FXML
    private Label getVehicleRequest;




    @FXML
    private void deleteButtonClicked() {
        VehicleRequest selectedItem = vehicleRequestTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Suppression de l'élément");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cet élément?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        vehicleRequestService.delete(selectedItem.getId());
                        vehicleRequestTable.getItems().remove(selectedItem);
                        showInformationAlert("Élément supprimé avec succès.");
                    } catch (SQLException e) {
                        showErrorAlert("Erreur lors de la suppression de l'élément: " + e.getMessage());
                    }
                }
            });
        } else {
            showErrorAlert("Veuillez sélectionner un élément à supprimer.");
        }
    }

    private void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showDetails(VehicleRequest request) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la demande");
        alert.setHeaderText("Détails de la demande sélectionnée");
        alert.setContentText("ID: " + request.getId() + "\n" +
                "Marque: " + request.getMarque() + "\n" +
                "Modele: " + request.getModele() + "\n"+
                "Matricule: " + request.getMatriculNumber() + "\n"+
                "Numero de serie" + request.getSerialNumber() + "\n"+
                "Prix: " + request.getVehiclePrice() + "\n"+
                "Date fabrication: " + request.getFabYear() + "\n"


        );
        alert.showAndWait();
    }
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<VehicleRequest> vehicleRequests = vehicleRequestService.getByUserId(1);

            ObservableList<VehicleRequest> observableVehicleRequests = FXCollections.observableArrayList(vehicleRequests);

            vehicleRequestTable.setItems(observableVehicleRequests);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        vehicleRequestTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showDetails(newSelection);
            }
        });
    }



}