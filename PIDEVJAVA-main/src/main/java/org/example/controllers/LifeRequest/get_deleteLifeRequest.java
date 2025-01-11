package org.example.controllers.LifeRequest;

import org.example.controllers.ContratVie.addContratVie;
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
import org.example.models.LifeRequest;
import org.controlsfx.control.Notifications;
import org.example.models.Question;
import org.example.services.LifeRequestService;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class get_deleteLifeRequest {


    @FXML
    private TableColumn<LifeRequest, Integer> idLifeRequest;

    @FXML
    private TableColumn<LifeRequest, String> statusColum;

    @FXML
    private TableColumn<LifeRequest, String> dateColumn;

    @FXML
    private TableColumn<LifeRequest, String> ageColumn;
    @FXML
    private TableColumn<LifeRequest, String> chronicDiseaseColumn;
    @FXML
    private TableColumn<LifeRequest, String> surgeryColumn;
    @FXML
    private TableColumn<LifeRequest, String> civilStatusColumn;
    @FXML
    private TableColumn<LifeRequest, String> occupationColumn;
    @FXML
    private TableColumn<LifeRequest, String> chronicDiseaseDescriptionColumn;
    @FXML
    private TableView<LifeRequest> LifeRequestTable;
    private List<LifeRequest> LifeRequests;
    @FXML
    private ComboBox<String> sortComboBox;
    private ObservableList<Question> masterData = FXCollections.observableArrayList();

    private LifeRequestService LifeRequestService = new LifeRequestService();



    public void initialize() {
        try {
            LifeRequests = LifeRequestService.getAll();
            System.out.println(LifeRequests);
            ObservableList<LifeRequest> observableLifeRequests = FXCollections.observableArrayList(LifeRequests);
            LifeRequestTable.setItems(observableLifeRequests);
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
    private TableColumn<LifeRequest, String> editCol;

    @FXML
    private TextField keywordTextField;

    ObservableList<LifeRequest> LifeRequestObservableList = FXCollections.observableArrayList();

    @FXML
    public void refreshTable() {



        try {
            LifeRequestObservableList.clear();
            LifeRequest rs=new LifeRequest();
            List<LifeRequest> rl= LifeRequestService.getAll();
            LifeRequestObservableList.addAll(rl);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }



    }
    private void loadDate() {

        refreshTable();
        idLifeRequest.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateRequest"));
        statusColum.setCellValueFactory(new PropertyValueFactory<>("Status"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        chronicDiseaseColumn.setCellValueFactory(new PropertyValueFactory<>("chronicDisease"));
        chronicDiseaseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("chronicDiseaseDescription"));
        surgeryColumn.setCellValueFactory(new PropertyValueFactory<>("surgery"));
        occupationColumn.setCellValueFactory(new PropertyValueFactory<>("occupation"));
        civilStatusColumn.setCellValueFactory(new PropertyValueFactory<>("civilStatus"));


        // Fetch and display user's LifeRequests

        Callback<TableColumn<LifeRequest, String>, TableCell<LifeRequest, String>> cellFoctory = (TableColumn<LifeRequest, String> param) -> {
            // make cell containing buttons
            final TableCell<LifeRequest, String> cell = new TableCell<LifeRequest, String>() {
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
                        LifeRequest r = getTableView().getItems().get(getIndex());
                        if (Objects.equals(r.getStatus(), "Refuser") || Objects.equals(r.getStatus(), "Accepter")) {
                            showIcon.setDisable(true);
                            refuseIcon.setDisable(true);
                        }
                        showIcon.setOnMouseClicked((MouseEvent event) -> {
                            LifeRequest rowData = getTableView().getItems().get(getIndex());
                            LifeRequest selectedLifeRequest = (LifeRequest) rowData;
                            if (selectedLifeRequest != null) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ContratVie/addContratVie.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL); // Ensure the popup is modal
                                stage.setTitle("Add contrat vehicule");
                                addContratVie controller = loader.getController();
                                controller.initData(selectedLifeRequest);
                                stage.setScene(new Scene(root));
                                stage.showAndWait();
                                refreshTable();// Show the popup and wait for it to close
                               }

                        });

                        refuseIcon.setOnMouseClicked((MouseEvent event) -> {
                            LifeRequest rowData = getTableView().getItems().get(getIndex());
                            LifeRequest selectedLifeRequest = (LifeRequest) rowData;
                            LifeRequestService lifeRequestService = new LifeRequestService();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText("Confirmer le refus");
                            alert.setContentText("Êtes-vous sûr de vouloir refuser cette demande ?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                try {
                                    lifeRequestService.updateStatus(selectedLifeRequest.getId(), "Refuser");
                                    notifactionAlert(event);

                                    getTableView().getItems().clear();

                                    try {
                                        List<LifeRequest> lifeRequests = lifeRequestService.getAll();
                                        getTableView().getItems().addAll(lifeRequests);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
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

        LifeRequestTable.setItems(LifeRequestObservableList);
        statusColum.setCellFactory(column -> new TableCell<LifeRequest, String>() {
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


    private void showDetails(LifeRequest request) {


    }

    public void addEmployeeSearch() {
        keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchKey = newValue.trim().toLowerCase(); // Trim and convert to lowercase

            if (searchKey.isEmpty()) {

                LifeRequestTable.setItems(LifeRequestObservableList);
                refreshTable();
                return;
            }

            FilteredList<LifeRequest> filteredList = new FilteredList<>(LifeRequestObservableList, lifeRequest ->
                    String.valueOf(lifeRequest.getId()).contains(searchKey)
                            || lifeRequest.getOccupation().toLowerCase().contains(searchKey)
                            || lifeRequest.getDateRequest().toString().toLowerCase().contains(searchKey) // Convertir LocalDate en String et appliquer toLowerCase()
                            || lifeRequest.getStatus().toLowerCase().contains(searchKey)
                            || lifeRequest.getCivilStatus().toLowerCase().contains(searchKey)
                            || lifeRequest.getSurgery().toLowerCase().contains(searchKey)
                            || lifeRequest.getChronicDiseaseDescription().toLowerCase().contains(searchKey)
                            || lifeRequest.getAge().toLowerCase().contains(searchKey)
                            || lifeRequest.getChronicDisease().toString().contains(searchKey));

            SortedList<LifeRequest> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(LifeRequestTable.comparatorProperty());

            LifeRequestTable.setItems(sortedList);
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

    private void initializeSortComboBox() {
        sortComboBox.getSelectionModel().selectFirst();
    }
    ObservableList<LifeRequest> filteredData = FXCollections.observableArrayList();

    @FXML
    private void onSortOptionSelected() throws SQLException {
        LifeRequestTable.getItems().removeAll();
        String selectedOption = sortComboBox.getSelectionModel().getSelectedItem();

        filterQuestionsByType(selectedOption);

    }

    private void filterQuestionsByType(String status) throws SQLException {
        if (status.equals("None")){
            LifeRequests =  LifeRequestService.getAll();
            ObservableList<LifeRequest> observableQuestions = FXCollections.observableArrayList(LifeRequests);
            LifeRequestTable.setItems(observableQuestions);        }
        else{
            filteredData.clear();
            for (LifeRequest lifeRequest : LifeRequests) {
                System.out.println(lifeRequest.getStatus());
                if (lifeRequest.getStatus().equals(status)) {
                    filteredData.add(lifeRequest);
                }
            }
            LifeRequestTable.getItems().setAll(filteredData);

        }

    }


}