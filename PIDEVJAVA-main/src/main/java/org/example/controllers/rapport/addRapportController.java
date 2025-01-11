package org.example.controllers.rapport;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.models.Rapport;
import org.example.models.SinisterProperty;
import org.example.models.SinisterVehicle;
import org.example.models.User;
import org.example.services.RapportService;
import org.example.services.SinisterPropertyService; // Import SinisterPropertyService
import org.example.services.SinisterVehicleService;

import java.sql.SQLException;

public class addRapportController {
    User user;
    private final String ACCOUNT_SID="AC1722b8e3cdc147fa85a5485f544f3fd8";
    private final String AUTH_TOKEN="2f5dc83c6cb36735db8af0c88dd99077";
    private final String NUMERO_TWILIO="+16593992287";

@FXML
private TextField telTF;
    @FXML
    private Label decisionErrorLabel;
    @FXML
    private Label justifictionErrorLabel;
    @FXML
    private ComboBox<String> decisionComboBox;

    @FXML
    private TextArea justificationTextArea;

    private final RapportService rapportService = new RapportService();
    private SinisterProperty sinisterProperty;
    private SinisterVehicle sinisterVehicle;
    private SinisterPropertyService sinisterPropertyService;
    private SinisterVehicleService sinisterVehicleService;
    public addRapportController() {
        this.sinisterVehicleService = new SinisterVehicleService();
        this.sinisterPropertyService = new SinisterPropertyService();
    }
    @FXML
    private void initialize() {
        decisionComboBox.getItems().addAll("Accepté par la garantie", "Non accepté par la garantie");
    }

    public void setSinisterProperty(SinisterProperty sinisterProperty) {

        this.sinisterProperty = sinisterProperty;
    }
    public void setSinisterVehicle(SinisterVehicle sinisterVehicle) {

        this.sinisterVehicle = sinisterVehicle;
    }


    @FXML
    private void handleAddRapport() {
        String emailBody = "Your sinister has been treated successfully! please check your account to view the report ! ";
        org.example.utils.EmailSender.sendEmail("yasmin.jenhani@gmail.com", "Protechtini results",emailBody);
        String decision = decisionComboBox.getValue();
        String justification = justificationTextArea.getText();

        if (decision == null || decision.isEmpty()) {
            decisionErrorLabel.setText("Please select a decision");
            decisionErrorLabel.setStyle("-fx-text-fill: #ff0a0a");
            return;
        }

        if (justification.isEmpty()) {
            justifictionErrorLabel.setText("justification is required");
            justifictionErrorLabel.setStyle("-fx-text-fill: #ff0a0a");
            return;
        }

        SinisterProperty sinister = sinisterProperty;
        Rapport rapport = new Rapport(decision);
        rapport.setJustification(justification);
        rapport.setSinisterRapport(sinister);

        try {
            rapportService.add(rapport);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Rapport added successfully.");
            System.out.println("rapport added");
            // Update the status of the sinister to 'traité'
            sinisterPropertyService.updateStatus(sinister.getId(), "traité");
            System.out.println("status updated");
            //sinisterTreatedConfirmationSMS();
            // Clear input fields after adding
            decisionComboBox.setValue(null);
            justificationTextArea.clear();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add Rapport: " + e.getMessage());
        }
    }
    @FXML
    private void handleAddRapport2() {
        String decision = decisionComboBox.getValue();
        String justification = justificationTextArea.getText();

        if (decision == null || decision.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a decision.");
            return;
        }

        if (justification.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please provide a justification.");
            return;
        }


        SinisterVehicle sinister = sinisterVehicle;
        Rapport rapport = new Rapport(decision);
        rapport.setJustification(justification);
        rapport.setSinisterRapport(sinister);

        try {
            rapportService.add2(rapport);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Rapport added successfully.");
            System.out.println("rapport added");

            sinisterVehicleService.updateStatus(sinister.getId(), "traité");
            System.out.println("status updated");
            //sinisterTreatedConfirmationSMS();
            decisionComboBox.setValue(null);
            justificationTextArea.clear();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add Rapport: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void sinisterTreatedConfirmationSMS() {
        // Formater le message SMS
        String messageSMS = "Congrats ! Your Sinister has been treated successfully! Thank you for using our service. ";

        // Initialiser Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Remplacez "userPhoneNumber" par le véritable numéro de téléphone de l'utilisateur obtenu à partir du formulaire
        String userPhoneNumber = "+21658067088";


        // Envoyer le SMS
        Message message = Message.creator(
                        new PhoneNumber("+" + userPhoneNumber),  // Ajoutez le préfixe international
                        new PhoneNumber(NUMERO_TWILIO),  // Numéro de téléphone Twilio
                        messageSMS)
                .create();

        System.out.println("SMS envoyé : " + message.getSid());
    }
}
