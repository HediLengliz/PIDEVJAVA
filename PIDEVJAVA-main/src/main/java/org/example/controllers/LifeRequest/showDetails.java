package org.example.controllers.LifeRequest;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.models.LifeRequest;

public class showDetails {
@FXML
private Label idLabel;
    @FXML
    private Label ageLabel;

    @FXML
    private Label chronDiseaseLabel;

    @FXML
    private Label surgeryLabel;

    @FXML
    private Label civilStatusLabel;

    @FXML
    private Label occupationLabel;

    @FXML
    private Label detailsDiseaseLabel;

    public void initData(LifeRequest lifeRequest) {
        idLabel.setText(String.valueOf(lifeRequest.getId_life()));
        ageLabel.setText(lifeRequest.getAge());
        chronDiseaseLabel.setText(lifeRequest.getChronicDisease());
        surgeryLabel.setText(lifeRequest.getSurgery());
        civilStatusLabel.setText(lifeRequest.getCivilStatus());
        occupationLabel.setText(lifeRequest.getOccupation());
        detailsDiseaseLabel.setText(lifeRequest.getChronicDiseaseDescription());
    }


}
