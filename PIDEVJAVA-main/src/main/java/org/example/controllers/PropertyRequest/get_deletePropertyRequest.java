package org.example.controllers.PropertyRequest;

import org.example.controllers.ContratHabitat.addContratHabitat;
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
import org.example.models.PropretyRequest;
import org.controlsfx.control.Notifications;
import org.example.services.PropertyRequestService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class get_deletePropertyRequest {


    @FXML
    private TableColumn<PropretyRequest, Integer> idLPropertyRequest;

    @FXML
    private TableColumn<PropretyRequest, String> statusColum;

    @FXML
    private TableColumn<PropretyRequest, String> dateColumn;

    @FXML
    private TableColumn<PropretyRequest, String> propertyFormColumn;
    @FXML
    private TableColumn<PropretyRequest, String> numberRoomsColumn;
    @FXML
    private TableColumn<PropretyRequest, String> addressColumn;
    @FXML
    private TableColumn<PropretyRequest, String> propertyValueColumn;
    @FXML
    private TableColumn<PropretyRequest, String> surfaceColumn;

    @FXML
    private TableView<PropretyRequest> propertyRequestTable;


    private PropertyRequestService propertyRequestService = new PropertyRequestService();

    @FXML
    private TextField keywordTextField;

    public void initialize() {
        try {
            List<PropretyRequest> propretyRequests = propertyRequestService.getAll();
            System.out.println(propretyRequests);
            ObservableList<PropretyRequest> observablePropertyRequests = FXCollections.observableArrayList(propretyRequests);
            propertyRequestTable.setItems(observablePropertyRequests);
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
    private TableColumn<PropretyRequest, String> editCol;



    ObservableList<PropretyRequest> propertyRequestObservableList = FXCollections.observableArrayList();

    @FXML
    private void refreshTable() {



        try {
            propertyRequestObservableList.clear();
            PropertyRequestService rs=new PropertyRequestService();
            List<PropretyRequest> rl= propertyRequestService.getAll();
            propertyRequestObservableList.addAll(rl);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }



    }
    private void loadDate() {

        refreshTable();
        idLPropertyRequest.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateRequest"));
        statusColum.setCellValueFactory(new PropertyValueFactory<>("Status"));
        propertyFormColumn.setCellValueFactory(new PropertyValueFactory<>("propertyForm"));
        numberRoomsColumn.setCellValueFactory(new PropertyValueFactory<>("numberRooms"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        propertyValueColumn.setCellValueFactory(new PropertyValueFactory<>("propertyValue"));
        surfaceColumn.setCellValueFactory(new PropertyValueFactory<>("surface"));



        Callback<TableColumn<PropretyRequest, String>, TableCell<PropretyRequest, String>> cellFoctory = (TableColumn<PropretyRequest, String> param) -> {
            final TableCell<PropretyRequest, String> cell = new TableCell<PropretyRequest, String>() {
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

                        refuseIcon.setStyle(
                                "-fx-cursor: hand;"
                                        + "-glyph-size: 28px;"
                                        + "-fx-fill: #ff1744;" // Rouge
                        );

                        showIcon.setStyle(
                                "-fx-cursor: hand;"
                                        + "-glyph-size: 28px;"
                                        + "-fx-fill: #00E676;" // Vert
                        );
                        PropretyRequest r = getTableView().getItems().get(getIndex());
                        if (Objects.equals(r.getStatus(), "Refuser") || Objects.equals(r.getStatus(), "Accepter")) {
                            showIcon.setDisable(true);
                            refuseIcon.setDisable(true);
                        }
                        showIcon.setOnMouseClicked((MouseEvent event) -> {
                            PropretyRequest rowData = getTableView().getItems().get(getIndex());

                            PropretyRequest selectedPropertyRequest = (PropretyRequest) rowData;
                            if (selectedPropertyRequest != null) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ContratHabitat/addContratHabitat.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL); // Ensure the popup is modal
                                stage.setTitle("Add contrat vehicule");
                                addContratHabitat controller = loader.getController();
                                controller.initData(selectedPropertyRequest);
                                stage.setScene(new Scene(root));
                                stage.showAndWait(); // Show the popup and wait for it to close
                            refreshTable();}
                        });
                        refuseIcon.setOnMouseClicked((MouseEvent event) -> {
                            PropretyRequest rowData = getTableView().getItems().get(getIndex());

                            PropretyRequest selectedPropertyRequest = (PropretyRequest) rowData;
                            PropertyRequestService propertyRequestService= new PropertyRequestService();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText("Confirmer le refus");
                            alert.setContentText("Êtes-vous sûr de vouloir refuser cette demande ?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                try {
                                    propertyRequestService.updateStatus(selectedPropertyRequest.getId(), "Refuser");
                                    notifactionAlert(event);
                                    getTableView().getItems().clear();

                                    try {
                                        List<PropretyRequest> propretyRequests = propertyRequestService.getAll();
                                        getTableView().getItems().addAll(propretyRequests);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });



                        HBox managebtn = new HBox( showIcon, refuseIcon);
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



        statusColum.setCellFactory(column -> new TableCell<PropretyRequest, String>() {
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
        editCol.setCellFactory(cellFoctory);
        propertyRequestTable.setItems(propertyRequestObservableList);

    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
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
    public void addEmployeeSearch() {
        keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchKey = newValue.trim().toLowerCase(); // Trim and convert to lowercase

            if (searchKey.isEmpty()) {

                propertyRequestTable.setItems(propertyRequestObservableList);
                refreshTable();
                return;
            }

            FilteredList<PropretyRequest> filteredList = new FilteredList<>(propertyRequestObservableList, propretyRequest ->
                    String.valueOf(propretyRequest.getId()).contains(searchKey)
                            || propretyRequest.getPropertyForm().toLowerCase().contains(searchKey)
                            || propretyRequest.getPropertyValue().toLowerCase().contains(searchKey)
                            || propretyRequest.getStatus().toLowerCase().contains(searchKey)
                            || propretyRequest.getSurface().toLowerCase().contains(searchKey)
                            || propretyRequest.getAddress().toLowerCase().contains(searchKey)
                            || propretyRequest.getNumberRooms().toString().contains(searchKey));

            SortedList<PropretyRequest> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(propertyRequestTable.comparatorProperty());

            propertyRequestTable.setItems(sortedList);
        });
    }




}