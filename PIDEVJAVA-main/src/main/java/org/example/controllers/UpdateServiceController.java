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

public class UpdateServiceController {
    private AnswerService answerService = new AnswerService();

    @FXML
    private TextField nameTF;
    @FXML
    private TextField priceTF;

    private int id;

    public void show(int id) throws SQLException {
        try {
            this.id = id;
            System.out.println(this.id);
            Service s = answerService.getServiceById(id);
            nameTF.setText(s.getName());
            priceTF.setText(s.getPrice().toString());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void closePopup()  {
        nameTF.getScene().getWindow().hide();
    }


    @FXML
    void updateService(ActionEvent event) throws SQLException, IOException {
        int questionId = answerService.getQuestion(this.id);
        Service s = new Service(this.id, nameTF.getText(), Float.parseFloat(priceTF.getText()));
        answerService.update(s);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("sevice updated successfully.");
        alert.showAndWait();
        closePopup();

    }



    private Stage stage ;
    private Scene scene ;

    private Parent root ;


}
