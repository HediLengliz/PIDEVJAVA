package org.example.controllers;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.models.Prescription;
import org.example.models.User;
import org.example.services.PrescriptionService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class PrescriptionListController implements Initializable {
    @FXML
    private VBox pnItems = null;
    @FXML
    private TableView<Prescription> tableViewPrescriptions;
    @FXML
    private TableColumn<Prescription, Number> columnId;
    @FXML
    private TableColumn<Prescription, String> columnDatePrescription;
    @FXML
    private TableColumn<Prescription, String> columnMedications;
    @FXML
    private TableColumn<Prescription, String> columnStatusPrescription;
    @FXML
    private TableColumn<Prescription, String> columnAdditionalNotes;
    @FXML
    private TableColumn<Prescription, Number> columnValidityDuration;
    @FXML
    private TableColumn<Prescription, Number> columnUserCINId;

    @FXML
    private TextField datePrescriptionField;
    @FXML
    private TextField medicationsField;
    @FXML
    private TextField additionalNotesField;
    @FXML
    private TextField validityDurationField;
    @FXML
    private TextField statusPrescriptionField;
    @FXML
    private TextField userCINField;

    private Prescription selectedPrescription = null;

    private PrescriptionService prescriptionService = new PrescriptionService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayPrescriptions();


    }

    public void displayPrescriptions() {
        try {
            PrescriptionService p = new PrescriptionService();
            List<Prescription> prescriptions = p.getAll();
            pnItems.getChildren().clear(); // Clear previous items

            // Load the header from FXML or create it programmatically
            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/Item.fxml"));
            HBox header = headerLoader.load();

            // Set the header labels
            setHeaderLabels(header);

            // Add the header to the VBox
            pnItems.getChildren().add(header);

            for (Prescription prescription : prescriptions) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Item.fxml"));
                HBox item = loader.load();
                setPrescriptionData(item, prescription);

                // Store the prescription object in the HBox's properties for later retrieval
                item.getProperties().put("prescription", prescription);

                // Add a mouse click event to handle selection
                item.setOnMouseClicked(event -> handleSelection(item));

                pnItems.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
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

    private void handleSelection(HBox selectedItem) {
        // Clear previous styles or markers
        pnItems.getChildren().forEach(item -> item.setStyle(null));

        // Highlight the selected item
        selectedItem.setStyle("-fx-background-color: lightgray;");

        // Retrieve the Prescription object from the selected HBox
        selectedPrescription = (Prescription) selectedItem.getProperties().get("prescription");

        // Update text fields with the selected prescription's details
        updateTextFields(selectedPrescription);
    }


    private void updateTextFields(Prescription prescription) {
        datePrescriptionField.setText(prescription.getDatePrescription().toString());
        medicationsField.setText(prescription.getMedications());
        additionalNotesField.setText(prescription.getAdditionalNotes());
        validityDurationField.setText(String.valueOf(prescription.getValidityDuration()));
        statusPrescriptionField.setText(prescription.getStatusPrescription());
        userCINField.setText(String.valueOf(prescription.getUserCIN().getFirstName()));
    }

    public void refreshHBoxItems(VBox hBox) {
        try {
            PrescriptionService r = new PrescriptionService();
            List<Prescription> prescriptions = r.getAll();

            // Clear the existing items in the HBox
            hBox.getChildren().clear();
            FXMLLoader l = new FXMLLoader(getClass().getResource("/Item.fxml"));
            HBox it = l.load();
            Label idL = (Label) it.lookup("#id");
            Label titleL = (Label) it.lookup("#titre");
            Label dateL = (Label) it.lookup("#date");
            Button btn = (Button) it.lookup("#btn");
            btn.setText("ACTIONS");
            btn.setStyle(" -fx-border-color: transparent;-fx-fill: white; -fx-padding: 0;");
            idL.setText("ID");
            titleL.setText("TITLE");
            dateL.setText("DATE");
            idL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");

            // Style for TITLE label
            titleL.setStyle("-fx-font-size: 16px; -fx-text-fill: #2E86C1; -fx-font-weight: bold; -fx-font-family: 'Arial';");

            // Style for DATE label
            dateL.setStyle("-fx-font-size: 16px; -fx-text-fill: #117A65; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            btn.setStyle("-fx-background-color: transparent; " +
                    "-fx-border-color: transparent; " +
                    "-fx-padding: 0; " +
                    "-fx-text-fill: #117A65; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: bold;");
            pnItems.getChildren().add(it);

            // Add the updated list of items to the HBox
            for (Prescription prescription : prescriptions) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Item.fxml"));
                HBox item = loader.load();

                Label idLabel = (Label) item.lookup("#id");
                Label date_prescriptionLabel = (Label) item.lookup("#date_prescription");
                Label medicationsLabel = (Label) item.lookup("#medications");
                Label statusPrescriptionLabel  = (Label) item.lookup("#status_prescription");
                Label additionalNotesLabel = (Label) item.lookup("#additional_notes") ;
                Label validityDurationLabel = (Label) item.lookup("#validity_duration") ;
                Label userCinIDLabel = (Label) item.lookup("#user_cin_id") ;
                idLabel.setText(String.valueOf(prescription.getId()));
                date_prescriptionLabel.setText(prescription.getId().toString());
                medicationsLabel.setText(prescription.getMedications());
                statusPrescriptionLabel.setText(prescription.getStatusPrescription());
                additionalNotesLabel.setText(prescription.getAdditionalNotes());
                validityDurationLabel.setText(prescription.getValidityDuration().toString());
                userCinIDLabel.setText(prescription.getUserCIN().getId().toString());

                // Add click event handler to show details


                hBox.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    private void loadPrescriptions() {
        try {
            List<Prescription> prescriptions = prescriptionService.getAll();
            ObservableList<Prescription> prescriptionObservableList = FXCollections.observableArrayList(prescriptions);
            tableViewPrescriptions.setItems(prescriptionObservableList);
        } catch (SQLException ex) {
            Logger.getLogger(PrescriptionListController.class.getName()).log(Level.SEVERE, null, ex);
            // Handle error
        }
    }

    private void clearTextFields() {
        datePrescriptionField.setText("");
        medicationsField.setText("");
        additionalNotesField.setText("");
        validityDurationField.setText("");
        statusPrescriptionField.setText("");
        userCINField.setText("");
    }
    @FXML
    private void clearTextFieldsevenNotSelected() {
        datePrescriptionField.setText("");
        medicationsField.setText("");
        additionalNotesField.setText("");
        validityDurationField.setText("");
        statusPrescriptionField.setText("");
        userCINField.setText("");
    }

    @FXML
    private void deleteSelectedPrescription() {
        if (selectedPrescription != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected prescription?", ButtonType.YES, ButtonType.NO);
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        prescriptionService.delete(Math.toIntExact(selectedPrescription.getId()));
                        displayPrescriptions(); // Assume this refreshes the VBox with updated list
                        clearTextFields();
                        selectedPrescription = null; // Clear the reference since the prescription is deleted
                    } catch (SQLException ex) {
                        Logger.getLogger(PrescriptionListController.class.getName()).log(Level.SEVERE, null, ex);
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to delete prescription.");
                        errorAlert.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a prescription to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void updateSelectedPrescription() {
        if (selectedPrescription != null) {
            // Extract values from text fields
            String medications = medicationsField.getText();
            String additionalNotes = additionalNotesField.getText();
            Integer validityDuration;

            try {
                validityDuration = Integer.parseInt(validityDurationField.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for validity duration.");
                alert.showAndWait();
                return;
            }

            selectedPrescription.setDatePrescription(LocalDate.now());  // Assuming the date should be set to now
            selectedPrescription.setMedications(medications);
            selectedPrescription.setAdditionalNotes(additionalNotes);
            selectedPrescription.setValidityDuration(validityDuration);

            try {
                prescriptionService.update(selectedPrescription);
                displayPrescriptions();  // Refresh the display to reflect the updated data
                clearTextFields();
                selectedPrescription = null; // Optionally clear the selected prescription after update
            } catch (SQLException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to update Prescription.");
                errorAlert.showAndWait();
            }
        } else {
            Alert selectionAlert = new Alert(Alert.AlertType.WARNING, "No Prescription is selected.");
            selectionAlert.showAndWait();
        }
    }


    public void exportPrescriptionToPDF(ActionEvent actionEvent) {
        if (selectedPrescription != null) {
            String path = "C:\\Users\\21652\\Downloads\\prescription_" + selectedPrescription.getId() + ".pdf";

            try {
                PdfWriter writer = new PdfWriter(path);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                // Define custom fonts and styles
                PdfFont headerFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                PdfFont bodyFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

                // Create styled paragraphs
                Paragraph header = new Paragraph("Prescription Details").setFont(headerFont).setFontSize(14).setBold();
                Paragraph date = new Paragraph("Date: " + selectedPrescription.getDatePrescription().toString()).setFont(bodyFont);
                Paragraph medications = new Paragraph("Medications: " + selectedPrescription.getMedications()).setFont(bodyFont);
                Paragraph status = new Paragraph("Status: " + selectedPrescription.getStatusPrescription()).setFont(bodyFont);
                Paragraph additionalNotes = new Paragraph("Additional Notes: " + selectedPrescription.getAdditionalNotes()).setFont(bodyFont);
                Paragraph validityDuration = new Paragraph("Validity Duration: " + selectedPrescription.getValidityDuration() + " days").setFont(bodyFont);
                Paragraph userId = new Paragraph("Prescribed to User ID: " + selectedPrescription.getUserCIN().getId()).setFont(bodyFont);

                Paragraph footer = new Paragraph("Protechtini\n18, rue de l'Usine\nZI Aéroport Charguia\nII 2035 Ariana\nPhone: (+216) 58 26 64 36\nEmail: protechtini.synthcode@esprit.tn")
                        .setFont(bodyFont).setItalic().setFontSize(10);

                // Add paragraphs to document
                document.add(header);
                document.add(date);
                document.add(medications);
                document.add(status);
                document.add(additionalNotes);
                document.add(validityDuration);
                document.add(userId);
                document.add(new Paragraph("\n\n\n\n\n"));
                document.add(footer);

                document.close();

                System.out.println("PDF exported successfully to: " + path);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("An error occurred while exporting the PDF.");
            }
        } else {
            System.out.println("No Prescription selected.");
        }
    }

    @FXML
    private void sendPrescriptionByEmail() {
        if (selectedPrescription != null) {
            User user = selectedPrescription.getUserCIN();
            System.out.println("User Details: " + user.getFirstName() + " " + user.getLastName());
            String userEmail = user.getEmail();
            String emailSubject = "Your Prescription Details";
            String emailContent = String.format(
                    "Dear %s, \n\nHere are your prescription details:\n- Medications: %s\n- Additional Notes: %s\n\nBest regards,\nYour Clinic",
                    user.getFirstName() + " " + user.getLastName(),
                    selectedPrescription.getMedications(),
                    selectedPrescription.getAdditionalNotes()
            );

            try {
                String emailUsername = "med26.marnissi02@outlook.com";
                String emailPassword = "MHR260402__";  // Consider securing your credentials or using environment variables

                System.out.println("Sending email to: " + userEmail);

                MailerAPI.sendMail(emailUsername, emailPassword, userEmail, emailSubject, emailContent);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Email sent successfully to " + userEmail);
                successAlert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to send email.");
                errorAlert.showAndWait();
            }
        } else {
            Alert selectionAlert = new Alert(Alert.AlertType.WARNING, "Please select a prescription to send.");
            selectionAlert.showAndWait();
        }
    }
    @FXML
    void backHome(ActionEvent event) {
        openWindow("/home.fxml", "List Vehicle Request");
    }
    @FXML
    void goProfile(ActionEvent event) {
        openWindow("/users/userInfo.fxml", "List Life Request");
    }
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
