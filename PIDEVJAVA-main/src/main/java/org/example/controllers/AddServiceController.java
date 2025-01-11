package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.models.Service;
import org.example.services.AnswerService;

import java.io.IOException;
import java.sql.SQLException;

public class AddServiceController {
    private final AnswerService answerService = new AnswerService();
    @FXML
    private TextField name;
    @FXML
    private TextField price;

    private int id;

    @FXML
    void addService(ActionEvent event) throws SQLException {

        String n = name.getText();
        Float t = Float.parseFloat(price.getText());
        Service newS = new Service(n,t);
        try {
            this.answerService.add(newS,this.id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("question created successfully.");
            alert.showAndWait();
            closePopup();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }



    }
    private void closePopup() throws SQLException {
        name.getScene().getWindow().hide();
    }


    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    @FXML
    public void switchToScene(ActionEvent event, String  link) throws IOException {
        root = FXMLLoader.load(getClass().getResource(link));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void show(int id) {
        this.id = id;
    }
}
