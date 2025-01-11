package org.example.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.models.Article;
import org.example.models.Commentaire;
import org.controlsfx.control.Rating;
import org.example.services.CommentServices;
import org.example.services.articleservices;
import static javafx.collections.FXCollections.observableArrayList;



import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class commentsection implements Initializable {

    @FXML
    private Rating rating;

    @FXML
    private TableColumn<Commentaire, Double> ratingColumn;
    @FXML
    private TableColumn<Commentaire, String> descriptionColumn;

    public TableView<Commentaire> TableView = new TableView();

    @FXML
    public ImageView articleImage = new ImageView();
    @FXML
    public Label titleLabel = new Label();
    @FXML
    public Label authorLabel = new Label();
    @FXML
    public Label categoryLabel = new Label();
    @FXML
    public Label dateLabel = new Label();

    //    public Label image = new ImageView();
    @FXML
    public TextArea descriptionArea = new TextArea();
    @FXML
    public TextArea commentTextArea = new TextArea();
    @FXML
    public VBox commentContainer;
    private final List<Commentaire> comments = new ArrayList<Commentaire>();
    private final CommentServices commentServices = new CommentServices();
    private final articleservices articleServices = new articleservices();
    public Label descriptionLabel = new Label();
    public Label commentLabel = new Label();
    public Button replyToCommentButton = new Button();
    public Button ReplyToComment;
    public Article selectedArticle;
    private Object addcomment;
    private Commentaire selectedComment = new Commentaire();


    ObservableList<Commentaire> observableComments = observableArrayList(comments);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<Commentaire> comments = commentServices.getAll();
            observableComments.setAll(comments); // Update observableComments with fetched comments
            TableView.setItems(observableComments); // Set items to TableView
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }
    }

    private void displayArticleDetails(Article article) {
        titleLabel.setText(article.getTitle());
        authorLabel.setText(article.getAuthorname());
        categoryLabel.setText(article.getCategorie());

        dateLabel.setText(article.getDatepub().toString()); // Format the date appropriately
        descriptionLabel.setText(article.getDescription());
    }
    private void displayCommentDetails(Commentaire comment)  {
        commentLabel.setText(comment.getDescription());
        rating.setRating(comment.getRate());
    }
    private void refreshComments() throws SQLException {
        try {
            List<Commentaire> comments = commentServices.getAll();
            observableComments.setAll(comments); // Update observableComments with fetched comments
            TableView.setItems(observableComments); // Set items to TableView
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }
    }


    public void postComment(ActionEvent actionEvent) {
        if (commentTextArea.getText().isEmpty()) {
            // Handle the case where no article is selected or input is empty
            return;
        }

        // Create a new Commentaire object with a fresh ID and the provided rate and text.
        String commentText = commentTextArea.getText();
        int rate = (int) rating.getRating(); // Ensure this converts the rating properly
        Commentaire newComment = new Commentaire(generateCommentID(), commentText, rate, 10);

        try {
            System.out.println(newComment);
            commentServices.add(newComment);
            commentTextArea.clear();
            refreshComments(); // Refresh comments to include the new one
        } catch (SQLException e) {
            // Log and handle the exception
            e.printStackTrace();
        }
    }

    private int generateCommentID() {
        // Generate a random ID for the comment
        // You may need to adjust this based on your application's requirements
        return (int) (Math.random() * 10000) + 1;
    }


    private int generateInitialRate() {
        // Generate a random initial rate for the comment
        // You may need to adjust this based on your application's requirements
        return (int) (Math.random() * 5) + 1;

    }

    private Node createCommentNode(Commentaire newComment) {
        VBox commentNode = new VBox();
        commentNode.getStyleClass().add("comment");

        Label commentLabel = new Label(newComment.getDescription());
        commentLabel.getStyleClass().add("comment-text");

        commentNode.getChildren().addAll(commentLabel);

        return commentNode;
    }


    public void ReplyComment(ActionEvent actionEvent) throws Exception {
        Commentaire selectedComment = TableView.getSelectionModel().getSelectedItem();
        if (selectedComment == null) {
            System.err.println("Selected comment is null. Cannot reply to comment.");
            return;
        }
        Stage replyStage = new Stage();
        replyStage.setTitle("Reply to Comment: " + selectedComment.getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/replytocomment.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            replyStage.setScene(scene);
            replyStage.setTitle("Reply to Comment");
            replyStage.show();
            ObservableList<Commentaire> comments = TableView.getItems();
            comments.add(selectedComment);
            TableView.setItems(comments);
            TableView.refresh();
            replyStage.close();
            replytocomment replyCommentController = loader.getController();
            replyCommentController.setSelectedComment(selectedComment);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

