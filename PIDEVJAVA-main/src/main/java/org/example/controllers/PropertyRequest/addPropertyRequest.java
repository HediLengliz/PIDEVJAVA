package org.example.controllers.PropertyRequest;

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
import org.example.models.PropretyRequest;
import org.example.models.User;
import org.controlsfx.control.Notifications;
import org.example.services.PropertyRequestService;
import org.example.utils.CurrentUser;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class addPropertyRequest {
    private final PropertyRequestService PR = new PropertyRequestService();

    @FXML
    private DatePicker dateRequest;

    @FXML
    private TextField typeInsurance;


    @FXML
    private TextField user;

    @FXML
    private TextField numberRooms;
    @FXML
    private TextField propertyForm;
    @FXML
    private TextField propertyValue;
    @FXML
    private TextField surface;
    @FXML
    private TextField address;

    @FXML
    private Label dateRequestLabel;
    @FXML
    private Label typeInsuranceLabel;
    @FXML
    private Label numberRoomsLabel;
    @FXML
    private Label propertyFormLabel;
    @FXML
    private Label propertyValueLabel;
    @FXML
    private Label surfaceLabel;
    @FXML
    private Label addressLabel;

    @FXML
    void addPropertyRequest(ActionEvent event) throws SQLException, IOException {

        LocalDate dateRequestV = dateRequest.getValue();
        String typeInsuranceV = typeInsurance.getText();
        String numberRoomsV = numberRooms.getText();
        String propertyFormV = propertyForm.getText();
        String propertyValueV = propertyValue.getText();
        String surfaceV = surface.getText();
        String addressV = address.getText();

        dateRequestLabel.setText("");
        typeInsuranceLabel.setText("");
        numberRoomsLabel.setText("");
        propertyFormLabel.setText("");
        propertyValueLabel.setText("");
        surfaceLabel.setText("");
        addressLabel.setText("");



        boolean isValid = true;
        if (dateRequestV == null) {
            typeInsuranceLabel.setText("Date de la demande is required.");
            isValid = false;
        }

        if (typeInsuranceV.isEmpty()) {
            typeInsuranceLabel.setText("Insurance type is required.");
            isValid = false;
        }

        if (numberRoomsV.isEmpty()) {
            numberRoomsLabel.setText("Nb piéce is required.");
            isValid = false;
        }

        if (propertyValueV.isEmpty()) {
            propertyValueLabel.setText("Valeur de l'habitat is required.");
            isValid = false;

        }
        if (propertyFormV.isEmpty()) {
            propertyFormLabel.setText("Type de l'habitat is required.");
            isValid = false;
        }
        if (surfaceV.isEmpty()) {
            surfaceLabel.setText("Surface is required.");
            isValid = false;
        }
        if (addressV.isEmpty()) {
            addressLabel.setText("Adresse is required.");
            isValid = false;
        }

        System.out.println(isValid);

        if (isValid){
            PropretyRequest newPropertyRequest= new PropretyRequest();

            LocalDate selectedDate = dateRequest.getValue();

            newPropertyRequest.setDateRequest(selectedDate);
            newPropertyRequest.setTypeInsurance(typeInsurance.getText());
            User user = CurrentUser.getCurrentUser();
            newPropertyRequest.setUser(user);
            newPropertyRequest.setNumberRooms(numberRooms.getText());
            newPropertyRequest.setPropertyForm(propertyForm.getText());
            newPropertyRequest.setPropertyValue(propertyValue.getText());
            newPropertyRequest.setAddress(address.getText());
            newPropertyRequest.setSurface(surface.getText());

            PR.add(newPropertyRequest);

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
        root = FXMLLoader.load(getClass().getResource("/PropertyRequest/HboxGetProp.fxml"));
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
    private Button btnAddVehicleRequest;
    @FXML
    private Button btnAddLifeRequest;
    @FXML
    private Button btnAddPropertyRequest;


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

    private void updateLabelText(String text) {
        lblTitle.setText(text);
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
