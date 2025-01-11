package org.example.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.models.SinisterLife;
import org.example.models.User;
import org.example.services.SinisterLifeService;
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
import java.util.logging.Level;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;


public class SinisterLifeListForUser implements Initializable {
    @FXML
    private VBox pnItems = null;
    @FXML
    private PieChart statusPieChart;

    private SinisterLife selectedSinisterLife = null;
    @FXML
    ImageView profilePicImageView;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        displaySinisterLives();
        updateDashboard();
    }

    public void displaySinisterLives() {
        try {
            SinisterLifeService sl = new SinisterLifeService();
            User user = CurrentUser.getCurrentUser();

            List<SinisterLife> sinisterLives = sl.getByUserId(user.getId()); // Get sinister lives for the current user
            pnItems.getChildren().clear(); // Clear previous items

            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/Item2.fxml"));
            HBox header = headerLoader.load();
            setHeaderLabels(header);
            pnItems.getChildren().add(header);

            for (SinisterLife sinisterLife : sinisterLives) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Item2.fxml"));
                HBox item = loader.load();
                setSinisterLifeData(item, sinisterLife);
                item.getProperties().put("sinisterlife", sinisterLife);
                item.setOnMouseClicked(event -> handleSelection(item));
                pnItems.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setHeaderLabels(HBox header) {
        ((Label) header.lookup("#date_sinister")).setText("Date Sinister Life");
        ((Label) header.lookup("#description")).setText("Description");
        ((Label) header.lookup("#status_sinister")).setText("Status Sinister Life");
        ((Label) header.lookup("#amount_sinister")).setText("Amount Sinister");
        ((Label) header.lookup("#beneficiary_name")).setText("Beneficiary Name");
        ((Label) header.lookup("#location")).setText("Location");

        header.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 5px;");
    }


    private void setSinisterLifeData(HBox item, SinisterLife SinisterLife) {
        ((Label) item.lookup("#date_sinister")).setText(SinisterLife.getDateSinisterFormatted());
        ((Label) item.lookup("#description")).setText(SinisterLife.getDescription());
        ((Label) item.lookup("#status_sinister")).setText(SinisterLife.getStatusSinister());
        ((Label) item.lookup("#amount_sinister")).setText(SinisterLife.getAmountSinister().toString());
        ((Label) item.lookup("#beneficiary_name")).setText(String.valueOf(SinisterLife.getBeneficiaryName()));
        ((Label) item.lookup("#location")).setText(SinisterLife.getLocation());
    }

    private void handleSelection(HBox selectedItem) {
        pnItems.getChildren().forEach(item -> item.setStyle(null));
        selectedItem.setStyle("-fx-background-color: lightgray;");
        selectedSinisterLife = (SinisterLife) selectedItem.getProperties().get("sinisterlife");

    }

    private void updateDashboard() {
        User currentUser = CurrentUser.getCurrentUser();
        SinisterLifeService slService = new SinisterLifeService();
        try {
            int ongoing = slService.countByStatusAndUserId("ongoing", currentUser.getId());
            int processed = slService.countByStatusAndUserId("processed", currentUser.getId());
            int closed = slService.countByStatusAndUserId("refused", currentUser.getId());

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Ongoing", ongoing),
                    new PieChart.Data("Processed", processed),
                    new PieChart.Data("Refused", closed)
            );

            statusPieChart.setData(pieChartData);
            statusPieChart.setTitle("Status of Your Sinister Lives");
            applyChartColors();
        } catch (SQLException e) {
            System.out.println("failed to update dashboard: " + e.getMessage());
        }
    }

    private void applyChartColors() {
        for (PieChart.Data data : statusPieChart.getData()) {
            switch (data.getName()) {
                case "Ongoing":
                    data.getNode().setStyle("-fx-pie-color: blue;");
                    break;
                case "Processed":
                    data.getNode().setStyle("-fx-pie-color: green;");
                    break;
                case "Refused":
                    data.getNode().setStyle("-fx-pie-color: red;");
                    break;
            }


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