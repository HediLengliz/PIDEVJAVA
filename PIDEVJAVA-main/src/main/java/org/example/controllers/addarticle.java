package org.example.controllers;
//import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.models.Article;
import org.example.services.articleservices;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import org.example.utils.MyDatabase;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.UUID;

public class addarticle {
    public TextField titleField;
    public TextField authornameField;
    public TextField categoryField;
    public Button browseImageButton = new Button();
    public DatePicker datePicker;
    public TextArea descriptionArea;
    public DatePicker datePub;
    @FXML
    private ImageView imageView;

    @FXML
    private Button uploadButton = new Button();

    @FXML
    private Button cancelButton;

    private File selectedImageFile;
    @FXML
    private Button addbutton;

    @FXML
    private Label welcomeText;
    private final articleservices as = new articleservices();
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    public void initialize()  {
        try {
            // Load the default image
            Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/main/resources/imgs/img1.jpg")));

            // Set the default image to the imageView
            imageView.setImage(defaultImage);

        } catch (NullPointerException e) {
            System.err.println("Default image not found: " + e.getMessage());
        }
    }
    public void saveProfilePicture() {
        if (imageView != null && imageView.getImage() != null) {
            try {
                // Get the image file path
                String imagePath = imageView.getImage().getUrl();
                if (imagePath.startsWith("file:/")) {
                    // Remove "file:/" prefix and decode URL
                    imagePath = java.net.URLDecoder.decode(imagePath.substring(6), "UTF-8");
                }

                // Check if the source file exists
                File sourceFile = new File(imagePath);
                if (!sourceFile.exists()) {
                    System.out.println("Source file does not exist.");
                    return; // Exit the method if source file does not exist
                }

                // Prepare destination directory
                File destinationDir = new File("src/main/resources/imgs/");
                if (!destinationDir.exists()) {
                    destinationDir.mkdirs();
                }

                // Generate a UUID for the file name
                String fileExtension = imagePath.substring(imagePath.lastIndexOf('.'));
                String uuid = UUID.randomUUID().toString();
                String fileName = uuid + fileExtension;

                // Copy the file to the destination directory with the UUID as the file name
                File destFile = new File(destinationDir, fileName);
                Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Update the profile picture path in the database
                String updateQuery = "UPDATE user SET image_file_name = ? WHERE id = ?";
                PreparedStatement preparedStatement = MyDatabase.getInstance().getConnection().prepareStatement(updateQuery);
                preparedStatement.setString(1, fileName);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Profile picture path updated in database.");
                } else {
                    System.out.println("Failed to update profile picture path in database.");
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No profile picture to save.");
        }
    }
    public void add() {
        try {
            // Get the data from the fields
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String authorname = authornameField.getText().trim();
            LocalDate datepub = datePicker.getValue();
            String categorie = categoryField.getText().trim();
            String image = null;
            if (imageView != null && imageView.getImage() != null) {
                try {
                    // Get the image file path
                    String imagePath = imageView.getImage().getUrl();
                    if (imagePath.startsWith("file:/")) {
                        // Remove "file:/" prefix and decode URL
                        imagePath = java.net.URLDecoder.decode(imagePath.substring(6), "UTF-8");
                    }

                    // Check if the source file exists
                    File sourceFile = new File(imagePath);
                    if (!sourceFile.exists()) {
                        System.out.println("Source file does not exist.");
                        return; // Exit the method if source file does not exist
                    }

                    // Prepare destination directory
                    File destinationDir = new File("src/main/resources/imgs/");
                    if (!destinationDir.exists()) {
                        destinationDir.mkdirs();
                    }

                    // Generate a UUID for the file name
                    String fileExtension = imagePath.substring(imagePath.lastIndexOf('.'));
                    String uuid = UUID.randomUUID().toString();
                    String fileName = uuid + fileExtension;

                    // Copy the file to the destination directory with the UUID as the file name
                    File destFile = new File(destinationDir, fileName);
                    Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);


                    if (selectedImageFile != null) {

                        image = fileName;
                    }

                    // Check if all fields are filled
                    if (title.isEmpty() || description.isEmpty() || authorname.isEmpty() || categorie.isEmpty()) {
                        showAlert("Error", "All fields are required.");
                        return;
                    }

                    // Create a new Article object
                    Article article = new Article(title, description, authorname, datepub, categorie, image);

                    // Add the article to the database
                    as.add(article);

                    // Display a success alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Article added successfully!");
                    alert.showAndWait();

                    // Wait 10 seconds before closing
                    waitAlert(alert, 1000);

                    // Clear fields after adding article
                    clearFields();

                    // Close the window after adding
                    Stage stage = (Stage) addbutton.getScene().getWindow();
                    stage.close();

                } catch (SQLException e) {
                    // Display an error alert in case of SQL error
                    showErrorAlert("Error adding Article: " + e.getMessage());
                } catch (DateTimeParseException e) {
                    showErrorAlert("Invalid date! Please enter a valid date.");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

            private  void waitAlert(Alert alert, int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        titleField.clear();
        descriptionArea.setText(null);
        authornameField.clear();
        datePicker.setValue(LocalDate.now());
        browseImageButton.disableProperty();
        categoryField.clear();
    }
    @FXML
    public void annuler(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait();
        waitAlert(alert, 1000);
        try {
            if (alert.getResult() == ButtonType.OK) {
                clearFields();
                // Charger le fichier FXML d'adduser
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/listarticle.fxml"));
                Parent root = loader.load();

                // Créer une nouvelle scène avec le contenu de adduser
                Scene scene = new Scene(root);

                // Obtenir la fenêtre actuelle à partir de l'événement
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                // Définir la nouvelle scène
                stage.setScene(scene);
                stage.show();

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void getAll() {
    }

    public void uploadImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        selectedImageFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (selectedImageFile != null) {
            try {
                String imageFileLocation = selectedImageFile.toURI().toString();
                Image image = new Image(imageFileLocation);
                imageView.setImage(image);
                // Save the file path (you'll need it when inserting/updating the Article in the DB)
                browseImageButton.setText(selectedImageFile.getAbsolutePath());
            } catch (Exception e) {
                showErrorAlert("Error loading image: " + e.getMessage());
            }
        }

    }

}