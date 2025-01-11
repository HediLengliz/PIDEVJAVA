package org.example.controllers.ContratHabitat;

import com.stripe.model.Customer;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.models.ContratHabitat;
import org.example.models.InsuranceRequest;
import org.example.models.PropretyRequest;
import org.controlsfx.control.Notifications;
import org.example.models.User;
import org.example.services.ContratHabitatService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.example.services.PropertyRequestService;
import org.example.utils.EmailApi;


public class addContratHabitat {
    private final ContratHabitatService CH = new ContratHabitatService();
    private ContratHabitat contratHabitat=new ContratHabitat();
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
    private PropertyRequestService propertyRequestService= new PropertyRequestService();

    @FXML
    void addContratHabitat(ActionEvent event) throws SQLException, IOException {

        LocalDate dateDebutV = dateDebut.getValue();
        LocalDate dateFinV = dateFin.getValue();

        String descriptionV = description.getText();
        String prixV = prix.getText();
        String matriculeAgentV = matriculeAgent.getText();
        String idV = String.valueOf(contratHabitat.getRequest());



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

            ContratHabitat newContratHabitat= new ContratHabitat();
            InsuranceRequest insuranceRequest1 = new InsuranceRequest();
            insuranceRequest1.setId(Integer.parseInt(request.getText()));
            newContratHabitat.setRequest(insuranceRequest1);
             LocalDate selectedDate = dateDebut.getValue();
             newContratHabitat.setDateDebut(selectedDate);
             LocalDate selectedDate2 = dateFin.getValue();
             newContratHabitat.setDateFin(selectedDate2);
             newContratHabitat.setDescription(description.getText());
             newContratHabitat.setMatriculeAgent(matriculeAgent.getText());

             newContratHabitat.setPrix(prix.getText());

            CH.add(newContratHabitat);
            propertyRequestService.updateStatus(Integer.parseInt(request.getText()), "Accepter");
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
        root = FXMLLoader.load(getClass().getResource("/ContratHabitat/get_deleteContratHabitat.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Label propertyFormLabel;

    @FXML
    private Label numberRoomsLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label propertyValueLabel;

    @FXML
    private Label surfaceLabel;



    public void initData(PropretyRequest propretyRequest) {
        propertyFormLabel.setText(propretyRequest.getPropertyForm());
        numberRoomsLabel.setText(propretyRequest.getNumberRooms());
        addressLabel.setText(propretyRequest.getAddress());
        propertyValueLabel.setText(propretyRequest.getPropertyValue());
        surfaceLabel.setText(propretyRequest.getSurface());
        request.setText(String.valueOf(propretyRequest.getId()));

    }


        final String ACCOUNT_SID = "AC8f64273573895ceb3ee295e4fb7424d1";
        final String AUTH_TOKEN = "594e62a3c73cb811d2406ea2d99713c2";

        public void send_sms_to_Client(int phone) {

            try {
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

                String from_number = "+18563473035";

                String send_number = "+216" + phone;

                String body = "ProTechtini: Votre demande d'asssurance habitat est acceptée avec sucées. Veuiller verifier votre contrat afin de terminer la peocédure de paiement ";

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



    @FXML
    private VBox pnItems = null;

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
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;


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

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
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
                .text("Votre demande est ajoutée avec succée")
                .graphic(confirmationIcon)
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);


        notificationBuilder.show();
    }



}
