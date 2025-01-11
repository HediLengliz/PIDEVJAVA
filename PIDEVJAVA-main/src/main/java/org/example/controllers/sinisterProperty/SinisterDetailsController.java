package org.example.controllers.sinisterProperty;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.SinisterProperty;
import org.example.services.SinisterPropertyService;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

public class SinisterDetailsController {
private SinisterPropertyService sinisterPropertyService = new SinisterPropertyService();
    private Stage stage;

    @FXML
    private TextField textFieldLocation;

    @FXML
    private TextField textFieldDateSinister;

    @FXML
    private TextField textFieldTypeDamage;

    @FXML
    private TextField textFieldStatus;

    @FXML
    private TextArea textAreaDescription;


    @FXML
    private void goBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sinisterProperty/getByUserIdSinisterProperty.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goBackGetAll(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/dash2.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {

    }

    public void setSinisterDetails(int id) throws SQLException {
        SinisterProperty sinisterProperty = sinisterPropertyService.getById(id);
        if (sinisterProperty != null) {
            textFieldLocation.setText(sinisterProperty.getLocation());
            textFieldDateSinister.setText(sinisterProperty.getDate_sinister().toString());
            textFieldTypeDamage.setText(sinisterProperty.getType_degat());
            textFieldStatus.setText(sinisterProperty.getStatus_sinister());
            textAreaDescription.setText(sinisterProperty.getDescription_degat());
        } else {
            System.out.println("No sinister details found.");
        }
    }


}
