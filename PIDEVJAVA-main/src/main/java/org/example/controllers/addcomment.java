package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.models.Commentaire;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.Rating;
import org.example.services.CommentServices;
import org.example.services.IComment;

import java.sql.SQLException;

import java.util.Objects;


public class addcomment {
    public Button postComment;
    @FXML
    private Label comment;
    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ImageView profileImage;

    @FXML
    private Rating rating;
    @FXML
    private Label username;


    private IComment commentService;
    CommentServices commentServices = new CommentServices();


    private void showAlert(String Description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(Description);
        alert.showAndWait();
        //wait 1 sec
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        //wait 1 sec
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void clearFields() {
        descriptionTextArea.clear();
        rating.setRating(2.5);
    }

    public void postComment(javafx.event.ActionEvent actionEvent)throws SQLException{
        {
            //use the add function provided in org.example.services to add a comment via postcomment button
            Commentaire comment = new Commentaire();
            comment.setDescription(descriptionTextArea.getText());
            comment.setRating(rating.getRating());
            commentServices.add(comment);

            if (descriptionTextArea.getText().isEmpty()){
                showErrorAlert(("Error All fields are required."));
                return;
            }
            //success alert
            showAlert("Success Comment added successfully");
            clearFields();
            //close the scene editor
            Stage stage = (Stage) postComment.getScene().getWindow();
            stage.close();

            //error alert
            if (Objects.isNull(comment))
            {
                showErrorAlert("Error Comment not added successfully");
            }
        }
    }
    @FXML
    void RATE(MouseEvent event) {
        rating.setPartialRating(true);


    }
}
