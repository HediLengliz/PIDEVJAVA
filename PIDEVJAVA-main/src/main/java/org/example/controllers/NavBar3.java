package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class NavBar3 {
    @FXML private Pane pnItems = null;
    @FXML private ImageView welcomeImage; // Reference to the ImageView

    @FXML private Button btnAddPrescription;
    @FXML private Button btnSeePrescription;
    @FXML private Button btnAddSinisterLife;
    @FXML private Button btnSeeMedicalSheet;
    @FXML private Button btnAddMedicalSheet;
    @FXML private Button btnMap;

    @FXML
    public void handleClicks(ActionEvent actionEvent) {
        if (welcomeImage.isVisible()) {
            welcomeImage.setVisible(false); // Hide the welcome image
        }

        if (actionEvent.getSource() == btnAddSinisterLife) {
            loadFXML("/AddSinisterLife.fxml");
        } else if (actionEvent.getSource() == btnSeePrescription) {
            loadFXML("/ShowPrescriptionList.fxml");
        } else if (actionEvent.getSource() == btnAddPrescription) {
            loadFXML("/AddPrescription.fxml");
        } else if (actionEvent.getSource() == btnAddMedicalSheet) {
            loadFXML("/AddMedicalSheet.fxml");
        } else if (actionEvent.getSource() == btnSeeMedicalSheet) {
            loadFXML("/ShowMedicalSheetList.fxml");
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
}
