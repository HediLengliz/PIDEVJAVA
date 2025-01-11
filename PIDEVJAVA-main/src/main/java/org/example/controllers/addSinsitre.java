package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class addSinsitre {
    @FXML
    void addLifeSinister(ActionEvent event) {
        openWindow("/AddSinisterLife.fxml", "Add Life Request");
    }

    @FXML
    void addPropertySinister(ActionEvent event) {
        openWindow("/sinisterProperty/addSinisterProperty.fxml", "Add Property Request");
    }
    @FXML
    void addVehicleSinister(ActionEvent event) {
        openWindow("/sinisterVehicle/addSinisterVehicle.fxml", "Add Vehicle Request");
    }
    @FXML
    void backHome(ActionEvent event) {
        openWindow("/home.fxml", "List Vehicle Request");
    }
    @FXML
    void goProfile(ActionEvent event) {
        openWindow("/users/userInfo.fxml", "List Life Request");
    }


    @FXML
    private Pane pnItems = null;
    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    private void openWindow(String fxmlFilePath, String title) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFilePath));
            Stage stage = (Stage) pnItems.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
