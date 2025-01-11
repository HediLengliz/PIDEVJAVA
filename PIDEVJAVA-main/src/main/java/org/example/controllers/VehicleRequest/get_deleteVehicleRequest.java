package org.example.controllers.VehicleRequest;

import org.example.controllers.ContratVehicule.addContratVehicule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.example.models.VehicleRequest;
import org.controlsfx.control.Notifications;
import org.example.services.VehicleRequestService;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class get_deleteVehicleRequest {


    @FXML
    private TableColumn<VehicleRequest, Integer> idVehicleRequest;

    @FXML
    private TableColumn<VehicleRequest, String> statusColum;

    @FXML
    private TableColumn<VehicleRequest, String> dateColumn;

    @FXML
    private TableColumn<VehicleRequest, String> marqueColumn;
    @FXML
    private TableColumn<VehicleRequest, String> modeleColumn;
    @FXML
    private TableColumn<VehicleRequest, String> matriculNumberColumn;
    @FXML
    private TableColumn<VehicleRequest, String> fabYearColumn;
    @FXML
    private TableColumn<VehicleRequest, String> serialNumberColumn;
    @FXML
    private TableColumn<VehicleRequest, String> vehiclePriceColumn;
    @FXML
    private TableView<VehicleRequest> VehicleRequestTable;


    private VehicleRequestService vehicleRequestService = new VehicleRequestService();

    @FXML
    private TextField keywordTextField;

    public void initialize() {
        try {
            List<VehicleRequest> vehicleRequests = vehicleRequestService.getAll();
            System.out.println(vehicleRequests);
            ObservableList<VehicleRequest> observableVehicleRequests = FXCollections.observableArrayList(vehicleRequests);
            VehicleRequestTable.setItems(observableVehicleRequests);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        loadDate();
    }







    private void showAlert(String title, String content) {
        // Implement your alert method here
    }
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableColumn<VehicleRequest, String> editCol;



    ObservableList<VehicleRequest> vehicleRequestObservableList = FXCollections.observableArrayList();

    @FXML
    private void refreshTable() {



        try {
            vehicleRequestObservableList.clear();
            VehicleRequest rs=new VehicleRequest();
            List<VehicleRequest> rl= vehicleRequestService.getAll();
            vehicleRequestObservableList.addAll(rl);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }



    }
    private void loadDate() {

        refreshTable();
        idVehicleRequest.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateRequest"));
        statusColum.setCellValueFactory(new PropertyValueFactory<>("Status"));
        marqueColumn.setCellValueFactory(new PropertyValueFactory<>("marque"));
        modeleColumn.setCellValueFactory(new PropertyValueFactory<>("modele"));
        matriculNumberColumn.setCellValueFactory(new PropertyValueFactory<>("matriculNumber"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        fabYearColumn.setCellValueFactory(new PropertyValueFactory<>("fabYear"));
        vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("vehiclePrice"));


        // Fetch and display user's VehicleRequests

        Callback<TableColumn<VehicleRequest, String>, TableCell<VehicleRequest, String>> cellFoctory = (TableColumn<VehicleRequest, String> param) -> {
            // make cell containing buttons
            final TableCell<VehicleRequest, String> cell = new TableCell<VehicleRequest, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);

                    } else {

                        FontAwesomeIconView showIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
                        FontAwesomeIconView refuseIcon = new FontAwesomeIconView(FontAwesomeIcon.TIMES);
                        showIcon.setFill(Color.web("#00E676")); // Vert
                        refuseIcon.setFill(Color.web("#ff1744")); // Rouge
                        showIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff1744;"
                        );
                        refuseIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );
                        VehicleRequest r = getTableView().getItems().get(getIndex());
                        if (Objects.equals(r.getStatus(), "Refuser") || Objects.equals(r.getStatus(), "Accepter")) {
                            showIcon.setDisable(true);
                            refuseIcon.setDisable(true);
                        }
                        showIcon.setOnMouseClicked((MouseEvent event) -> {
                            VehicleRequest rowData = getTableView().getItems().get(getIndex());
                            VehicleRequest selectedVehicleRequest = (VehicleRequest) rowData;
                            if (selectedVehicleRequest != null) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ContratVehicule/addContratVehicule.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL); // Ensure the popup is modal
                                stage.setTitle("Add contrat vehicule");
                                addContratVehicule controller = loader.getController();
                                controller.initData(selectedVehicleRequest);
                                stage.setScene(new Scene(root));
                                stage.showAndWait(); // Show the popup and wait for it to close
                                System.out.println("siiiiiiiiiiiiiiiii");
                            refreshTable();}
                        });

                        refuseIcon.setOnMouseClicked((MouseEvent event) -> {
                            VehicleRequest rowData = getTableView().getItems().get(getIndex());
                            VehicleRequest selectedVehicleRequest = (VehicleRequest) rowData;
                            VehicleRequestService vehicleRequestService1 = new VehicleRequestService();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText("Confirmer le refus");
                            alert.setContentText("Êtes-vous sûr de vouloir refuser cette demande ?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                try {
                                    vehicleRequestService1.updateStatus(selectedVehicleRequest.getId(), "Refuser");
                                    notifactionAlert(event);
                                    getTableView().getItems().clear();

                                    try {
                                        VehicleRequestService vehicleRequestService = new VehicleRequestService();
                                        List<VehicleRequest> vehicleRequests = vehicleRequestService.getAll();
                                        getTableView().getItems().addAll(vehicleRequests);
                                    } catch (SQLException e) {
                                        e.printStackTrace(); // Gérer l'exception correctement
                                    }


                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });



                        HBox managebtn = new HBox(showIcon, refuseIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(showIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(refuseIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                    }
                    setText(null);
                }

            };

            return cell;
        };

        editCol.setCellFactory(cellFoctory);
        VehicleRequestTable.setItems(vehicleRequestObservableList);

        statusColum.setCellFactory(column -> new TableCell<VehicleRequest, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    if (item.equals("Refuser")) {
                        setTextFill(Color.RED);
                        setStyle("-fx-font-weight: bold;");

                    } else if (item.equals("Accepter")) {
                        setTextFill(Color.GREEN);
                        setStyle("-fx-font-weight: bold;");

                    } else if (item.equals(("en_cours"))){
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });

        //add cell of button edit

    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    private void showDetails(VehicleRequest request) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'utilisateur");
        alert.setHeaderText("Détails de la demande sélectionnée");

        alert.setContentText("ID : " + request.getId() + "\n" +
                "Date : " + request.getDateRequest() + "\n"+
                "Status : " + request.getStatus() + "\n" +
                "Marque: " + request.getMarque() + "\n"+
                "Modele : " + request.getModele() + "\n" +
                "Anne de fabrication : " + request.getFabYear() + "\n" +
                "Matricule : " + request.getMatriculNumber() + "\n" +
                "Numero de serie : " + request.getSerialNumber() + "\n"+
                "Prix : " + request.getVehiclePrice() + "\n"


        );

        alert.showAndWait();
    }

    public void addEmployeeSearch() {
        keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchKey = newValue.trim().toLowerCase(); // Trim and convert to lowercase

            if (searchKey.isEmpty()) {

                VehicleRequestTable.setItems(vehicleRequestObservableList);
                refreshTable();
                return;
            }

            FilteredList<VehicleRequest> filteredList = new FilteredList<>(vehicleRequestObservableList, vehicleRequest ->
                    String.valueOf(vehicleRequest.getId()).contains(searchKey)
                            || vehicleRequest.getVehiclePrice().toLowerCase().contains(searchKey)
                            || vehicleRequest.getDateRequest().toString().toLowerCase().contains(searchKey)
                            || vehicleRequest.getFabYear().toString().toLowerCase().contains(searchKey)
                            || vehicleRequest.getStatus().toLowerCase().contains(searchKey)
                            || vehicleRequest.getMarque().toLowerCase().contains(searchKey)
                            || vehicleRequest.getSerialNumber().toLowerCase().contains(searchKey)
                            || vehicleRequest.getMatriculNumber().toString().contains(searchKey));

            SortedList<VehicleRequest> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(VehicleRequestTable.comparatorProperty());

            VehicleRequestTable.setItems(sortedList);
        });
    }

    private void notifactionAlert(MouseEvent event) {


        FontAwesomeIconView confirmationIcon = new FontAwesomeIconView();
        confirmationIcon.setGlyphName("CHECK_CIRCLE"); // Utilisation de l'icône de confirmation
        confirmationIcon.setSize("5em"); // Taille de l'icône
        confirmationIcon.setFill(Color.RED); // Changer la couleur de l'icône en vert

        Notifications notificationBuilder = Notifications.create()
                .title("Demande Refusée")
                .text("La demande est refusée avec succée")
                .graphic(confirmationIcon)
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);


        notificationBuilder.show();
    }

}