package org.example.controllers.sinisterVehicle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.models.SinisterVehicle;
import org.example.services.SinisterVehicleService;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class SinisterVehicleController {
    private final SinisterVehicleService svs = new SinisterVehicleService();
    @FXML
    private Button btnOverview;
    private FileChooser fileChooser;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField LocationTF;

    @FXML
    private DatePicker Date_of_sinisterPicker;

    @FXML
    private TextField Nom_conducteur_aTF;

    @FXML
    private TextField Nom_conducteur_bTF;

    @FXML
    private TextField Prenom_conducteur_bTF;

    @FXML
    private TextField Prenom_conducteur_aTF;

    @FXML
    private TextField Adresse_conducteur_aTF;

    @FXML
    private TextField Adresse_conducteur_bTF;

    @FXML
    private TextField Num_permis_aTF;

    @FXML
    private TextField Num_permis_bTF;

    @FXML
    private DatePicker Delivre_aPicker;

    @FXML
    private DatePicker Delivre_bPicker;

    @FXML
    private TextField Num_contrat_aTF;

    @FXML
    private TextField Num_contrat_bTF;

    @FXML
    private TextField Marque_vehicule_aTF;

    @FXML
    private TextField Marque_vehicule_bTF;

    @FXML
    private TextField Immatriculation_aTF;

    @FXML
    private TextField Immatriculation_bTF;

    @FXML
    private TextField Bvehicule_assure_parTF;

    @FXML
    private TextField AgenceTF;

    private String file;
    @FXML
    private ImageView sinisterImageView;
    @FXML
    public void uploadImage(ActionEvent event) throws MalformedURLException {

        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.file = file.toString();
            Image image = new Image(file.toURI().toURL().toString());
            sinisterImageView.setImage(image);
        }
    }

    @FXML
    void addSinisterVehicle(ActionEvent event) throws SQLException {
        if (sinisterImageView != null && sinisterImageView.getImage() != null) {
            try {
                // Get the image file path
                String imagePath = sinisterImageView.getImage().getUrl();

                // Check if the source file exists
                File sourceFile = new File(this.file);
                if (!sourceFile.exists()) {
                    System.out.println("Source file does not exist.");
                    return; // Exit the method if source file does not exist
                }

                // Prepare destination directory
                File destinationDir = new File("resources/images/profiles/");
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

                // Set the imagePath to the SinisterVehicle constructor
                String image = destFile.getAbsolutePath(); // Use the absolute path of the destination file
                LocalDate dateOfSinister = Date_of_sinisterPicker.getValue();
                SinisterVehicle sinisterVehicle = new SinisterVehicle(
                        dateOfSinister,
                        LocationTF.getText(),
                        Nom_conducteur_aTF.getText(),
                        Nom_conducteur_bTF.getText(),
                        Prenom_conducteur_bTF.getText(),
                        Prenom_conducteur_aTF.getText(),
                        Adresse_conducteur_aTF.getText(),
                        Adresse_conducteur_bTF.getText(),
                        Num_permis_aTF.getText(),
                        Num_permis_bTF.getText(),
                        Delivre_aPicker.getValue(),
                        Delivre_bPicker.getValue(),
                        Num_contrat_aTF.getText(),
                        Num_contrat_bTF.getText(),
                        Marque_vehicule_aTF.getText(),
                        Marque_vehicule_bTF.getText(),
                        Immatriculation_aTF.getText(),
                        Immatriculation_bTF.getText(),
                        Bvehicule_assure_parTF.getText(),
                        AgenceTF.getText(),
                        fileName // Pass the imagePath here
                );

                svs.add(sinisterVehicle);
                showSuccessMessage();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to copy the image file.");
            }
        }
    }

    @FXML
    void nextPage(ActionEvent event) {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (currentIndex < tabPane.getTabs().size() - 1) {
            tabPane.getSelectionModel().select(currentIndex + 1);
        }
    }

    @FXML
    void previousPage(ActionEvent event) {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (currentIndex > 0) {
            tabPane.getSelectionModel().select(currentIndex - 1);
        }
    }

    private void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Sinister added successfully!");
        alert.showAndWait();
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void backHome(ActionEvent event) {
        openWindow("/home.fxml", "List Vehicle Request");
    }
    @FXML
    void goProfile(ActionEvent event) {
        openWindow("/users/profil.fxml", "List Life Request");
    }



    @FXML
    private Pane pnItems = null;
    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    private void openWindow(String fxmlFilePath, String title) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFilePath));
            Stage stage = (Stage) pnItems.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
