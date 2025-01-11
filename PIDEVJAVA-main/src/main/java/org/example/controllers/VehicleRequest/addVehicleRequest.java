package org.example.controllers.VehicleRequest;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.models.User;
import org.example.models.VehicleRequest;
import org.controlsfx.control.Notifications;
import org.example.services.VehicleRequestService;
import org.example.utils.CurrentUser;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class addVehicleRequest {
    private final VehicleRequestService VR = new VehicleRequestService();

    @FXML
    private DatePicker dateRequest;

    @FXML
    private TextField typeInsurance;


    @FXML
    private TextField user;

    @FXML
    private TextField marque;
    @FXML
    private TextField modele;
    @FXML
    private TextField matriculNumber;
    @FXML
    private TextField serialNumber;
    @FXML
    private TextField vehiclePrice;
    @FXML
    private DatePicker fabYear;

    @FXML
    private Label dateRequestLabel;

    @FXML
    private Label typeInsuranceLabel;
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
    @FXML
    private Label errorLabel;

    @FXML
    void addVehicleRequest(ActionEvent event) throws SQLException, IOException {
        String typeInsuranceV = typeInsurance.getText();
        String marqueV = marque.getText();
        String modeleV = modele.getText();
        String matriculNumberV = matriculNumber.getText();
        String serialNumberV = serialNumber.getText();
        String vehiclePriceV = vehiclePrice.getText();
        LocalDate fabYearV = fabYear.getValue();
        LocalDate dateRequestV = dateRequest.getValue();

        typeInsuranceLabel.setText("");
        marqueLabel.setText("");
        modeleLabel.setText("");
        matriculNumberLabel.setText("");
        serialNumberLabel.setText("");
        vehiclePriceLabel.setText("");
        fabYearLabel.setText("");
        dateRequestLabel.setText("");

        boolean isValid = true;
        if (dateRequestV == null) {
            dateRequestLabel.setText("Date de la demande is required.");
            isValid = false;
        }
        if (typeInsuranceV.isEmpty()) {
            typeInsuranceLabel.setText("Insurance type is required.");
            isValid = false;
        }

        if (marqueV.isEmpty()) {
            marqueLabel.setText("Marque is required.");
            isValid = false;
        }

        if (modeleV.isEmpty()) {
            modeleLabel.setText("Modele is required.");
            isValid = false;

        }

        if (matriculNumberV.isEmpty()) {
            matriculNumberLabel.setText("Matricule is required.");
            isValid = false;
        } else {

            String regex = "\\d{1,3}TUN\\d{1,4}";
            if  (!matriculNumberV.matches(regex)){
                matriculNumberLabel.setText("Matricule must be in the format: [1-240]TUN[1-9999]");
                isValid = false;}
        }

        if (serialNumberV.isEmpty()) {
            serialNumberLabel.setText("Serial number is required.");
            isValid = false;
        } else {
            int serialNumber = Integer.parseInt(serialNumberV);

            if (serialNumber < 0 || serialNumber > 250) {
                serialNumberLabel.setText("Serial number must be between 0 and 250.");
                isValid = false;
            }

        }
        if (vehiclePriceV.isEmpty()) {
            vehiclePriceLabel.setText("Valeur is required.");
            isValid = false;
        }
        if (fabYearV==null) {
            fabYearLabel.setText("Date de fabrication is required.");
            isValid = false;
        }

        System.out.println(isValid);

        if (isValid){
            VehicleRequest newVehicleRequest= new VehicleRequest();

            LocalDate selectedDate = dateRequest.getValue();
            newVehicleRequest.setDateRequest(selectedDate);
            newVehicleRequest.setTypeInsurance(typeInsurance.getText());
            User user = CurrentUser.getCurrentUser();

            newVehicleRequest.setUser(user);
            newVehicleRequest.setMarque(marque.getText());
            newVehicleRequest.setModele(modele.getText());
            newVehicleRequest.setMatriculNumber(matriculNumber.getText());
            newVehicleRequest.setSerialNumber(serialNumber.getText());
            newVehicleRequest.setVehiclePrice(vehiclePrice.getText());
            LocalDate selectedDates = fabYear.getValue();
            newVehicleRequest.setFabYear(selectedDates);


            VR.add(newVehicleRequest);
            notifactionAlert(event);
            switchToSceneone(event);

        }
        else {
            System.out.println("Please correct the errors in the form.");
        }
    }
    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    @FXML
    public void switchToSceneone(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/VehicleRequest/HboxGetVehi.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
    @FXML
    private Pane pnItems = null;

    @FXML
    private Button btnAddPropertyRequest;

    @FXML
    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnAddPropertyRequest) {
            loadFXML("/addRequest.fxml");

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
    @FXML
    void backHome(ActionEvent event) {
        openWindow("/home.fxml", "List Vehicle Request");
    }
    @FXML
    void goProfile(ActionEvent event) {
        openWindow("/users/userInfo.fxml", "List Life Request");
    }




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
