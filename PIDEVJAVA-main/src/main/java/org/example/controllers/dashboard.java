package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.models.User;
import org.example.services.UserService;

import java.io.IOException;
import java.util.Optional;

public class dashboard {

    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnQuestion;
    @FXML
    private Button btnContratVehicule;
    @FXML
    private Button btnContratHabitat;
    @FXML
    private Button btnContratVie;

    @FXML
    private Button btnVehicule;

    @FXML
    private Button btnHabitat;

    @FXML
    private Button btnVie;

    @FXML
    private Label lblTitle;

    @FXML
    private Pane pnlMenus;
    @FXML
    private Button btnSinistresV;

    @FXML
    private Button btnSinistresH;

    @FXML
    private Button btnRapport;
    @FXML
    private Button btnarticle;
    @FXML
    private Button btnComment;

    @FXML
    private Button btnSinisterLife;

    @FXML
    private Button btnGetUsers;
    @FXML
    private Button btnGetReclamations;
    @FXML
    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnVehicule) {
            loadFXML("/VehicleRequest/get_deleteVehicleRequest.fxml");
            updateLabelText("Demandes de véhicules");

        } else if (actionEvent.getSource() == btnHabitat) {
            loadFXML("/PropertyRequest/get_deletePropertyRequest.fxml");
            updateLabelText("Demandes de habitat");

        } else if (actionEvent.getSource() == btnVie) {
            loadFXML("/LifeRequest/get_deleteLifeRequest.fxml");
            updateLabelText("Demandes de vie");

        } else if (actionEvent.getSource() == btnContratVie) {
            loadFXML("/ContratVie/get_deleteContratVie.fxml");
            updateLabelText("Contrats assurances vie");
        }

        else if (actionEvent.getSource() == btnContratHabitat) {
            loadFXML("/ContratHabitat/get_deleteContratHabitat.fxml");
            updateLabelText("Contrats assurances habitat");
        }
        else if (actionEvent.getSource() == btnContratVehicule) {
            loadFXML("/ContratVehicule/get_deleteContratVehicule.fxml");
            updateLabelText("Contrats assurances vehicule");
        }
        else   if (actionEvent.getSource() == btnSinistresV) {
            loadFXML("/sinisterVehicle/getAllSinisterVehicle.fxml");
            updateLabelText("Sinistres véhicules");

        }

        else if (actionEvent.getSource() == btnSinistresH) {
            loadFXML("/sinisterProperty/getAllSinisterProperty.fxml");
            updateLabelText("Sinistres habitats");
        }
        else if (actionEvent.getSource() == btnRapport) {
            loadFXML("/rapport/getAllRapport.fxml");
            updateLabelText("Rapports");
        } else if (actionEvent.getSource() == btnSinisterLife) {
            loadFXML("/ShowSinisterLifeList.fxml");

        }   else if (actionEvent.getSource() == btnGetUsers) {
            loadFXML("/users/allUsers.fxml");
            updateLabelText("All users");
        }

        else if (actionEvent.getSource() == btnGetReclamations) {
            loadFXML("/users/allReclamation.fxml");
            updateLabelText("All users");
        }
        else if (actionEvent.getSource() == btnQuestion) {
            loadFXML("/getQuestions.fxml");
            updateLabelText("Contrats assurances vehicule");
        }
        else if (actionEvent.getSource() == btnarticle) {
            loadFXML("/listarticle.fxml");
            updateLabelText("Contrats assurances vehicule");
        }
        else if (actionEvent.getSource() == btnComment) {
            loadFXML("/listcomment.fxml");
            updateLabelText("Contrats assurances vehicule");
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


    private final UserService userService = new UserService();
    private User selectedUser; // Holds the selected user for updating

    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK))
            {
                pnItems.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/users/login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}