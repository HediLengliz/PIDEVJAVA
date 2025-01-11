package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class NavBar2 {
    @FXML
    private Pane pnItems = null;

    @FXML
    private Button btnAddVehicleRequest;
    @FXML
    private Button btnAddLifeRequest;
    @FXML
    private Button btnAddPropertyRequest;
    @FXML
    private Button btnAddSinisterProperty;
    @FXML
    private Button btnAddSinisterVehicle;
    @FXML
    private Button btnMap;

    @FXML
    private Button btnHabitat;

    @FXML
    private Button btnVie;

    @FXML
    private Label lblTitle;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;
    @FXML
    private ImageView imgInitial;

    @FXML
    public void initialize() {
        imgInitial.setImage(new Image("/home page.PNG")); // Set the initial image
    }

    @FXML
    public void handleClicks(ActionEvent actionEvent) {
        if (imgInitial.isVisible()) {
            imgInitial.setVisible(false); // Hide the image when any button is clicked
        } else if
        (actionEvent.getSource() == btnAddPropertyRequest) {
            loadFXML("/sinisterVehicle/showTableV.fxml");

        } else if (actionEvent.getSource() == btnAddLifeRequest) {
            loadFXML("/LifeRequest/addLifeRequest.fxml");

        } else if (actionEvent.getSource() == btnAddVehicleRequest) {
            loadFXML("/sinisterProperty/showTable.fxml");


        }  else if (actionEvent.getSource() == btnAddSinisterVehicle) {
            loadFXML("/sinisterVehicle/addSinisterVehicle.fxml");


        }   else if (actionEvent.getSource() == btnAddSinisterProperty) {
            loadFXML("/sinisterProperty/addSinisterProperty.fxml");
        }  else if (actionEvent.getSource() == btnMap) {
            loadFXML("/map.fxml");
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

    private void updateLabelText(String text) {
        lblTitle.setText(text);
    }
}