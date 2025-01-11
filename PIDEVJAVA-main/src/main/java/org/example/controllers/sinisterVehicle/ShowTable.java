package org.example.controllers.sinisterVehicle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.controllers.rapport.SinisterReportDetailsController;
import org.example.controllers.rapport.addRapportController;
import org.example.models.Reclamation;
import org.example.models.ServiceVehicle;
import org.example.models.SinisterVehicle;
import org.example.models.User;
import org.example.services.SinisterVehicleService;
import org.example.services.UserService;
import org.example.utils.CurrentUser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShowTable implements Initializable {
    @FXML
    private VBox pnItems = null;
    public void displayUserReclamations() {
        try {
            SinisterVehicleService r = new SinisterVehicleService();
            List<SinisterVehicle> sinisters = r.getAll();
            System.out.println(sinisters);

            FXMLLoader l = new FXMLLoader(getClass().getResource("/sinisterVehicle/itemsH.fxml"));
            HBox it = l.load();

            Label idL = (Label) it.lookup("#id");
            Label titleL = (Label) it.lookup("#titre");
            Label dateL = (Label) it.lookup("#date");
            Button btn = (Button) it.lookup("#btn");


            btn.setText("ACTIONS");
            btn.setStyle(" -fx-border-color: transparent;-fx-fill: white; -fx-padding: 0;");
            idL.setText("ID");
            titleL.setText("TITLE");
            dateL.setText("DATE");
            idL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            // Style for TITLE label
            titleL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");

            // Style for DATE label
            dateL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            btn.setStyle("-fx-background-color: transparent; " +
                    "-fx-border-color: transparent; " +
                    "-fx-padding: 0; " +
                    "-fx-text-fill: #FF5733; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: bold;");

            pnItems.getChildren().add(it);

            for (int i = 0; i < sinisters.size(); i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sinisterVehicle/itemsH.fxml"));
                HBox item = loader.load();

                Label idLabel = (Label) item.lookup("#id");
                Label titleLabel = (Label) item.lookup("#titre");
                Label dateLabel = (Label) item.lookup("#date");
                Button b1 = (Button) item.lookup("#btn");

                Button b2 = (Button) item.lookup("#btn2");
                b1.setText("show");

                b2.setVisible(true);
                SinisterVehicle reclamation = sinisters.get(i);

                idLabel.setText(String.valueOf(reclamation.getId()));
                titleLabel.setText(reclamation.getStatus_sinister());
                dateLabel.setText(reclamation.getDate_sinister().toString());

                // Add mouse hover effects
                //                item.setOnMouseEntered(event -> {
                //                    item.setStyle("-fx-background-color : #c6c8ce");
                //                });
                //                item.setOnMouseExited(event -> {
                //                    item.setStyle("-fx-background-color : #9a9bad");
                //                });

                // Add click event handler to show details

                item.setOnMouseClicked(event -> {

                });
                b1.setStyle( "-fx-background-color: #295229; " +
                        "-fx-border-color: transparent; " +
                        "-fx-padding: 0; " +
                        "-fx-text-fill: #FFF; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;");

                if (reclamation.getStatus_sinister().equals("traité")) {
                    b2.setText("View Report");
                    b2.setStyle( "-fx-background-color: #34437a; " +
                            "-fx-border-color: transparent; " +
                            "-fx-padding: 0; " +
                            "-fx-text-fill: #FFF; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold;");
                }
                b2.setOnAction(event -> {
                    try {
                        // Check the status of the sinister
                        if (reclamation.getStatus_sinister().equals("traité")) {

                            b2.setText("View Report");
                            // Load the report page
                            FXMLLoader reportLoader = new FXMLLoader(getClass().getResource("/rapport/sinisterRapportDetails.fxml"));
                            Parent reportRoot = reportLoader.load();
                            // Optionally pass any data to the report controller
                            SinisterReportDetailsController reportController = reportLoader.getController();
                            reportController.setSinisterReportDetails((reclamation.getId().intValue()));

                            // Create a new stage for the report view
                            Stage reportStage = new Stage();
                            reportStage.setScene(new Scene(reportRoot));
                            reportStage.show();
                        } else {
                            // If status is not "traité", proceed with deletion
                            b2.setText("delete");
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText("Confirm Deletion");
                            alert.setContentText("Are you sure you want to delete this item?");
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    try {
                                        SinisterVehicleService rs = new SinisterVehicleService();
                                        rs.delete(reclamation.getId().intValue());
                                        // Refresh the UI after deletion
                                        refreshHBoxItems(pnItems);
                                    } catch (SQLException ex) {
                                        System.out.println(ex.getMessage());
                                    }
                                }
                            });
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                });

                b1.setOnAction(event -> {
                    try {
                        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/sinisterVehicle/viewSVDet.fxml"));
                        Parent root = loader2.load();

                        // Get the controller associated with the sinisterDetails.fxml
                        SinisterDetailsVController sinisterDetailsController = loader2.getController();

                        // Pass the selected SinisterProperty object to the SinisterDetailsController
                        sinisterDetailsController.setSinisterDetails((reclamation.getId().intValue()));

                        // Create a new stage for the details view
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                pnItems.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String imageUUID = CurrentUser.getCurrentUser().getImage(); // Assuming this returns the UUID
        System.out.println(imageUUID);
        if (imageUUID != null && !imageUUID.isEmpty()) {
            String imagePath = "src/main/resources/images/profiles/" + imageUUID ; // Adjust file extension if necessary
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toURL().toString());
                    profilePicImageView.setImage(image);
                } else {
                    // Handle case where profile picture file does not exist
                    System.out.println("Profile picture file not found at: " + imagePath);
                }
            } catch (MalformedURLException e) {
                // Handle malformed URL exception
                e.printStackTrace();
            }
        } else {
            // Handle case where UUID is null or empty
            System.out.println("Profile picture UUID is null or empty.");
        }

        displayUserReclamations();
    }


    public void refreshHBoxItems(VBox hBox) {
        try {
            SinisterVehicleService r = new SinisterVehicleService();
            List<SinisterVehicle> sinisters = r.getAll();
            // Clear the existing items in the HBox
            hBox.getChildren().clear();
            FXMLLoader l = new FXMLLoader(getClass().getResource("/sinisterVehicle/itemsH.fxml"));
            HBox it = l.load();

            Label idL = (Label) it.lookup("#id");
            Label titleL = (Label) it.lookup("#titre");
            Label dateL = (Label) it.lookup("#date");
            Button btn = (Button) it.lookup("#btn");


            btn.setText("ACTIONS");
            btn.setStyle(" -fx-border-color: transparent;-fx-fill: white; -fx-padding: 0;");
            idL.setText("ID");
            titleL.setText("TITLE");
            dateL.setText("DATE");
            idL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            // Style for TITLE label
            titleL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");

            // Style for DATE label
            dateL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            btn.setStyle("-fx-background-color: transparent; " +
                    "-fx-border-color: transparent; " +
                    "-fx-padding: 0; " +
                    "-fx-text-fill: #FF5733; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: bold;");

            pnItems.getChildren().add(it);

            for (int i = 0; i < sinisters.size(); i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sinisterVehicle/itemsH.fxml"));
                HBox item = loader.load();

                Label idLabel = (Label) item.lookup("#id");
                Label titleLabel = (Label) item.lookup("#titre");
                Label dateLabel = (Label) item.lookup("#date");
                Button b1 = (Button) item.lookup("#btn");

                Button b2 = (Button) item.lookup("#btn2");
                b1.setText("show");

                b2.setVisible(true);
                SinisterVehicle reclamation = sinisters.get(i);

                idLabel.setText(String.valueOf(reclamation.getId()));
                titleLabel.setText(reclamation.getStatus_sinister());
                dateLabel.setText(reclamation.getDate_sinister().toString());

                // Add mouse hover effects
                //                item.setOnMouseEntered(event -> {
                //                    item.setStyle("-fx-background-color : #c6c8ce");
                //                });
                //                item.setOnMouseExited(event -> {
                //                    item.setStyle("-fx-background-color : #9a9bad");
                //                });

                // Add click event handler to show details

                item.setOnMouseClicked(event -> {

                });

                if (reclamation.getStatus_sinister().equals("traité")) {
                    b2.setText("View Report");
                    b2.setStyle( "-fx-background-color: #34437a; " +
                            "-fx-border-color: transparent; " +
                            "-fx-padding: 0; " +
                            "-fx-text-fill: #FFF; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold;");
                }
                b2.setOnAction(event -> {
                    try {
                        // Check the status of the sinister
                        if (reclamation.getStatus_sinister().equals("traité")) {

                            b2.setText("View Report");
                            // Load the report page
                            FXMLLoader reportLoader = new FXMLLoader(getClass().getResource("/rapport/sinisterRapportDetails.fxml"));
                            Parent reportRoot = reportLoader.load();
                            // Optionally pass any data to the report controller
                            SinisterReportDetailsController reportController = reportLoader.getController();
                            // Perform any necessary actions related to viewing the report

                            // Create a new stage for the report view
                            Stage reportStage = new Stage();
                            reportStage.setScene(new Scene(reportRoot));
                            reportStage.show();
                        } else {
                            // If status is not "traité", proceed with deletion
                            b2.setText("delete");
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText("Confirm Deletion");
                            alert.setContentText("Are you sure you want to delete this item?");
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    try {
                                        SinisterVehicleService rs = new SinisterVehicleService();
                                        rs.delete(reclamation.getId().intValue());
                                        // Refresh the UI after deletion
                                        refreshHBoxItems(pnItems);
                                    } catch (SQLException ex) {
                                        System.out.println(ex.getMessage());
                                    }
                                }
                            });
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                });

                b1.setOnAction(event -> {
                    try {
                        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/sinisterVehicle/viewSVDet.fxml"));
                        Parent root = loader2.load();

                        // Get the controller associated with the sinisterDetails.fxml
                        SinisterDetailsVController sinisterDetailsController = loader2.getController();

                        // Pass the selected SinisterProperty object to the SinisterDetailsController
                        sinisterDetailsController.setSinisterDetails((reclamation.getId().intValue()));

                        // Create a new stage for the details view
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                pnItems.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    ArrayList<Node> nodes = new <Node>ArrayList();


    @FXML
    private Button btnListPropertyRequest;

    @FXML
    private Button btnListLifeRequest;

    @FXML
    private Button btnListVehicleRequest;


    @FXML
    void listLifeRequest(ActionEvent event)
    {
        openWindow("/LifeRequest/test.fxml", "User Informations");
    }

    @FXML
    void userInformations(ActionEvent event)
    {
        openWindow("/users/userInfo.fxml", "User Informations");
    }
    @FXML
    void goToChangePassword(){
        System.out.println("aaa");
        openWindow("/users/changePassword.fxml", "User Informations");
    }

    @FXML
    void listPropertyRequest(ActionEvent event) {
        openWindow("/PropertyRequest/HboxGetProp.fxml", "List Property Request");
    }
    @FXML
    void listVehicleRequest(ActionEvent event) {
        openWindow("/VehicleRequest/HboxGetVehi.fxml", "List Vehicle Request");
    }

    @FXML
    void listUserReclamations(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/reclamation/userReclamations.fxml", "User Reclamations");
    }
    @FXML
    void listSinistreHabitat(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/sinisterProperty/showTable.fxml", "User Reclamations");
    }
    @FXML
    void listSinistreVehicule(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/sinisterVehicle/showTableV.fxml", "User Reclamations");
    }
    @FXML
    void listSinistreMaladie(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/ShowSinisterLifeForUSer.fxml", "User Reclamations");
    }

    @FXML
    void listDevis(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/test2.fxml", "User Reclamations");
    }
    @FXML
    void contrat(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/ContratHabitat/ContratInUser.fxml", "User Reclamations");
    }


    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    private void openWindow(String fxmlFilePath, String title) {
        try {
            root =FXMLLoader.load(getClass().getResource(fxmlFilePath));
            Stage stage = (Stage) pnItems.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    ImageView profilePicImageView;
    @FXML
    private Button btnOverview;
    private FileChooser fileChooser;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;
    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;


    private final UserService userService = new UserService();
    private User selectedUser; // Holds the selected user for updating

    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK))
            {
                pnItems.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/users/login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






}
