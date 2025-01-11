package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Admin {
    @FXML
    void showContratVie(ActionEvent event) {
        openWindow("/ContratVie/get_deleteContratVie.fxml");
    }

    @FXML
    void showContratHabitat(ActionEvent event) {
        openWindow("/ContratHabitat/get_deleteContratHabitat.fxml");
    }

    @FXML
    void showContratVehicule(ActionEvent event) {
        openWindow("/ContratVehicule/get_deleteContratVehicule.fxml");
    }
    @FXML
    void showLifeRequest(ActionEvent event) {
        openWindow("/LifeRequest/get_deleteLifeRequest.fxml");
    }
    @FXML
    void showPropertyRequest(ActionEvent event) {
        openWindow("/PropertyRequest/get_deletePropertyRequest.fxml");
    }
    @FXML
    void showVehicleRequest(ActionEvent event) {
        openWindow("/VehicleRequest/get_deleteVehicleRequest.fxml");
    }

    private void openWindow(String fxmlFilePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
