package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class home {
    @FXML
    void addRequest(ActionEvent event) {
        openWindow("/addRequest.fxml", "Add Life Request");
    }

    @FXML
    void addSinistre(ActionEvent event) {
        openWindow("/addSinister.fxml", "Add Property Request");
    }
    @FXML
    void addDevis(ActionEvent event) {
        openWindow("/quoteType.fxml", "Add Vehicle Request");
    }


    @FXML
    void btnShowArticle(ActionEvent event) {
        openWindow("/article.fxml", "Add Vehicle Request");
    }
 @FXML
 void btnShowMap(ActionEvent event){
     openWindow("/map.fxml", "Add Vehicle Request");
 }
    @FXML
    void goProfil(ActionEvent event) {
        openWindow("/users/userInfo.fxml", "Add Vehicle Request");
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
