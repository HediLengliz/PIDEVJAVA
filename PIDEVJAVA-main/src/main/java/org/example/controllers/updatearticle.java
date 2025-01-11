package org.example.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.Article;
import org.example.services.articleservices;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class updatearticle {
    public Button updatebutton;
    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField authornameField;

    @FXML
    private  TextField categoryField;
            ;

    @FXML
    private DatePicker datepubField;

    private final articleservices articleService = new articleservices();

    private Article articleToUpdate;

    public void setArticleToUpdate(Article article) {
        this.articleToUpdate = article;
        // Populate fields with current article data for editing
        titleField.setText(article.getTitle());
        descriptionField.setText(article.getDescription());
        authornameField.setText(article.getAuthorname());
       datepubField.setValue(article.getDatepub());
       categoryField.setText(article.getCategorie());

    }

    @FXML
    void updateArticle() {
        if (articleToUpdate != null) {
            // Update article properties with new values from the fields
            articleToUpdate.setTitle(titleField.getText());
            articleToUpdate.setDescription(descriptionField.getText());
            articleToUpdate.setAuthorname(authornameField.getText());
            articleToUpdate.setDatepub(datepubField.getValue());
            articleToUpdate.setCategorie(categoryField.getText());
            // Update other article properties with corresponding field values as needed
            try {
                // Call service to update the article in the database
                articleService.update(articleToUpdate);
                //WAIT 10 SECONDS BEFORE CLOSING
                TimeUnit.MILLISECONDS.sleep(1000);

                // Display success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Article updated successfully!");

                Stage stage = (Stage) updatebutton.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                // Display error message if update fails
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update article: " + e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No article selected for update!");
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