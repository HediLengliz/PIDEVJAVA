package org.example.controllers.Reclamation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.Reclamation;
import org.example.models.User;
import org.example.services.ReclamationService;
import org.example.services.UserService;
import org.example.utils.CurrentUser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;

public class AddReclamation {
    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button submitButton;
    @FXML
    private Button close;

    private ReclamationService reclamationService;

    public AddReclamation() {
        reclamationService = new ReclamationService(); // You need to implement this class
    }

    @FXML
    private void initialize() {
    }


    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchToSceneOne() throws IOException {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser != null) {


            root = FXMLLoader.load(getClass().getResource("/reclamation/userReclamation.fxml"));
            stage = (Stage) descriptionArea.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }


    }

    @FXML
    private void handleSubmit() {
        String title = titleField.getText();
        String description = descriptionArea.getText();

        if (title.isEmpty() || description.isEmpty() || CurrentUser.getCurrentUser() == null) {
            showAlert("Error", "Please fill in all fields.");
        } else {
            String apiKey = "BvRBPgZPE12rd1Cm0GF9T3YbT9gHcRqW";
            String apiUrl = "https://api.apilayer.com/bad_words?censor_character=*";

            // Construct the request body
            String requestBody = title + " " + description;

            // Create a new HttpRequest

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("apikey", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Send the request asynchronously
            HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(responseBody -> {
                        // Parse the JSON response
                        JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();
                        int badWordsTotal = responseJson.get("bad_words_total").getAsInt();
                        if (badWordsTotal > 0) {
                            JsonArray badWordsList = responseJson.getAsJsonArray("bad_words_list");
                            final String[] finalTitle = {title};
                            final String[] finalDescription = {description};
                            badWordsList.forEach(badWord -> {
                                JsonObject badWordObject = badWord.getAsJsonObject();
                                String word = badWordObject.get("word").getAsString();
                                finalTitle[0] = finalTitle[0].replaceAll("(?i)" + word, "*".repeat(word.length()));
                                finalDescription[0] = finalDescription[0].replaceAll("(?i)" + word, "*".repeat(word.length()));
                            });
                            Platform.runLater(() -> {
                                titleField.setText(finalTitle[0]);
                                descriptionArea.setText(finalDescription[0]);
                                showAlert("Alert", "Bad words detected. Please remove them before submitting.");
                            });
                        } else {
                            Platform.runLater(() -> {
                                try {
                                    addReclamation(title, description);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    showAlert("Error", "An error occurred while adding the reclamation.");
                                }
                            });
                        }
                        return null;
                    })
                    .exceptionally(throwable -> {
                        throwable.printStackTrace();
                        showAlert("Error", "An error occurred while processing the request.");
                        return null;
                    });
        }
    }

    private void addReclamation(String title, String description) throws SQLException {
        User user = getUser(); // Implement this method to get the current user
        if (user != null) {
            Reclamation reclamation = new Reclamation(title, description, user);
            reclamationService.add(reclamation);
            showAlert("Success", "Reclamation added successfully.");
            clearFields();
            handleCloseButtonAction();
        } else {
            showAlert("Error", "User not found.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        titleField.clear();
        descriptionArea.clear();
    }

    private User getUser() throws SQLException {
        UserService u=new UserService();
        User us=u.getById(CurrentUser.getCurrentUser().getId());
        return us;

    }
    @FXML
    private void handleCloseButtonAction() {
        // Get the stage (window) of the current scene and close it
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
}
