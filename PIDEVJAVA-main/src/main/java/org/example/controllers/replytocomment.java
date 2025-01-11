package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.example.models.Article;
import org.example.models.Commentaire;
import org.example.services.articleservices;
import org.example.services.CommentServices;
import org.example.services.IComment;

import java.sql.SQLException;
import java.util.Objects;

public class replytocomment {
    @FXML
    private TextArea descriptionTextArea = new TextArea();
    @FXML
    private Label authornameLabel = new Label();
    @FXML
    private Label commentLabel = new Label();
    public Button replyToComment;

    private Commentaire selectedComment;
    private IComment<Commentaire> commentService;
    private articleservices articleServices = new articleservices();

    public void initialize() throws NullPointerException, SQLException {
        if (selectedComment != null) {
            // Handle the case where no comment is selected, such as showing an error message or disabling certain UI elements.
            throw new NullPointerException("No comment selected");
        }
        try {
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa");
            commentService = new CommentServices();
            displayComment(articleServices.getById(63), selectedComment);
        } catch (NullPointerException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
            commentLabel.setText("Error fetching comment");
            authornameLabel.setText("Author: Unknown");
        }
        displayComment(Objects.requireNonNull(articleServices.getById(63)), (selectedComment));
    }

    public void setSelectedComment(Commentaire selectedComment) {
        this.selectedComment = selectedComment;
    }

    public void setCommentService(IComment<Commentaire> commentService) {
        this.commentService = commentService;
    }
    public void setArticleServices(articleservices articleServices) {
        this.articleServices = articleServices;
    }

    private void displayComment(Article article,Commentaire comment) throws NullPointerException {

        if (selectedComment != null) {
            //initialize comment
            commentLabel.setText("dzezf");
            //initialize article
            authornameLabel.setText("Author: " + article.getAuthorname());
            commentLabel.setText(selectedComment.getDescription());
            authornameLabel.setText("Author: " + article.getAuthorname());
        } else {
            throw new NullPointerException("No comment selected");
        }
    }

    public void replytocommentt(ActionEvent actionEvent) {
        if (commentService != null) {
            String replyDescription = descriptionTextArea.getText();
            try {
                commentService.addC(selectedComment, replyDescription);
                showAlert("Reply added successfully");
                Stage stage = (Stage) replyToComment.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
                showErrorAlert("Error adding reply");
            }
        } else {
            showErrorAlert("No comment selected");
        }
    }

    private void showAlert(String description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(description);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
