package org.example.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.models.Prescription;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserPrescriptionsController implements Initializable {
    @FXML
    private VBox pnItems = null;

    private List<Prescription> userRelatedPrescriptions; // List to store the user-specific prescriptions



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayPrescriptions();
    }


    public void setUserRelatedPrescriptions(List<Prescription> prescriptions) {
        this.userRelatedPrescriptions = prescriptions;
        displayPrescriptions();  // Now calling displayPrescriptions here to ensure it executes after the list is set
    }

    public void displayPrescriptions() {
        if (userRelatedPrescriptions == null || userRelatedPrescriptions.isEmpty()) {
            System.out.println("No prescriptions to display.");
            return;
        }

        try {
            pnItems.getChildren().clear(); // Clear previous items

            // Load the header from FXML
            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/ItemUserRelatedPrescriptions.fxml"));
            HBox header = headerLoader.load();
            setHeaderLabels(header);
            pnItems.getChildren().add(header);

            for (Prescription prescription : userRelatedPrescriptions) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ItemUserRelatedPrescriptions.fxml"));
                HBox item = loader.load();
                setPrescriptionData(item, prescription);
                pnItems.getChildren().add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setHeaderLabels(HBox header) {
        ((Label) header.lookup("#id")).setText("ID");
        ((Label) header.lookup("#date_prescription")).setText("Date Prescription");
        ((Label) header.lookup("#medications")).setText("Medications");
        ((Label) header.lookup("#status_prescription")).setText("Status Prescription");
        ((Label) header.lookup("#additional_notes")).setText("Additional Notes");
        ((Label) header.lookup("#validity_duration")).setText("Validity Duration");
        ((Label) header.lookup("#user_cin_id")).setText("User ID");

        header.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 5px;");
    }

    private void setPrescriptionData(HBox item, Prescription prescription) {
        ((Label) item.lookup("#id")).setText(String.valueOf(prescription.getId()));
        ((Label) item.lookup("#date_prescription")).setText(prescription.getDatePrescription().toString());
        ((Label) item.lookup("#medications")).setText(prescription.getMedications());
        ((Label) item.lookup("#status_prescription")).setText(prescription.getStatusPrescription());
        ((Label) item.lookup("#additional_notes")).setText(prescription.getAdditionalNotes());
        ((Label) item.lookup("#validity_duration")).setText(String.valueOf(prescription.getValidityDuration()));
        ((Label) item.lookup("#user_cin_id")).setText(prescription.getUserCIN().getFirstName());
    }
}