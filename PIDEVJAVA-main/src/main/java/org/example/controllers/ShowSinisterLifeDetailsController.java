package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.models.SinisterLife;
import org.example.services.SinisterLifeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ShowSinisterLifeDetailsController {

    @FXML
    private Label labelDescription;
    @FXML
    private Label labelLocation;
    @FXML
    private Label labelAmountSinister;
    @FXML
    private Label labelStatusSinister;
    @FXML
    private Label labelDateSinister;
    @FXML
    private Label labelBeneficiaryName;

    private SinisterLifeService sinisterLifeService;

    public ShowSinisterLifeDetailsController() {
        sinisterLifeService = new SinisterLifeService();
    }

    @FXML
    private void initialize() {
        try {
            SinisterLife lastAddedSinisterLife = sinisterLifeService.getLastAddedSinisterLife();
            showSinisterLifeDetails(lastAddedSinisterLife);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void showSinisterLifeDetails(SinisterLife sinisterLife) {
        if (sinisterLife != null) {
            labelDescription.setText("Description: " + sinisterLife.getDescription());
            labelLocation.setText("Location: " + sinisterLife.getLocation());
            labelAmountSinister.setText("Amount Sinister: " + sinisterLife.getAmountSinister());
            labelStatusSinister.setText("Status Sinister: " + sinisterLife.getStatusSinister());
            labelDateSinister.setText("Date Sinister: " + sinisterLife.getDateSinister().toString());
            labelBeneficiaryName.setText("Beneficiary Name: " + sinisterLife.getBeneficiaryName());
        }
    }
    public void setSinisterLife(SinisterLife sinisterLife) {
        labelDescription.setText(sinisterLife.getDescription());
        labelLocation.setText(sinisterLife.getLocation());
        labelAmountSinister.setText(String.format(Locale.getDefault(), "%.2f", sinisterLife.getAmountSinister()));
        labelStatusSinister.setText(sinisterLife.getStatusSinister());
        LocalDate localDate = new java.sql.Date(sinisterLife.getDateSinister().getTime()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String formattedDate = localDate.format(formatter);

        labelDateSinister.setText("Date Sinister: " + formattedDate);
        labelBeneficiaryName.setText(sinisterLife.getBeneficiaryName());
    }

}
