package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.models.MedicalSheet;
import org.example.services.MedicalSheetService;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.layout.HBox;


public class MedicalSheetListController implements Initializable {
    @FXML
    private VBox pnItems = null;
    @FXML
    private TableView<MedicalSheet> tableViewMedicalSheets;
    @FXML
    private TableColumn<MedicalSheet, Number> columnId;
    @FXML
    private TableColumn<MedicalSheet, String> columnMedicalDiagnosis;
    @FXML
    private TableColumn<MedicalSheet, String> columnTreatmentPlan;
    @FXML
    private TableColumn<MedicalSheet, String> columnMedicalReports;
    @FXML
    private TableColumn<MedicalSheet, Integer> columnDurationOfIncapacity;
    @FXML
    private TableColumn<MedicalSheet, String> columnProcedurePerformed;
    @FXML
    private TableColumn<MedicalSheet, Integer> columnSickLeaveDuration;
    @FXML
    private TableColumn<MedicalSheet, Integer> columnHospitalizationPeriod;
    @FXML
    private TableColumn<MedicalSheet, Integer> columnRehabilitationPeriod;
    @FXML
    private TableColumn<MedicalSheet, String> columnMedicalInformation;
    @FXML
    private TextField textFieldMedicalDiagnosis;
    @FXML
    private TextField textFieldTreatmentPlan;
    @FXML
    private TextField textFieldMedicalReports;
    @FXML
    private TextField textFieldDurationOfIncapacity;
    @FXML
    private TextField textFieldProcedurePerformed;
    @FXML
    private TextField textFieldSickLeaveDuration;
    @FXML
    private TextField textFieldHospitalizationPeriod;
    @FXML
    private TextField textFieldRehabilitationPeriod;
    @FXML
    private TextField textFieldMedicalInformation;

