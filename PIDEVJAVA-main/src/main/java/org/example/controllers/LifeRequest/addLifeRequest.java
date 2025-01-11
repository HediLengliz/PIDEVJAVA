package org.example.controllers.LifeRequest;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.models.LifeRequest;
import org.example.models.User;
import org.controlsfx.control.Notifications;
import org.example.services.LifeRequestService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.scene.paint.Color;
import org.example.utils.CurrentUser;

public class addLifeRequest  {

    private final LifeRequestService LR = new LifeRequestService();
    @FXML
    private TextField user;
    @FXML
    private DatePicker dateRequest;
    @FXML
    private TextField typeInsurance;
    @FXML
    private TextField age;
    @FXML
    private TextField chronicDisease;
    @FXML
    private TextField surgery;
    @FXML
    private TextField civilStatus;
    @FXML
    private TextField occupation;
    @FXML
    private TextField chronicDiseaseDescription;
    @FXML
    private Label dateRequestLabel;
    @FXML
    private Label typeInsuranceLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label ocuupationLabel;
    @FXML
    private Label chronicDiseaseLabel;
    @FXML
    private Label surgeryLabel;
    @FXML
    private Label civilStatusLabel;
    @FXML
    private Label chronicDiseaseDescriptionLabel;
    @FXML
    private Label occupationLabel;

    @FXML
    void addLifeRequest(ActionEvent event) throws SQLException, IOException {
        LocalDate dateRequestV = dateRequest.getValue();
        String typeInsuranceV = typeInsurance.getText();
        String ageV = age.getText();
        String chronicDiseaseV = chronicDisease.getText();
        String surgeryV = surgery.getText();
        String civilStatusV = civilStatus.getText();
        String chronicDiseaseDescriptionV = chronicDiseaseDescription.getText();
        String occupationV = occupation.getText();

        dateRequestLabel.setText("");
        typeInsuranceLabel.setText("");
        ageLabel.setText("");
        chronicDiseaseLabel.setText("");
        surgeryLabel.setText("");
        civilStatusLabel.setText("");
        chronicDiseaseDescriptionLabel.setText("");
        occupationLabel.setText("");

        boolean isValid = true;

        if (dateRequestV==null) {
            dateRequestLabel.setText("Date de la demande is required.");
            isValid = false;
        }

        if (typeInsuranceV.isEmpty()) {
            typeInsuranceLabel.setText("Insurance type is required.");
            isValid = false;
        }

        if (ageV.isEmpty()) {
            ageLabel.setText("Age type is required.");
            isValid = false;

        } else {

            try {
                int ageValue = Integer.parseInt(ageV); // Parse the ageV string to an integer
                if (ageValue < 18) {
                    ageLabel.setText("Age must be greater than or equal to 18."); // Inform user about the age requirement
                    isValid = false; // Set isValid to false
                } else {
                    // Age is valid
                    isValid = true;
                }
            } catch (NumberFormatException e) {
                ageLabel.setText("Please enter a valid age."); // Inform user about invalid input
                isValid = false; // Set isValid to false
            }
        }
        if (chronicDiseaseV.isEmpty()) {
            chronicDiseaseLabel.setText("Maladie chronique type is required.");
            isValid = false;
        }
        if (surgeryV.isEmpty()) {
            surgeryLabel.setText("Opération is required.");
            isValid = false;
        }
        if (civilStatusV.isEmpty()) {
            civilStatusLabel.setText("Etat civil is required.");
            isValid = false;
        }

        if (occupationV.isEmpty()) {
            occupationLabel.setText("Profession type is required.");
            isValid = false;
        }
        System.out.println(isValid);
        if (isValid){

            LifeRequest newLifeRequest = new LifeRequest();

            LocalDate selectedDate = dateRequest.getValue();

            newLifeRequest.setDateRequest(selectedDate);
            newLifeRequest.setTypeInsurance(typeInsurance.getText());
            User user = CurrentUser.getCurrentUser();
            newLifeRequest.setUser(user);
            newLifeRequest.setAge(age.getText());
            newLifeRequest.setChronicDisease(chronicDisease.getText());
            newLifeRequest.setSurgery(surgery.getText());
            newLifeRequest.setCivilStatus(civilStatus.getText());
            newLifeRequest.setOccupation(occupation.getText());
            newLifeRequest.setChronicDiseaseDescription(chronicDiseaseDescription.getText());

            LR.add(newLifeRequest);
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
        root = FXMLLoader.load(getClass().getResource("/LifeRequest/test.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TableView<LifeRequest> lifeRequestTable;
    @FXML
    void getByUserId(ActionEvent event) {
        try {
            int userId = Integer.parseInt(user.getText());
            List<LifeRequest> properties = LR.getByUserId(userId);
            if (!properties.isEmpty()) {
                lifeRequestTable.getItems().setAll(properties);

            } else {
                showAlert(Alert.AlertType.INFORMATION, "Information", "No life request found for User ID: " + userId);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid User ID.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve life request: " + e.getMessage());
        }


    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
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
            root =FXMLLoader.load(getClass().getResource(fxmlFilePath));
            Stage stage = (Stage) pnItems.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
