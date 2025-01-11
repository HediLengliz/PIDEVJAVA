package org.example.controllers.User;
import org.example.controllers.Reclamation.ShowReclamationDetals;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.models.Reclamation;
import org.example.models.User;
import org.controlsfx.control.GridCell;

import org.example.services.ReclamationService;
import org.example.services.UserService;
import org.example.utils.CurrentUser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.net.URL;

public class Profil implements Initializable {
    @FXML
    private VBox pnItems = null;

    @FXML
    private ScrollPane pnlReclamation;


//    public void afficherLifeRequest() {
//        try {
//
////            LifeRequestService lifeRequestService = new LifeRequestService();
////            List<LifeRequest> lifeRequests = lifeRequestService.getByUserId(8);
//
//            FXMLLoader l = new FXMLLoader(getClass().getResource("/LifeRequest/LifeRequestUser.fxml"));
//            HBox it = l.load();
//
//            Label idL = (Label) it.lookup("#id");
//            Label statusL = (Label) it.lookup("#status");
//            Label dateL = (Label) it.lookup("#dateRequest");
//            Label ageL = (Label) it.lookup("#age");
//            Label chronicDiseaseL = (Label) it.lookup("#chron_disease");
//            Label surgeryL = (Label) it.lookup("#surgery");
//            Label occupationL = (Label) it.lookup("#occupation");
//            Label chronicDiseaseDescriptionL = (Label) it.lookup("#chron_disease_description");
//            Label civilStatusL = (Label) it.lookup("#civil_status");
//
//            Button btn = (Button) it.lookup("#btn");
//            btn.setText("ACTIONS");
//            btn.setStyle(" -fx-border-color: transparent;-fx-fill: white; -fx-padding: 0;");
//            idL.setText("ID");
//            statusL.setText("Status");
//            dateL.setText("Date");
//            ageL.setText("Age");
//            chronicDiseaseL.setText("Maladie");
//            surgeryL.setText("Operation");
//            occupationL.setText("Profession");
//            chronicDiseaseDescriptionL.setText("Description");
//            civilStatusL.setText("DATE");
//            System.out.println(statusL.getText());
//
//            idL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-padding: 0 0 0 20;");
//            statusL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; ");
//            ageL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-padding: 0 5 0 0;");
//            statusL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            chronicDiseaseL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            surgeryL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            occupationL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            civilStatusL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            chronicDiseaseDescriptionL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//
//            // Style for DATE label
//            dateL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            btn.setStyle("-fx-background-color: transparent; " +
//                    "-fx-border-color: transparent; " +
//                    "-fx-padding: 0; " +
//                    "-fx-text-fill: #301bef; " +
//                    "-fx-font-size: 14px; " +
//                    "-fx-font-weight: bold;");
//            pnItems.getChildren().add(it);
//            System.out.println(lifeRequests.size());
//
//            for (int i = 0; i < lifeRequests.size(); i++) {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/LifeRequest/LifeRequestUser.fxml"));
//                HBox item = loader.load();
//
//                Label idLabel = (Label) item.lookup("#id");
//                Label statusLabel = (Label) item.lookup("#status");
//                Label dateLabel = (Label) item.lookup("#dateRequest");
//                Label ageLabel = (Label) item.lookup("#age");
//                Label chronicDiseaseLabel = (Label) item.lookup("#chron_disease");
//                Label surgeryLabel = (Label) item.lookup("#surgery");
//                Label occupationLabel = (Label) item.lookup("#occupation");
//                Label chronicDiseaseDescriptionLabel = (Label) item.lookup("#chron_disease_description");
//                Label civilStatusLabel = (Label) item.lookup("#civil_status");
//
//
//
//                LifeRequest lifeRequest = lifeRequests.get(i);
//
//
//                idLabel.setText(String.valueOf(lifeRequest.getId_life()));
//                statusLabel.setText(lifeRequest.getStatus());
//                dateLabel.setText(lifeRequest.getDateRequest().toString());
//                chronicDiseaseLabel.setText(lifeRequest.getChronicDisease());
//                ageLabel.setText(lifeRequest.getAge());
//                surgeryLabel.setText(lifeRequest.getSurgery());
//                occupationLabel.setText(lifeRequest.getOccupation());
//                chronicDiseaseDescriptionLabel.setText(lifeRequest.getChronicDiseaseDescription());
//                civilStatusLabel.setText(lifeRequest.getCivilStatus());
//
//                System.out.println(statusLabel.getText());
//                // Add mouse hover effects
//                //                item.setOnMouseEntered(event -> {
//                //                    item.setStyle("-fx-background-color : #c6c8ce");
//                //                });
//                //                item.setOnMouseExited(event -> {
//                //                    item.setStyle("-fx-background-color : #9a9bad");
//                //                });
//
//                // Add click event handler to show details
//
////                //item.setOnMouseClicked(event -> {
////                    try {
////                        FXMLLoader detailsLoader = new FXMLLoader(getClass().getResource("/reclamation/showReclamationDetals.fxml"));
////                        Parent detailsRoot = detailsLoader.load();
////                        ShowReclamationDetals detailsController = detailsLoader.getController();
////                        detailsController.setTitleField(reclamation.getTitle());
////                        detailsController.setDescriptionArea(reclamation.getDescription());
////                        detailsController.setDatereclamation(String.valueOf(reclamation.getDateReclamation()));
////
////                        Stage detailsStage = new Stage();
////                        detailsStage.setScene(new Scene(detailsRoot));
////                        detailsStage.initStyle(StageStyle.UTILITY);
////                        detailsStage.show();
////
////                    } catch (IOException ex) {
////                        System.out.println(ex.getMessage());
////                    }
////                });
//
//                Button b = (Button) item.lookup("#btn");
//                b.setOnAction(event -> {
//                    try {
//                        System.out.println("aa");
//                        if (lifeRequest != null) {
//                            // Create a confirmation dialog
//                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                            alert.setTitle("Confirmation");
//                            alert.setHeaderText("Confirm Deletion");
//                            alert.setContentText("Are you sure you want to delete this item?");
//
//                            // Show the dialog and wait for the user's response
//                            alert.showAndWait().ifPresent(response -> {
//                                if (response == ButtonType.OK) {
//                                    try {
//                                        LifeRequestService rs = new LifeRequestService();
//                                        rs.delete(lifeRequest.getId_life());
//                                        System.out.println(lifeRequest.getId_life());
//                                        refreshHBoxItems(pnItems);
//                                    } catch (SQLException ex) {
//                                        System.out.println(ex.getMessage());
//                                    }
//                                }
//                            });
//                        }
//                    } catch (Exception ex) {
//                        System.out.println(ex.getMessage());
//                    }
//                });
//
//                pnItems.getChildren().add(item);
//            }
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void refreshHBoxItems(VBox hBox) {
//        try {
//            LifeRequestService r = new LifeRequestService();
//            List<LifeRequest> lifeRequests = r.getByUserId(8);
//
//            hBox.getChildren().clear();
//            FXMLLoader l = new FXMLLoader(getClass().getResource("/LifeRequest/LifeRequestUser.fxml"));
//            HBox it = l.load();
//            Label idL = (Label) it.lookup("#id");
//            Label statusL = (Label) it.lookup("#status");
//            Label dateL = (Label) it.lookup("#dateRequest");
//            Label ageL = (Label) it.lookup("#age");
//            Label chronicDiseaseL = (Label) it.lookup("#chron_disease");
//            Label surgeryL = (Label) it.lookup("#surgery");
//            Label occupationL = (Label) it.lookup("#occupation");
//            Label chronicDiseaseDescriptionL = (Label) it.lookup("#chron_disease_description");
//            Label civilStatusL = (Label) it.lookup("#civil_status");
//
//
//            Button btn = (Button) it.lookup("#btn");
//            btn.setText("ACTIONS");
//            btn.setStyle(" -fx-border-color: transparent;-fx-fill: white; -fx-padding: 0;");
//            idL.setText("ID");
//            statusL.setText("TITLE");
//            dateL.setText("DATE");
//            ageL.setText("Age");
//            chronicDiseaseL.setText("Maladie chronique");
//            surgeryL.setText("Operation");
//            occupationL.setText("Profession");
//            chronicDiseaseDescriptionL.setText("Description");
//            civilStatusL.setText("DATE");
//
//
//            idL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//
//            // Style for TITLE label
//            statusL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            ageL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            statusL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            chronicDiseaseL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            surgeryL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            occupationL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            civilStatusL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//
//            // Style for DATE label
//            dateL.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF5733; -fx-font-weight: bold; -fx-font-family: 'Arial';");
//            btn.setStyle("-fx-background-color: transparent; " +
//                    "-fx-border-color: transparent; " +
//                    "-fx-padding: 0; " +
//                    "-fx-text-fill: #FF5733; " +
//                    "-fx-font-size: 14px; " +
//                    "-fx-font-weight: bold;");
//            pnItems.getChildren().add(it);
//
//            // Add the updated list of items to the HBox
//            for (LifeRequest lifeRequest : lifeRequests) {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/LifeRequest/LifeRequestUser.fxml"));
//                HBox item = loader.load();
//
//                Label idLabel = (Label) item.lookup("#id");
//                Label statusLabel = (Label) item.lookup("#status");
//                Label dateLabel = (Label) item.lookup("#dateRequest");
//                Label ageLabel = (Label) item.lookup("#age");
//                Label chronicDiseaseLabel = (Label) item.lookup("#chron_disease");
//                Label surgeryLabel = (Label) item.lookup("#surgery");
//                Label occupationLabel = (Label) item.lookup("#occupation");
//                Label chronicDiseaseDescriptionLabel = (Label) item.lookup("#chron_disease_description");
//                Label civilStatusLabel = (Label) item.lookup("#civil_status");
//
//
//
//                idLabel.setText(String.valueOf(lifeRequest.getId_life()));
//                statusLabel.setText(lifeRequest.getStatus());
//                dateLabel.setText(lifeRequest.getDateRequest().toString());
//                chronicDiseaseLabel.setText(lifeRequest.getChronicDisease());
//                ageLabel.setText(lifeRequest.getAge());
//                surgeryLabel.setText(lifeRequest.getSurgery());
//                occupationLabel.setText(lifeRequest.getOccupation());
//                chronicDiseaseDescriptionLabel.setText(lifeRequest.getChronicDiseaseDescription());
//                civilStatusLabel.setText(lifeRequest.getCivilStatus());
//
//                Button b = (Button) item.lookup("#btn");
//                b.setOnAction(event -> {
//                    try {
//                        System.out.println("aa");
//                        if (lifeRequest != null) {
//                            // Create a confirmation dialog
//                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                            alert.setTitle("Confirmation");
//                            alert.setHeaderText("Confirm Deletion");
//                            alert.setContentText("Are you sure you want to delete this item?");
//
//                            // Show the dialog and wait for the user's response
//                            alert.showAndWait().ifPresent(response -> {
//                                if (response == ButtonType.OK) {
//                                    try {
//                                        LifeRequestService rs = new LifeRequestService();
//                                        rs.delete(lifeRequest.getId_life());
//                                        System.out.println(lifeRequest.getId_life());
//                                        refreshHBoxItems(pnItems);
//                                    } catch (SQLException ex) {
//                                        System.out.println(ex.getMessage());
//                                    }
//                                }
//                            });
//                        }
//                    } catch (Exception ex) {
//                        System.out.println(ex.getMessage());
//                    }
//                });
//
//                // Add click event handler to show details
////                item.setOnMouseClicked(event -> {
////                    try {
////                        FXMLLoader detailsLoader = new FXMLLoader(getClass().getResource("/reclamation/showReclamationDetals.fxml"));
////                        Parent detailsRoot = detailsLoader.load();
////                        ShowReclamationDetals detailsController = detailsLoader.getController();
////                        detailsController.setTitleField(reclamation.getTitle());
////                        detailsController.setDescriptionArea(reclamation.getDescription());
////                        detailsController.setDatereclamation(String.valueOf(reclamation.getDateReclamation()));
////
////                        Stage detailsStage = new Stage();
////                        detailsStage.setScene(new Scene(detailsRoot));
////                        detailsStage.initStyle(StageStyle.UTILITY);
////                        detailsStage.show();
////
////                    } catch (IOException ex) {
////                        System.out.println(ex.getMessage());
////                    }
////                });
//
//                hBox.getChildren().add(item);
//            }
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//    }
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

@FXML
    private void goHome() {
        try {
            root =FXMLLoader.load(getClass().getResource("/home.fxml"));
            Stage stage = (Stage) pnItems.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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


    @FXML
    private TextField emailTF;
    @FXML
    private PasswordField currentPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label username;


    @FXML
    private TextField firstNameTF;
    @FXML
    private TextField roleTF;

    @FXML
    private Label firstNameErrorLabel;

    @FXML
    private TextField lastNameTF;

    @FXML
    private Label lastNameErrorLabel;

    @FXML
    private TextField cinTF;

    @FXML
    private Label cinErrorLabel;

    @FXML
    private TextArea addressTF;

    @FXML
    private Label addressErrorLabel;

    @FXML
    private TextField phoneNumberTF;

    @FXML
    private Label phoneNumberErrorLabel;

    @FXML
    private Label sqlerror;
@FXML
Button addrecbtn;


    public void initData(User user) throws SQLException {
        selectedUser = user;
        if (selectedUser == null) {
            selectedUser=userService.getById(113);

        }
        username.setText(user.getFirstName()+" "+user.getLastName());
        if (emailTF != null) {
            populateFields();

        }
    }
    private void populateFields() {
        emailTF.setText(selectedUser.getEmail());
        List<String> rolesList = selectedUser.getRoles();
        String rolesString = rolesList.stream()
                .map(role -> role.replaceAll("[\\[\\]\"]", ""))
                .collect(Collectors.joining("-"));        System.out.println(rolesString.join("-")+"zz");
        roleTF.setText(Arrays.asList(rolesString).stream().collect(Collectors.joining("-")));
        firstNameTF.setText(selectedUser.getFirstName());
        lastNameTF.setText(selectedUser.getLastName());
        cinTF.setText(selectedUser.getCin());
        addressTF.setText(selectedUser.getAddress());
        phoneNumberTF.setText(selectedUser.getPhoneNumber());
        String imageUUID = selectedUser.getImage(); // Assuming this returns the UUID
        if (imageUUID != null && !imageUUID.isEmpty()) {
            String imagePath = "src/main/resources/images/profiles/  " + imageUUID ; // Adjust file extension if necessary
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

    }
    @FXML
    private void updatePassword() throws IOException {
        String currentPasswordText = currentPassword.getText();
        String newPasswordText = newPassword.getText();
        String confirmPasswordText = confirmPassword.getText();

        // Perform validation checks
        if (currentPasswordText.isEmpty() || newPasswordText.isEmpty() || confirmPasswordText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error","","All fields are required.");
            return;
        }

        if (!newPasswordText.equals(confirmPasswordText)) {
            showAlert(Alert.AlertType.ERROR, "Error", "","New password and confirm password do not match.");
            return;
        }

        try {
            userService.updatePassword(currentPasswordText, newPasswordText);
            showAlert(Alert.AlertType.INFORMATION, "Success","", "Password updated successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error","","Failed to update password: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error","","Failed to update password: " + e.getMessage());
        }

        }

    @FXML
    void updateUser(ActionEvent event) throws IOException, SQLException {
        String email = emailTF.getText();
        String firstName = firstNameTF.getText();
        String lastName = lastNameTF.getText();
        String cin = cinTF.getText();
        String address = addressTF.getText();
        String phoneNumber = phoneNumberTF.getText();
        if (email.isEmpty()) {
            emailErrorLabel.setText("Email is required.");
        } else {
            emailErrorLabel.setText(""); // Clear error if valid
        }

        if (firstName.isEmpty()) {
            firstNameErrorLabel.setText("First Name is required.");
        } else {
            firstNameErrorLabel.setText(""); // Clear error if valid
        }

        if (lastName.isEmpty()) {
            lastNameErrorLabel.setText("Last Name is required.");
        } else {
            lastNameErrorLabel.setText(""); // Clear error if valid
        }

        if (cin.isEmpty()) {
            cinErrorLabel.setText("CIN is required.");
        } else {
            cinErrorLabel.setText(""); // Clear error if valid
        }

        if (address.isEmpty()) {
            addressErrorLabel.setText("Address is required.");
        } else {
            addressErrorLabel.setText(""); // Clear error if valid
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberErrorLabel.setText("Phone Number is required.");
        } else {
            phoneNumberErrorLabel.setText(""); // Clear error if valid
        }


        if (emailErrorLabel.getText().isEmpty() && firstNameErrorLabel.getText().isEmpty() &&
                lastNameErrorLabel.getText().isEmpty() && cinErrorLabel.getText().isEmpty() && addressErrorLabel.getText().isEmpty() && phoneNumberErrorLabel.getText().isEmpty()  ) {
            selectedUser.setEmail(email);

            selectedUser.setFirstName(firstName);
            selectedUser.setLastName(lastName);
            selectedUser.setCin(cin);
            selectedUser.setAddress(address);
            selectedUser.setPhoneNumber(phoneNumber);

            try {
                username.setText(selectedUser.getFirstName()+" "+selectedUser.getLastName());

                userService.update(selectedUser);
                showAlert(Alert.AlertType.INFORMATION, "Success", "User Updated", "User updated successfully.");
            } catch (SQLException e) {
                sqlerror.setText(e.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) throws IOException {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();


    }

    public void saveProfilePicture() {
        if (profilePicImageView != null && profilePicImageView.getImage() != null) {
            try {
                // Get the image file path
                String imagePath = profilePicImageView.getImage().getUrl();
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
                File destinationDir = new File("src/main/resources/images/profiles/");
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
                PreparedStatement preparedStatement = userService.getConnection().prepareStatement(updateQuery);
                preparedStatement.setString(1, fileName);
                preparedStatement.setInt(2, CurrentUser.getCurrentUser().getId()); // Set the user ID or use a parameter
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
    public class CustomGridCell extends GridCell<String> {

        private final GridPane pane;
        private final Label label;

        public CustomGridCell() {
            this.pane = new GridPane();
            this.label = new Label();

            pane.add(label, 0, 0);
            setGraphic(pane);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                label.setText(item);
                setGraphic(pane);
            }
        }
    }
    @FXML
    private GridPane gridpane;
    @FXML
    Button btnListUserReclamations;
    private void populateGridPane(List<Reclamation> reclamations) {
        int row = 0;

        Label idHeader = new Label("ID");
        Label titleHeader = new Label("TITLE");
        Label dateHeader = new Label("DATE");
        Label actionsHeader = new Label("ACTIONS");
        idHeader.setAlignment(Pos.CENTER);
    titleHeader.setAlignment(Pos.CENTER);
    dateHeader.setAlignment(Pos.CENTER);
    actionsHeader.setAlignment(Pos.CENTER);
        idHeader.getStyleClass().add("table-header");
        titleHeader.getStyleClass().add("table-header");
        dateHeader.getStyleClass().add("table-header");
        actionsHeader.getStyleClass().add("table-header");

        idHeader.setAlignment(Pos.CENTER);
        titleHeader.setAlignment(Pos.CENTER);
        dateHeader.setAlignment(Pos.CENTER);
        actionsHeader.setAlignment(Pos.CENTER_RIGHT);

        // Add column headers to the grid
        gridpane.add(idHeader, 0, row);
        gridpane.add(titleHeader, 1, row);
        gridpane.add(dateHeader, 2, row);
        gridpane.add(actionsHeader, 3, row);

        row++; // Move to the next row for data

        for (Reclamation reclamation : reclamations) {
            // Create a BorderPane for each row to hold the content
         Pane bp1 = new Pane();
            BorderPane bp2 = new BorderPane();
            BorderPane bp3 = new BorderPane();

            Label idLabel = new Label(reclamation.getId().toString());
            Label titleLabel = new Label(reclamation.getTitle());
            Label dateLabel = new Label(reclamation.getDateReclamation().toString());

            idLabel.getStyleClass().add("table-cell");
            titleLabel.getStyleClass().add("table-cell");
            dateLabel.getStyleClass().add("table-cell");

            // Set content alignment in BorderPanes
            bp1.getChildren().addAll(titleLabel,idLabel,dateLabel);
            bp2.setCenter(titleLabel);
            bp3.setCenter(dateLabel);

            // Add the BorderPanes to the GridPane at the specified row
            gridpane.add(bp1, 0, row);
//            gridpane.add(bp2, 1, row);
//            gridpane.add(bp3, 2, row);

            FontAwesomeIconView showIcon = new FontAwesomeIconView(FontAwesomeIcon.EYE);
            showIcon.setFill(Color.BLUE); // Set icon color
            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
            deleteIcon.setFill(Color.RED); // Set icon color
            showIcon.setSize("2em"); // Set icon size (larger than default)
            deleteIcon.setSize("2em"); // Set icon size (larger than default)

            HBox iconsBox = new HBox(showIcon, deleteIcon);
              iconsBox.setAlignment(Pos.CENTER);
            gridpane.add(iconsBox, 3, row); // Add icons box to the specified column

            row++;
        }
    }
    public void initialize(URL location, ResourceBundle resources) {
        try {
            if (CurrentUser.getCurrentUser() == null) {
                CurrentUser.setCurrentUser(userService.getById(113));

            }
            String imageUUID = CurrentUser.getCurrentUser().getImage(); // Assuming this returns the UUID

            if (imageUUID != null && !imageUUID.isEmpty()) {
                String imagePath = "src/main/resources/images/profiles/" + imageUUID ; // Adjust file extension if necessary
                try
                {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        Image image = new Image(imageFile.toURI().toURL().toString());
                        profilePicImageView.setImage(image);
                    }
                    else
                    {

                        System.out.println("Profile picture file not found at: " + imagePath);
                    }
                } catch (MalformedURLException e) {
                    // Handle malformed URL exception
                    e.printStackTrace();
                }
            } else
            {
                System.out.println("Profile picture UUID is null or empty.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < nodes.size(); i++) {
            final int j = i;
            try {
                nodes.add( FXMLLoader.load(getClass().getResource("/LifeRequest/LifeRequestUser.fxml")));
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
                displayUserReclamations();
                fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Profile Picture");
                fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

                profilePicImageView.setOnMouseClicked(event -> chooseProfilePicture());
        User u=CurrentUser.getCurrentUser();
        if (u == null){
            try
            {
                u=userService.getById(113);
            }
            catch (SQLException e)
            {

                throw new RuntimeException(e);

            }
        }
        try {
            initData(u);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < nodes.size(); i++) {
            final int j = i;
            try {
                nodes.add(FXMLLoader.load(getClass().getResource("/reclamation/Item.fxml")));
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
    private ImageView profilePicImageView;
    private void chooseProfilePicture() {
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            System.out.println(selectedFile.toURI().toString());
            Image image = new Image(selectedFile.toURI().toString());
            profilePicImageView.setImage(image);
            saveProfilePicture();

        } else
        {

        }
    }

    public void switchToSceneOne() throws IOException {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser != null) {
            // Load the FXML file for the popup window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/AddReclamation.fxml"));
            Parent popupRoot = loader.load();

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
            popupStage.initOwner(pnItems.getScene().getWindow()); // Set the owner window
            popupStage.setTitle("Add Reclamation"); // Set the title of the popup window

            // Set the scene with the loaded FXML for the popup
            Scene popupScene = new Scene(popupRoot);
            popupStage.setScene(popupScene);

            popupStage.showAndWait();

            // Perform refresh after the popup is closed
            refreshHBoxItems(pnItems);

        }
    }

    @FXML
    Text pr;
    public void handleClicks(ActionEvent actionEvent) {
        System.out.println(actionEvent.getSource());
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setVisible(true);
            pnlSettings.setVisible(false);
            pnlReclamation.setVisible(false);
            pr.setVisible(false);
            addrecbtn.setVisible(false);

        }

        if(actionEvent.getSource()==btnSettings)
        {
            pnlCustomer.setVisible(false);
        pnlSettings.setVisible(true);
        pnlReclamation.setVisible(false);
            pr.setVisible(false);
            addrecbtn.setVisible(false);


        }
    if(actionEvent.getSource()==btnMenus){
            pnlCustomer.setVisible(false);
            pnlSettings.setVisible(false);
             pnlReclamation.setVisible(true);
        pr.setVisible(true);
        addrecbtn.setVisible(true);


    }
}
    public void displayUserReclamations() {
        try {
            ReclamationService r = new ReclamationService();
            List<Reclamation> reclamations = r.getReclamationsByUser();

            FXMLLoader l = new FXMLLoader(getClass().getResource("/reclamation/Item.fxml"));
            HBox it = l.load();
            it.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 5px;");
            Label idL = (Label) it.lookup("#id");
            Label titleL = (Label) it.lookup("#titre");
            Label dateL = (Label) it.lookup("#date");
            Button btn = (Button) it.lookup("#btn");
            btn.setText("ACTIONS");
            btn.setStyle(" -fx-border-color: transparent;-fx-fill: #e0dcdc; -fx-padding: 0;");
            idL.setText("ID");
            titleL.setText("TITLE");
            dateL.setText("DATE");
            idL.setStyle("-fx-font-size: 16px; -fx-text-fill: #181818; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            titleL.setStyle("-fx-font-size: 16px; -fx-text-fill: #181818; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            dateL.setStyle("-fx-font-size: 16px; -fx-text-fill: #181818; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            btn.setStyle(
                        "-fx-background-color: transparent; " +
                        "-fx-border-color: transparent; " +
                        "-fx-padding: 0; " +
                        "-fx-text-fill: #181818; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;")
            ;
                        pnItems.getChildren().add(it);

                for (int i = 0; i < reclamations.size(); i++) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/Item.fxml"));
                    HBox item = loader.load();
                    Label idLabel = (Label) item.lookup("#id");
                    Label titleLabel = (Label) item.lookup("#titre");
                    Label dateLabel = (Label) item.lookup("#date");
                    Reclamation reclamation = reclamations.get(i);
                    idLabel.setText(String.valueOf(reclamation.getId()));
                    titleLabel.setText(reclamation.getTitle());
                    dateLabel.setText(reclamation.getDateReclamation().toString());
                    item.setOnMouseClicked(event -> {
                    try {
                            FXMLLoader detailsLoader = new FXMLLoader(getClass().getResource("/reclamation/showReclamationDetals.fxml"));
                            Parent detailsRoot = detailsLoader.load();
                            ShowReclamationDetals detailsController = detailsLoader.getController();
                            detailsController.setTitleField(reclamation.getTitle());
                            detailsController.setDescriptionArea(reclamation.getDescription());
                            detailsController.setDatereclamation(String.valueOf(reclamation.getDateReclamation()));
                            Stage detailsStage = new Stage();
                            detailsStage.setScene(new Scene(detailsRoot));
                            detailsStage.initStyle(StageStyle.UTILITY);
                            detailsStage.show();
                    }
                    catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                });
                Button b = (Button) item.lookup("#btn");
                b.setOnAction(event -> {
                    try {
                        System.out.println("aa");
                        if (reclamation != null) {
                            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Confirmation", JOptionPane.YES_NO_OPTION);
                            if (choice == JOptionPane.YES_OPTION) {
                                try {
                                    ReclamationService rs = new ReclamationService();
                                    rs.delete(reclamation.getId());
                                    refreshHBoxItems(pnItems);
                                }

                                catch (SQLException ex)
                                {
                                    System.out.println(ex.getMessage());
                                }

                            }
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
            ReclamationService r = new ReclamationService();
            List<Reclamation> reclamations = r.getReclamationsByUser();
            // Clear the existing items in the HBox
            hBox.getChildren().clear();
            FXMLLoader l = new FXMLLoader(getClass().getResource("/reclamation/Item.fxml"));
            HBox it = l.load();
            it.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 5px;");
            Label idL = (Label) it.lookup("#id");
            Label titleL = (Label) it.lookup("#titre");
            Label dateL = (Label) it.lookup("#date");
            Button btn = (Button) it.lookup("#btn");
            btn.setText("ACTIONS");
            btn.setStyle(" -fx-border-color: transparent;-fx-fill: #e0dcdc; -fx-padding: 0;");
            idL.setText("ID");
            titleL.setText("TITLE");
            dateL.setText("DATE");
            idL.setStyle("-fx-font-size: 16px; -fx-text-fill: #181818; -fx-font-weight: bold; -fx-font-family: 'Arial';");

            // Style for TITLE label
            titleL.setStyle("-fx-font-size: 16px; -fx-text-fill: #181818; -fx-font-weight: bold; -fx-font-family: 'Arial';");

            // Style for DATE label
            dateL.setStyle("-fx-font-size: 16px; -fx-text-fill: #181818; -fx-font-weight: bold; -fx-font-family: 'Arial';");
            btn.setStyle("-fx-background-color: transparent; " +
                    "-fx-border-color: transparent; " +
                    "-fx-padding: 0; " +
                    "-fx-text-fill: #181818; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: bold;");
            pnItems.getChildren().add(it);

            // Add the updated list of items to the HBox
            for (Reclamation reclamation : reclamations) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/Item.fxml"));
                HBox item = loader.load();

                Label idLabel = (Label) item.lookup("#id");
                Label titleLabel = (Label) item.lookup("#titre");
                Label dateLabel = (Label) item.lookup("#date");

                idLabel.setText(String.valueOf(reclamation.getId()));
                titleLabel.setText(reclamation.getTitle());
                dateLabel.setText(reclamation.getDateReclamation().toString());

                // Add click event handler to show details
                item.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader detailsLoader = new FXMLLoader(getClass().getResource("/reclamation/showReclamationDetals.fxml"));
                        Parent detailsRoot = detailsLoader.load();
                        ShowReclamationDetals detailsController = detailsLoader.getController();
                        detailsController.setTitleField(reclamation.getTitle());
                        detailsController.setDescriptionArea(reclamation.getDescription());
                        detailsController.setDatereclamation(String.valueOf(reclamation.getDateReclamation()));

                        Stage detailsStage = new Stage();
                        detailsStage.setScene(new Scene(detailsRoot));
                        detailsStage.initStyle(StageStyle.UTILITY);
                        detailsStage.show();

                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                });
                Button b = (Button) item.lookup("#btn");
                b.setOnAction(event -> {
                    try {
                        System.out.println("aa");
                        if (reclamation != null) {
                            // Create a confirmation dialog
                            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Confirmation", JOptionPane.YES_NO_OPTION);

                            // Check user's choice
                            if (choice == JOptionPane.YES_OPTION) {
                                try {
                                    ReclamationService rs = new ReclamationService();
                                    rs.delete(reclamation.getId());
                                    refreshHBoxItems(pnItems);
                                } catch (SQLException ex) {
                                    System.out.println(ex.getMessage());
                                }
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                });

                hBox.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}
