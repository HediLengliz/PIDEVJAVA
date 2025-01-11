package org.example.controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.models.ContratHabitat;
import org.example.models.ContratVehicule;
import org.example.models.ContratVie;
import org.example.models.User;
import org.example.services.ContratHabitatService;
import org.example.services.ContratVehiculeService;
import org.example.services.ContratVieService;
import org.example.services.UserService;
import org.example.utils.CurrentUser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ContratInUser {

    @FXML
    private TableView<ContratVie> contratVieTable;
    @FXML
    private TableColumn<ContratVie, Integer> idContrat;
    @FXML
    private TableColumn<ContratVie, String> dateDebutColumn;
    @FXML
    private TableColumn<ContratVie, String> dateFinColumn;
    @FXML
    private TableColumn<ContratVie, String> descriptionColumn;
    @FXML
    private TableColumn<ContratVie, String> matriculeAgentColumn;
    @FXML
    private TableColumn<ContratVie, String> prixColumn;
    @FXML
    private TableColumn<ContratHabitat, String> editCol;
    @FXML
    private TableColumn<ContratVie, String> editColVie;
    @FXML
    private TableColumn<ContratVehicule, String> editColV;

    @FXML
    private TableView<ContratHabitat> contratHabitatTable;


    @FXML
    private TableColumn<ContratVie, Integer> idContratH;
    @FXML
    private TableColumn<ContratVie, String> dateDebutColumnH;
    @FXML
    private TableColumn<ContratVie, String> dateFinColumnH;
    @FXML
    private TableColumn<ContratVie, String> descriptionColumnH;
    @FXML
    private TableColumn<ContratVie, String> matriculeAgentColumnH;
    @FXML
    private TableColumn<ContratVie, String> prixColumnH;

    private void openPopupFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Payement/payement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Popup Title");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private TableColumn<ContratHabitat, Integer> idContratHabitat;
    // Ajoutez d'autres colonnes pour ContratHabitat ici

    @FXML
    private TableView<ContratVehicule> contratVehiculeTable;

    @FXML
    private TableColumn<ContratVie, Integer> idContratV;
    @FXML
    private TableColumn<ContratVie, String> dateDebutColumnV;
    @FXML
    private TableColumn<ContratVie, String> dateFinColumnV;
    @FXML
    private TableColumn<ContratVie, String> descriptionColumnV;
    @FXML
    private TableColumn<ContratVie, String> matriculeAgentColumnV;
    @FXML
    private TableColumn<ContratVie, String> prixColumnV;
    @FXML
    private TableColumn<ContratVehicule, Integer> idContratVehicule;
    // Ajoutez d'autres colonnes pour ContratVehicule ici

    private ContratVieService contratVieService = new ContratVieService();
    private ContratVehiculeService contratVehiculeService = new ContratVehiculeService();
    private ContratHabitatService contratHabitatService = new ContratHabitatService();


    public void initialize() {
        loadContratVieTable();
        loadContratHabitatTable();
        loadContratVehiculeTable();
    }

    private void loadContratVieTable() {
        try {
            System.out.println("insurance  "+contratVieService.getAllInsuranceIdsByUserIdAndType(CurrentUser.getCurrentUser().getId(), "vie").size());
            List<ContratVie> contratVies = contratVieService.getAllContratsVieByInsuranceIds(contratVieService.getAllInsuranceIdsByUserIdAndType(9, "vie"));
            ObservableList<ContratVie> observableContratVies = FXCollections.observableArrayList(contratVies);
            contratVieTable.setItems(observableContratVies);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        idContrat.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        matriculeAgentColumn.setCellValueFactory(new PropertyValueFactory<>("matriculeAgent"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        Callback<TableColumn<ContratVie, String>, TableCell<ContratVie, String>> cellFactory = (TableColumn<ContratVie, String> param) -> {
            final TableCell<ContratVie, String> cell = new TableCell<ContratVie, String>() {
                final Button btn = new Button("Action");

                {
                    btn.setOnAction((ActionEvent event) -> {
                        ContratVie contratVie = getTableView().getItems().get(getIndex());
                        openPopupFXML();
                    });
                }

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        };

        // Apply the custom cell to the action column
        editColVie.setCellFactory(cellFactory); // Replace `editCol` with the actual name of your action column
    }


    private void loadContratHabitatTable() {
        try {
            List<ContratHabitat> contratHabitats = contratHabitatService.getAllContratsVieByInsuranceIds(
                    contratHabitatService.getAllInsuranceIdsByUserIdAndType(CurrentUser.getCurrentUser().getId(), "habitat"));
            ObservableList<ContratHabitat> observableContratHabitats = FXCollections.observableArrayList(contratHabitats);
            contratHabitatTable.setItems(observableContratHabitats);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        idContratH.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateDebutColumnH.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumnH.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        descriptionColumnH.setCellValueFactory(new PropertyValueFactory<>("description"));
        matriculeAgentColumnH.setCellValueFactory(new PropertyValueFactory<>("matriculeAgent"));
        prixColumnH.setCellValueFactory(new PropertyValueFactory<>("prix"));

        Callback<TableColumn<ContratHabitat, String>, TableCell<ContratHabitat, String>> cellFactory = (TableColumn<ContratHabitat, String> param) -> {
            final TableCell<ContratHabitat, String> cell = new TableCell<ContratHabitat, String>() {
                final Button btn = new Button("Action");

                {
                    btn.setOnAction((ActionEvent event) -> {
                        ContratHabitat contratHabitat = getTableView().getItems().get(getIndex());
                        openPopupFXML();
                        // Code à exécuter lors du clic sur le bouton
                    });
                }

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        };

        // Appliquer la cellule personnalisée à la colonne d'action
        editCol.setCellFactory(cellFactory);
    }


    private void loadContratVehiculeTable() {
        try {
            List<ContratVehicule> contratVehicules = contratVehiculeService.getAllContratsVieByInsuranceIds(
                    contratVehiculeService.getAllInsuranceIdsByUserIdAndType(CurrentUser.getCurrentUser().getId(), "vie"));
            ObservableList<ContratVehicule> observableContratVehicules = FXCollections.observableArrayList(contratVehicules);
            contratVehiculeTable.setItems(observableContratVehicules);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        idContratV.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateDebutColumnV.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumnV.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        descriptionColumnV.setCellValueFactory(new PropertyValueFactory<>("description"));
        matriculeAgentColumnV.setCellValueFactory(new PropertyValueFactory<>("matriculeAgent"));
        prixColumnV.setCellValueFactory(new PropertyValueFactory<>("prix"));

        Callback<TableColumn<ContratVehicule, String>, TableCell<ContratVehicule, String>> cellFactory = (TableColumn<ContratVehicule, String> param) -> {
            final TableCell<ContratVehicule, String> cell = new TableCell<ContratVehicule, String>() {
                final Button btn = new Button("Payer");

                {
                    btn.setOnAction((ActionEvent event) -> {
                        ContratVehicule contratVehicule = getTableView().getItems().get(getIndex());
                        openPopupFXML();
                        // Code à exécuter lors du clic sur le bouton
                    });
                }

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        };

        // Appliquer la cellule personnalisée à la colonne d'action
        editColV.setCellFactory(cellFactory);
    }


    @FXML
    private void refreshTable() {
        loadContratVieTable();
        loadContratHabitatTable();
        loadContratVehiculeTable();
    }

    public TextField cardNumberField;
    public TextField expiryDateField;
    public TextField cvcField;

    public void processPayment() {
        String cardNumber = cardNumberField.getText();
        String expiryDate = expiryDateField.getText();
        String cvc = cvcField.getText();

        if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvc.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        // Validating card details
        if (!isValidCardDetails(cardNumber, expiryDate, cvc)) {
            showAlert("Error", "Invalid card details.");
            return;
        }

        Stripe.apiKey = "sk_test_51OnfjpIUcD4J9UQYS32pfvQXJMAtOffv9lzsZaxmCk4GHyezuQ8PTS0bVlmtVeMor0Xw7uNN6BA8i34LEVtczCSt00YL6gLVDj";

        try {
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", 2000); // Montant en cents
            chargeParams.put("currency", "usd");
            chargeParams.put("source", "tok_visa"); // Utilisez un numéro de carte de test fourni par Stripe
            chargeParams.put("description", "Test Charge");

            Charge.create(chargeParams);

            showAlertSuccess("Success", "Payment successful!");
        } catch (StripeException e) {
            e.printStackTrace();
            showAlert("Error", "Payment failed. Please try again later.");
        }
    }

    private boolean isValidCardDetails(String cardNumber, String expiryDate, String cvc) {
        // Remove all non-digit characters from card number
        String sanitizedCardNumber = cardNumber.replaceAll("[^\\d]", "");

        // Check if card number is of valid length (between 13 and 19 digits)
        if (sanitizedCardNumber.length() < 13 || sanitizedCardNumber.length() > 19) {
            return false;
        }

        // Perform Luhn algorithm validation for card number
        if (!isValidLuhn(sanitizedCardNumber)) {
            return false;
        }

        // Check if expiry date is in the format MM/YY
        if (!expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            return false;
        }

        // Check if CVC is 3 or 4 digits
        if (!cvc.matches("\\d{3,4}")) {
            return false;
        }

        return true;
    }

    // Luhn algorithm implementation
    private boolean isValidLuhn(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAlertSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        openWindow("/reclamation/userReclamations.fxml", "User Reclamations");
    }
    @FXML
    void listSinistreHabitat(ActionEvent event)
    {
        openWindow("/sinisterProperty/showTable.fxml", "User Reclamations");
    }
    @FXML
    void listSinistreVehicule(ActionEvent event)
    {
        openWindow("/sinisterVehicle/showTableV.fxml", "User Reclamations");
    }
    @FXML
    void listSinistreMaladie(ActionEvent event)
    {
        openWindow("/test.fxml", "User Reclamations");
    }

    @FXML
    void listDevis(ActionEvent event)
    {
        openWindow("/test2.fxml", "User Reclamations");
    }
    @FXML
    void contrat(ActionEvent event)
    {
        openWindow("/ContratHabitat/ContratInUser.fxml", "User Reclamations");
    }

    @FXML
    private VBox pnItems = null;

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
