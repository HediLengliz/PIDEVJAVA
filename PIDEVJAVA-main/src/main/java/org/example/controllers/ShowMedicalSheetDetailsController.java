package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.models.MedicalSheet;
import org.example.services.MedicalSheetService;

import java.util.Locale;

public class ShowMedicalSheetDetailsController {

    @FXML
    private Label labelUserCIN;
    @FXML
    private Label labelSinisterLife;
    @FXML
    private Label labelMedicalDiagnosis;
    @FXML
    private Label labelTreatmentPlan;
    @FXML
    private Label labelMedicalReports;
    @FXML
    private Label labelDurationOfIncapacity;
    @FXML
    private Label labelProcedurePerformed;
    @FXML
    private Label labelSickLeaveDuration;
    @FXML
    private Label labelHospitalizationPeriod;
    @FXML
    private Label labelRehabilitationPeriod;
    @FXML
    private Label labelMedicalInformation;

    private MedicalSheetService medicalSheetService;

    public ShowMedicalSheetDetailsController() {
        medicalSheetService = new MedicalSheetService();
    }

    @FXML
    private void initialize() {
        try {
            MedicalSheet lastAddedMedicalSheet = medicalSheetService.getLastAddedMedicalSheet();
            showMedicalSheetDetails(lastAddedMedicalSheet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMedicalSheetDetails(MedicalSheet medicalSheet) {
        if (medicalSheet != null) {
            labelMedicalDiagnosis.setText("Medical Diagnosis: " + medicalSheet.getMedicalDiagnosis());
            labelTreatmentPlan.setText("Treatment Plan: " + medicalSheet.getTreatmentPlan());
            labelMedicalReports.setText("Medical Reports: " + medicalSheet.getMedicalReports());
            labelDurationOfIncapacity.setText("Duration Of Incapacity: " + medicalSheet.getDurationOfIncapacity());
            labelProcedurePerformed.setText("Procedure Performed: " + medicalSheet.getProcedurePerformed());
            labelSickLeaveDuration.setText("Sick Leave Duration: " + medicalSheet.getSickLeaveDuration());
            labelHospitalizationPeriod.setText("Hospitalization Period: " + medicalSheet.getHospitalizationPeriod());
            labelRehabilitationPeriod.setText("Rehabilitation Period: " + medicalSheet.getRehabilitationPeriod());
            labelMedicalInformation.setText("Medical Information: " + medicalSheet.getMedicalInformation());
        }
    }

    public  void setMedicalSheet(MedicalSheet medicalSheet) {
        // labelUserCIN.setText(medicalSheet.getUserCIN().getCin()); // Assuming getUserCIN().getCIN() gets the CIN string
        //labelSinisterLife.setText(String.valueOf(medicalSheet.getSinisterLife().getId())); // Assuming getSinisterLife().getId() gets the ID
        labelMedicalDiagnosis.setText(medicalSheet.getMedicalDiagnosis());
        labelTreatmentPlan.setText(medicalSheet.getTreatmentPlan());
        labelMedicalReports.setText(medicalSheet.getMedicalReports());
        labelDurationOfIncapacity.setText(String.format(Locale.getDefault(), "%d", medicalSheet.getDurationOfIncapacity()));
        labelProcedurePerformed.setText(medicalSheet.getProcedurePerformed());
        labelSickLeaveDuration.setText(String.format(Locale.getDefault(), "%d", medicalSheet.getSickLeaveDuration()));
        labelHospitalizationPeriod.setText(String.format(Locale.getDefault(), "%d", medicalSheet.getHospitalizationPeriod()));
        labelRehabilitationPeriod.setText(String.format(Locale.getDefault(), "%d", medicalSheet.getRehabilitationPeriod()));
        labelMedicalInformation.setText(medicalSheet.getMedicalInformation());
    }
}
