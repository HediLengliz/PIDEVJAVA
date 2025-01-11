package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.models.Service;
import org.example.services.AnswerService;

import java.io.IOException;
import java.sql.SQLException;

public class ShowServiceController {
    private AnswerService answerService =new AnswerService();

    @FXML
    private Label nameLabel;
    @FXML
    private Label priceLabel;
    private int id ;

    public void show(int id) throws SQLException {
        try {
            this.id = id;
            Service s = answerService.getServiceById(id);
            nameLabel.setText(s.getName());
            priceLabel.setText(s.getPrice().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void closePopup() throws SQLException {
        nameLabel.getScene().getWindow().hide();
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


    public void deleteService(ActionEvent actionEvent) throws SQLException, IOException {
        int id = answerService.getQuestion(this.id);
        answerService.delete(this.id);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("service deleted successfully.");
        alert.showAndWait();
        closePopup();
    }
}
