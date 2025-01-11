package org.example.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.models.Commentaire;
import org.controlsfx.control.Rating;
import org.example.services.CommentServices;

public class updatecomment {
    public Rating Rate;
    public Button updateButton;
    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField RateField = new TextField();

    private Commentaire commentToUpdate;

    private final CommentServices commentService = new CommentServices();

    public void setCommentToUpdate(Commentaire comment) {
        this.commentToUpdate = comment;
        // Populate fields with current comment data for editing
        descriptionTextArea.setText(comment.getDescription());
        RateField.setText(String.valueOf(comment.getRate()));

    }

    @FXML
    void updateComment(){
        if (commentToUpdate != null) {
            // Update comment properties with new values from the fields
            commentToUpdate.setDescription(descriptionTextArea.getText());
            commentToUpdate.setRate(Rate.getMax());

            // Call service to update the comment in the database
            commentService.update((Commentaire) commentToUpdate);

            // Display success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Comment updated successfully!");

            // Close the window after 1 second
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> {
                Stage stage = (Stage) updateButton.getScene().getWindow();
                stage.close();
            });
            delay.play();

        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No comment selected for update!");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void getAll() {
    }

}
