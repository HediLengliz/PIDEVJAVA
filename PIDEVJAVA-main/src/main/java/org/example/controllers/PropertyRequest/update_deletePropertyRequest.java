package org.example.controllers.PropertyRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.models.PropretyRequest;
import org.example.models.User;
import org.example.services.PropertyRequestService;
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


public class update_deletePropertyRequest implements Initializable {


    @FXML
    private VBox pnItems = null;

    @FXML
    private ScrollPane pnlReclamation;


    public void afficherPropertyRequest() {
        try {

            PropertyRequestService propertyRequestService = new PropertyRequestService();
            User user = CurrentUser.getCurrentUser();

            List<PropretyRequest> propretyRequests = propertyRequestService.getByUserId(user.getId());

            FXMLLoader l = new FXMLLoader(getClass().getResource("/PropertyRequest/PropertyRequestUser.fxml"));
            HBox it = l.load();

            Label idL = (Label) it.lookup("#id");
            Label statusL = (Label) it.lookup("#status");
            Label dateL = (Label) it.lookup("#dateRequest");
            Label propertyFormL = (Label) it.lookup("#propertyForm");
            Label numberRoomsL = (Label) it.lookup("#numberRooms");
            Label addressL = (Label) it.lookup("#address");
            Label propertyValueL = (Label) it.lookup("#propertyValue");
            Label surfaceL = (Label) it.lookup("#surface");

            Button btn = (Button) it.lookup("#btn");
            btn.setText("ACTIONS");
            btn.setStyle(" -fx-border-color: transparent;-fx-fill: white; -fx-padding: 0;");
            idL.setText("ID");
            statusL.setText("Status");
            dateL.setText("Date");
            propertyFormL.setText("Type");
            numberRoomsL.setText("Nb piéce");
            addressL.setText("Adresse");
            propertyValueL.setText("Valeur");
            surfaceL.setText("Surface");

            idL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-padding: 0 0 0 20;");
            statusL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; ");
            propertyFormL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-padding: 0 5 0 0;");
            dateL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            numberRoomsL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            addressL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            propertyValueL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            surfaceL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");

            // Style for DATE label
            dateL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            btn.setStyle("-fx-background-color: transparent; " +
                    "-fx-border-color: transparent; " +
                    "-fx-padding: 0; " +
                    "-fx-text-fill: #301bef; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: bold;");
            pnItems.getChildren().add(it);
            System.out.println(propretyRequests.size());

            for (int i = 0; i < propretyRequests.size(); i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PropertyRequest/PropertyRequestUser.fxml"));
                HBox item = loader.load();

                Label idLabel = (Label) item.lookup("#id");
                Label statusLabel = (Label) item.lookup("#status");
                Label dateLabel = (Label) item.lookup("#dateRequest");
                Label propertyFormLabel = (Label) item.lookup("#propertyForm");
                Label numberRoomsLabel = (Label) item.lookup("#numberRooms");
                Label addressLabel = (Label) item.lookup("#address");
                Label propertyValueLabel = (Label) item.lookup("#propertyValue");
                Label surfaceLabel = (Label) item.lookup("#surface");



                PropretyRequest propretyRequest = propretyRequests.get(i);


                idLabel.setText(String.valueOf(propretyRequest.getId_property()));
                statusLabel.setText(propretyRequest.getStatus());
                dateLabel.setText(propretyRequest.getDateRequest().toString());
                propertyFormLabel.setText(propretyRequest.getPropertyForm());
                numberRoomsLabel.setText(propretyRequest.getNumberRooms());
                addressLabel.setText(propretyRequest.getAddress());
                propertyValueLabel.setText(propretyRequest.getPropertyValue());
                surfaceLabel.setText(propretyRequest.getSurface());

                System.out.println(statusLabel.getText());
                // Add mouse hover effects
                //                item.setOnMouseEntered(event -> {
                //                    item.setStyle("-fx-background-color : #c6c8ce");
                //                });
                //                item.setOnMouseExited(event -> {
                //                    item.setStyle("-fx-background-color : #9a9bad");
                //                });

                // Add click event handler to show details

//                //item.setOnMouseClicked(event -> {
//                    try {
//                        FXMLLoader detailsLoader = new FXMLLoader(getClass().getResource("/reclamation/showReclamationDetals.fxml"));
//                        Parent detailsRoot = detailsLoader.load();
//                        ShowReclamationDetals detailsController = detailsLoader.getController();
//                        detailsController.setTitleField(reclamation.getTitle());
//                        detailsController.setDescriptionArea(reclamation.getDescription());
//                        detailsController.setDatereclamation(String.valueOf(reclamation.getDateReclamation()));
//
//                        Stage detailsStage = new Stage();
//                        detailsStage.setScene(new Scene(detailsRoot));
//                        detailsStage.initStyle(StageStyle.UTILITY);
//                        detailsStage.show();
//
//                    } catch (IOException ex) {
//                        System.out.println(ex.getMessage());
//                    }
//                });

                Button b = (Button) item.lookup("#btn");
                b.setOnAction(event -> {
                    try {
                        System.out.println("aa");
                        if (propretyRequest != null) {
                            // Create a confirmation dialog
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText("Confirm Deletion");
                            alert.setContentText("Are you sure you want to delete this item?");

                            // Show the dialog and wait for the user's response
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    try {
                                        PropertyRequestService rs = new PropertyRequestService();
                                        rs.delete(propretyRequest.getId_property());
                                        System.out.println(propretyRequest.getId_property());
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

                pnItems.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshHBoxItems(VBox hBox) {
        try {
            PropertyRequestService r = new PropertyRequestService();
            User user = CurrentUser.getCurrentUser();

            List<PropretyRequest> propretyRequests = r.getByUserId(user.getId());

            hBox.getChildren().clear();
            FXMLLoader l = new FXMLLoader(getClass().getResource("/PropertyRequest/PropertyRequestUser.fxml"));
            HBox it = l.load();
            Label idL = (Label) it.lookup("#id");
            Label statusL = (Label) it.lookup("#status");
            Label dateL = (Label) it.lookup("#dateRequest");
            Label propertyFormL = (Label) it.lookup("#propertyForm");
            Label numberRoomsL = (Label) it.lookup("#numberRooms");
            Label addressL = (Label) it.lookup("#address");
            Label propertyValueL = (Label) it.lookup("#propertyValue");
            Label surfaceL = (Label) it.lookup("#surface");

            Button btn = (Button) it.lookup("#btn");
            btn.setText("ACTIONS");
            btn.setStyle(" -fx-border-color: transparent;-fx-fill: white; -fx-padding: 0;");
            idL.setText("ID");
            statusL.setText("Status");
            dateL.setText("Date");
            propertyFormL.setText("Type");
            numberRoomsL.setText("Nb piéce");
            addressL.setText("Adresse");
            propertyValueL.setText("Valeur");
            surfaceL.setText("Surface");

            idL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-padding: 0 0 0 20;");
            statusL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; ");
            propertyFormL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-padding: 0 5 0 0;");
            dateL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            numberRoomsL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            addressL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            propertyValueL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            surfaceL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");

            // Style for DATE label
            dateL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            btn.setStyle("-fx-background-color: transparent; " +
                    "-fx-border-color: transparent; " +
                    "-fx-padding: 0; " +
                    "-fx-text-fill: #301bef; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: bold;");
            pnItems.getChildren().add(it);

            // Add the updated list of items to the HBox
            for (PropretyRequest propretyRequest : propretyRequests) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PropertyRequest/PropertyRequestUser.fxml"));
                HBox item = loader.load();

                Label idLabel = (Label) item.lookup("#id");
                Label statusLabel = (Label) item.lookup("#status");
                Label dateLabel = (Label) item.lookup("#dateRequest");
                Label propertyFormLabel = (Label) item.lookup("#propertyForm");
                Label numberRoomsLabel = (Label) item.lookup("#numberRooms");
                Label addressLabel = (Label) item.lookup("#address");
                Label propertyValueLabel = (Label) item.lookup("#propertyValue");
                Label surfaceLabel = (Label) item.lookup("#surface");



                idLabel.setText(String.valueOf(propretyRequest.getId_property()));
                statusLabel.setText(propretyRequest.getStatus());
                dateLabel.setText(propretyRequest.getDateRequest().toString());
                propertyFormLabel.setText(propretyRequest.getPropertyForm());
                numberRoomsLabel.setText(propretyRequest.getNumberRooms());
                addressLabel.setText(propretyRequest.getAddress());
                propertyValueLabel.setText(propretyRequest.getPropertyValue());
                surfaceLabel.setText(propretyRequest.getSurface());

                System.out.println(statusLabel.getText());
                // Add mouse hover effects
                //                item.setOnMouseEntered(event -> {
                //                    item.setStyle("-fx-background-color : #c6c8ce");
                //                });
                //                item.setOnMouseExited(event -> {
                //                    item.setStyle("-fx-background-color : #9a9bad");
                //                });

                // Add click event handler to show details

//                //item.setOnMouseClicked(event -> {
//                    try {
//                        FXMLLoader detailsLoader = new FXMLLoader(getClass().getResource("/reclamation/showReclamationDetals.fxml"));
//                        Parent detailsRoot = detailsLoader.load();
//                        ShowReclamationDetals detailsController = detailsLoader.getController();
//                        detailsController.setTitleField(reclamation.getTitle());
//                        detailsController.setDescriptionArea(reclamation.getDescription());
//                        detailsController.setDatereclamation(String.valueOf(reclamation.getDateReclamation()));
//
//                        Stage detailsStage = new Stage();
//                        detailsStage.setScene(new Scene(detailsRoot));
//                        detailsStage.initStyle(StageStyle.UTILITY);
//                        detailsStage.show();
//
//                    } catch (IOException ex) {
//                        System.out.println(ex.getMessage());
//                    }
//                });

                Button b = (Button) item.lookup("#btn");
                b.setOnAction(event -> {
                    try {
                        System.out.println("aa");
                        if (propretyRequest != null) {
                            // Create a confirmation dialog
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText("Confirm Deletion");
                            alert.setContentText("Are you sure you want to delete this item?");

                            // Show the dialog and wait for the user's response
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    try {
                                        PropertyRequestService rs = new PropertyRequestService();
                                        rs.delete(propretyRequest.getId_property());
                                        System.out.println(propretyRequest.getId_property());
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

                pnItems.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    ArrayList<Node> nodes = new <Node>ArrayList();

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
        afficherPropertyRequest();

        for (int i = 0; i < nodes.size(); i++) {
            final int j = i;
            try {
                nodes.add( FXMLLoader.load(getClass().getResource("/PropertyRequest/PropertyRequestUser.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //give the items some effect

            nodes.get(i).setOnMouseEntered(event -> {
                nodes.get(j).setStyle("-fx-background-color : #0A0E3F");
            });
            nodes.get(i).setOnMouseExited(event -> {
                nodes.get(j).setStyle("-fx-background-color : #02030A");
            });
        }
    }



    @FXML
    ImageView profilePicImageView;

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

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlSettings;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;
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