    private MedicalSheet selectedMedicalSheet= null;
    private MedicalSheetService medicalSheetService = new MedicalSheetService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayMedicalSheets();
    }


    public void displayMedicalSheets() {


        try {
            MedicalSheetService service = new MedicalSheetService();
            List<MedicalSheet> medicalSheets = service.getAll();
            System.out.println("Retrieved " + medicalSheets.size() + " medical sheets.");
            pnItems.getChildren().clear();

            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/ItemMedicalSheet.fxml"));
            HBox header = headerLoader.load();
            setHeaderLabels(header);
            pnItems.getChildren().add(header);

            for (MedicalSheet sheet : medicalSheets) {
                System.out.println("MedicalSheet ID: " + sheet.getId() + ", SinisterLife ID: " + sheet.getSinisterLife().getDescription()
                );

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ItemMedicalSheet.fxml"));
                HBox item = loader.load();
                setMedicalSheetData(item, sheet);
                item.getProperties().put("medicalSheet", sheet);

                item.setOnMouseClicked(event -> handleSelection(item));
                pnItems.getChildren().add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setHeaderLabels(HBox header) {
        ((Label) header.lookup("#id")).setText("ID");
        ((Label) header.lookup("#sinister_life_id")).setText("Sinister Life Description");
        ((Label) header.lookup("#user_cin_id")).setText("User Name");
        ((Label) header.lookup("#medical_diagnosis")).setText("Medical Diagnosis");
        ((Label) header.lookup("#treatment_plan")).setText("Treatment Plan");
        ((Label) header.lookup("#medical_reports")).setText("Medical Reports");
        ((Label) header.lookup("#duration_of_incapacity")).setText("Duration of Incapacity");
        ((Label) header.lookup("#medical_information")).setText("Medical Information");
        header.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 5px; -fx-font-weight: bold;");
    }



    private void setMedicalSheetData(HBox item, MedicalSheet sheet) {
        ((Label) item.lookup("#id")).setText(String.valueOf(sheet.getId()));
        ((Label) item.lookup("#sinister_life_id")).setText(sheet.getSinisterLife().getDescription());
        ((Label) item.lookup("#user_cin_id")).setText(sheet.getUserCIN().getFirstName());
        ((Label) item.lookup("#medical_diagnosis")).setText(sheet.getMedicalDiagnosis() );
        ((Label) item.lookup("#treatment_plan")).setText(sheet.getTreatmentPlan() );
        ((Label) item.lookup("#medical_reports")).setText(sheet.getMedicalReports() );
        ((Label) item.lookup("#duration_of_incapacity")).setText(sheet.getDurationOfIncapacity().toString());
        ((Label) item.lookup("#medical_information")).setText(sheet.getMedicalInformation());
    }



    private void handleSelection(HBox selectedItem) {
        pnItems.getChildren().forEach(item -> item.setStyle("-fx-background-color: transparent;"));  // Reset all other items
        selectedItem.setStyle("-fx-background-color: lightgray;");  // Highlight selected item

        selectedMedicalSheet = (MedicalSheet) selectedItem.getProperties().get("medicalSheet");
        updateTextFields(selectedMedicalSheet);
    }

    private void updateTextFields(MedicalSheet sheet) {
        textFieldMedicalDiagnosis.setText(sheet.getMedicalDiagnosis());
        textFieldTreatmentPlan.setText(sheet.getTreatmentPlan());
        textFieldMedicalReports.setText(sheet.getMedicalReports());
        textFieldDurationOfIncapacity.setText(sheet.getDurationOfIncapacity() != null ? String.valueOf(sheet.getDurationOfIncapacity()) : "");
        textFieldMedicalInformation.setText(sheet.getMedicalInformation());

    }







    @FXML
    private void deleteSelectedMedicalSheet() {
        if (selectedMedicalSheet != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected medical sheet?", ButtonType.YES, ButtonType.NO);
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        medicalSheetService.delete(selectedMedicalSheet.getId().intValue());
                        displayMedicalSheets();
                        clearTextFields();
                        selectedMedicalSheet = null;
                    } catch (Exception ex) {
                        Logger.getLogger(MedicalSheetListController.class.getName()).log(Level.SEVERE, null, ex);
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to delete medical sheet.");
                        errorAlert.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a medical sheet to delete.");
            alert.showAndWait();
        }
    }


    @FXML
    private void updateSelectedMedicalSheet() {
        if (selectedMedicalSheet != null) {
            String diagnosis = textFieldMedicalDiagnosis.getText();
            String treatmentPlan = textFieldTreatmentPlan.getText();
            String medicalReports = textFieldMedicalReports.getText();
            String medicalInformation = textFieldMedicalInformation.getText();
            Integer durationOfIncapacity;


            try {
                durationOfIncapacity = Integer.parseInt(textFieldDurationOfIncapacity.getText());

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numbers for duration fields.");
                alert.showAndWait();
                return;
            }



            // Set the updated values to the selected MedicalSheet
            selectedMedicalSheet.setMedicalDiagnosis(diagnosis);
            selectedMedicalSheet.setTreatmentPlan(treatmentPlan);
            selectedMedicalSheet.setMedicalReports(medicalReports);
            selectedMedicalSheet.setDurationOfIncapacity(durationOfIncapacity);

            selectedMedicalSheet.setMedicalInformation(medicalInformation);

            try {
                medicalSheetService.update(selectedMedicalSheet);
                displayMedicalSheets();
                clearTextFields();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to update Medical Sheet.");
                errorAlert.showAndWait();
            }
        } else {
            Alert selectionAlert = new Alert(Alert.AlertType.WARNING, "No Medical Sheet is selected.");
            selectionAlert.showAndWait();
        }
    }

    @FXML
    private void clearTextFields() {
        textFieldMedicalDiagnosis.setText("");
        textFieldTreatmentPlan.setText("");
        textFieldMedicalReports.setText("");
        textFieldDurationOfIncapacity.setText("");
        textFieldMedicalInformation.setText("");
    }



}
