package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NavBar implements Initializable {
    @FXML
    private Pane pnItems = null;

    @FXML
    private Button btnAddPropertyRequest;
    @FXML
    private Button btnAddVehicleRequest;
    @FXML
    private Button btnAddLifeRequest;

    @FXML
    private Button btnAddSinisterProperty;
    @FXML
    private Button btnAddSinisterVehicle;
    @FXML
    private Button btnMap;


    @FXML
    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnAddPropertyRequest) {
            loadFXML("/addRequest.fxml");

        }
    }

    private void loadFXML(String fxmlFilePath) {
        try {
            Node itemNode = FXMLLoader.load(getClass().getResource(fxmlFilePath));
            pnItems.getChildren().clear();
            pnItems.getChildren().add(itemNode);
            pnItems.setPrefWidth(itemNode.getLayoutBounds().getWidth());
            pnItems.setPrefHeight(itemNode.getLayoutBounds().getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFXML("/addRequest.fxml");

    }




    @FXML
    private Button btnProfile;





    @FXML
    void btnProfile(ActionEvent event) {
        openWindow("/PropertyRequest/HboxGetProp.fxml", "Profile");
    }

    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    private void openWindow(String fxmlFilePath, String title) {
        try {
            root =FXMLLoader.load(getClass().getResource(fxmlFilePath));
            Stage stage = (Stage) pnItems.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
