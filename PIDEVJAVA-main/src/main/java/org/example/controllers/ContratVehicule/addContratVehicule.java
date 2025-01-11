package org.example.controllers.ContratVehicule;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.models.ContratVehicule;
import org.example.models.InsuranceRequest;
import org.example.models.User;
import org.example.models.VehicleRequest;
import org.controlsfx.control.Notifications;
import org.example.services.ContratVehiculeService;
import org.example.services.VehicleRequestService;
import org.example.utils.EmailApi;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class addContratVehicule {
    private final ContratVehiculeService CH = new ContratVehiculeService();
    @FXML
    private TextField request;

    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;

    @FXML
    private TextField description;
    @FXML
    private TextField matriculeAgent;
    @FXML
    private TextField prix;
    @FXML
    private Label idLabel;
    @FXML
    private Label dateDebutLabel;
    @FXML
    private Label dateFinLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label matriculeAgentLabel;
    @FXML
    private Label prixLabel;
    private VehicleRequestService vehicleRequestService =new VehicleRequestService();
private ContratVehicule contratVehicule= new ContratVehicule();
    @FXML
    void addContratVehicule(ActionEvent event) throws SQLException, IOException {
        LocalDate dateDebutV = dateDebut.getValue();
        LocalDate dateFinV = dateFin.getValue();

        String descriptionV = description.getText();
        String prixV = prix.getText();
        String matriculeAgentV = matriculeAgent.getText();

        String idV = String.valueOf(contratVehicule.getRequest());


        dateDebutLabel.setText("");
        dateFinLabel.setText("");
        prixLabel.setText("");
        descriptionLabel.setText("");
        matriculeAgentLabel.setText("");
        idLabel.setText("");


        boolean isValid = true;
        if (dateDebutV == null) {
            dateDebutLabel.setText("Date debut du contrat is required.");
            isValid = false;
        }

        if (dateFinV  == null) {
            dateFinLabel.setText("Date fin du contrat is required.");
            isValid = false;
        }

        if (prixV == null || prixV.isEmpty()) {
            prixLabel.setText("Prix is required.");
            isValid = false;
        }

        if (descriptionV == null || descriptionV.isEmpty()) {
            descriptionLabel.setText("Description is required.");
            isValid = false;

        }
        if (matriculeAgentV == null || matriculeAgentV.isEmpty()) {
            matriculeAgentLabel.setText("Matricule agent is required.");
            isValid = false;
        }
        if (idV == null || idV.isEmpty()) {
            idLabel.setText("Id request is required.");
            isValid = false;
        }

        System.out.println(isValid);

        if (isValid){

        ContratVehicule newContratVehicule= new ContratVehicule();
            InsuranceRequest insuranceRequest1 = new InsuranceRequest();
            insuranceRequest1.setId(Integer.parseInt(request.getText()));
            newContratVehicule.setRequest(insuranceRequest1);
        LocalDate selectedDate = dateDebut.getValue();
        newContratVehicule.setDateDebut(selectedDate);
        LocalDate selectedDate2 = dateFin.getValue();
        newContratVehicule.setDateFin(selectedDate2);
        newContratVehicule.setDescription(description.getText());
        newContratVehicule.setMatriculeAgent(matriculeAgent.getText());

        newContratVehicule.setPrix(prix.getText());

        CH.add(newContratVehicule);
        vehicleRequestService.updateStatus(Integer.parseInt(request.getText()), "Accepter");

        notifactionAlert(event);
        closePopup();
            User user =new User();
            String emailBody = "Votre demande est accéptée. Bienvenue dans Protechtini. Veuillez consulter votre contrat afin de terminer la procédure du paiement";

            EmailApi.sendEmail("yasmine.said2001@gmail.com", "Protechtini credentials",emailBody);
        //send_sms_to_Client(52021389);
        }
        else {
            System.out.println("Please correct the errors in the form.");
        }}
    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    @FXML
    public void switchToSceneone(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/ContratVehicule/get_deleteContratVehicule.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private Label marqueLabel;
    @FXML
    private Label modeleLabel;
    @FXML
    private Label matriculNumberLabel;
    @FXML
    private Label serialNumberLabel;
    @FXML
    private Label vehiclePriceLabel;
    @FXML
    private Label fabYearLabel;


    public void initData(VehicleRequest vehicleRequest) {
        marqueLabel.setText(vehicleRequest.getMarque());
        modeleLabel.setText(vehicleRequest.getModele());
        matriculNumberLabel.setText(vehicleRequest.getMatriculNumber());
        serialNumberLabel.setText(vehicleRequest.getSerialNumber());
        vehiclePriceLabel.setText(vehicleRequest.getVehiclePrice());
        request.setText(String.valueOf(vehicleRequest.getId()));


    }

    final String ACCOUNT_SID = "AC8f64273573895ceb3ee295e4fb7424d1";
    final String AUTH_TOKEN = "594e62a3c73cb811d2406ea2d99713c2";

    public void send_sms_to_Client(int phone) {

        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            String from_number = "+18563473035";

            String send_number = "+216" + phone;

            String body = "ProTechtini: Votre demande d'asssurance Vehicule est acceptée avec sucées. Veuiller verifier votre contrat afin de terminer la peocédure de paiement ";

            DateTimeFormatter formatter_date = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            Message message = Message.creator(
                            new PhoneNumber(send_number),
                            new PhoneNumber(from_number),
                            body )
                    .create();

            System.out.println("Message sent successfully. Message SID: " + message.getSid());

        } catch (final ApiException e) {
            // Gestion des exceptions
            System.err.println(e);
        }
    }
    private void closePopup() throws SQLException {
        // Code to close the popup/stage
        // You can get the stage from the root node of the FXML scene
        description.getScene().getWindow().hide();
    }
    private void notifactionAlert(ActionEvent event) {


        FontAwesomeIconView confirmationIcon = new FontAwesomeIconView();
        confirmationIcon.setGlyphName("CHECK_CIRCLE"); // Utilisation de l'icône de confirmation
        confirmationIcon.setSize("5em"); // Taille de l'icône
        confirmationIcon.setFill(Color.GREEN); // Changer la couleur de l'icône en vert

        Notifications notificationBuilder = Notifications.create()
                .title("Download Complete")
                .text("Le contrat est ajoutée avec succée")
                .graphic(confirmationIcon)
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);


        notificationBuilder.show();
    }
}
