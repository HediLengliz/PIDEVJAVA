package org.example.controllers.sinisterProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.Modality;
import org.example.controllers.rapport.SinisterReportDetailsController;
import org.example.controllers.rapport.addRapportController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.models.SinisterProperty;
import org.example.models.SinisterVehicle;
import org.example.services.SinisterPropertyService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class getAllSinisterPropertyController implements Initializable {

    private final SinisterPropertyService sps = new SinisterPropertyService();

    @FXML
    private TableView<SinisterProperty> sinisterTableView;


    @FXML
    private TableColumn<SinisterProperty, Void> actionColumn;

    @FXML
    private TableColumn<SinisterProperty, String> labelStatusSinister;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    void displayProperties(ActionEvent event) {
        try {
            List<SinisterProperty> properties = sps.getAll();
            sinisterTableView.getItems().setAll(properties);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve sinister properties: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private Stage stage ;
    private Scene scene ;

    private Parent root ;


    private Callback<TableColumn<SinisterProperty, Void>, TableCell<SinisterProperty, Void>> getButtonCellFactory() {
        return new Callback<TableColumn<SinisterProperty, Void>, TableCell<SinisterProperty, Void>>() {
            @Override
            public TableCell<SinisterProperty, Void> call(final TableColumn<SinisterProperty, Void> param) {
                return new TableCell<SinisterProperty, Void>() {
                    private final Button viewButton = new Button("Details");

                    private final Button treatButton = new Button("Treat");


                    {
                        viewButton.setStyle("-fx-background-color: #ff9900");
                        viewButton.setOnAction(event -> {
                            SinisterProperty rowData = getTableView().getItems().get(getIndex());

                            try {
                                switchToSceneWithData(event, rowData.getId().intValue());
                            } catch (IOException | SQLException e) {
                                e.printStackTrace();
                            }
                        });

                        treatButton.setOnAction(event -> {
                            SinisterProperty rowData = getTableView().getItems().get(getIndex());

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/rapport/addRapport.fxml"));
                                Parent root = loader.load();

                                addRapportController controller = loader.getController();
                                controller.setSinisterProperty(rowData);

                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.setTitle("Add Rapport");
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            viewButton.setText("Details");
                            viewButton.setStyle("-fx-background-color: #ff9900");

                            SinisterProperty rowData = getTableView().getItems().get(getIndex());
                            HBox buttons = new HBox();
                            buttons.setSpacing(5);

                            Button viewButton = new Button("Details");
                            viewButton.setStyle("-fx-background-color: #e7e7e7;-fx-text-fill: black");

                            viewButton.setOnAction(event -> {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sinisterProperty/sinisterDetails.fxml"));
                                    Parent root = loader.load();

                                    SinisterDetailsController controller = loader.getController();
                                    controller.setSinisterDetails(rowData.getId().intValue());

                                    Stage popupStage = new Stage();
                                    popupStage.initModality(Modality.APPLICATION_MODAL);
                                    popupStage.setTitle("Sinister Details");
                                    popupStage.setScene(new Scene(root));
                                    popupStage.showAndWait();
                                } catch (IOException | SQLException e) {
                                    e.printStackTrace();
                                }
                            });
                            buttons.getChildren().add(viewButton);

                            if (rowData.getStatus_sinister().equals("en_cours")) {
                                Button treatButton = new Button("Treat");
                                treatButton.setStyle("-fx-background-color: #cbddfb;-fx-text-fill: black");

                                treatButton.setOnAction(event -> {
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/rapport/addRapport.fxml"));
                                        Parent root = loader.load();

                                        addRapportController controller = loader.getController();
                                        controller.setSinisterProperty(rowData);

                                        Stage stage = new Stage();
                                        stage.setScene(new Scene(root));
                                        stage.setTitle("Add Rapport");
                                        stage.show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                buttons.getChildren().add(treatButton);
                            } else if (rowData.getStatus_sinister().equals("traité")) {
                                Button viewReportButton = new Button("Report");
                                viewReportButton.setStyle("-fx-background-color: #e0d6dd;-fx-text-fill: black");
                                viewReportButton.setOnAction(event -> {
                                    try {
                                        switchToSceneWithData2(event, rowData.getId());
                                    } catch (IOException | SQLException e) {
                                        e.printStackTrace();
                                    }
                                });
                                buttons.getChildren().add(viewReportButton);
                            }

                            setGraphic(buttons);
                        }
                    }

                };
            }
        };
    }
    @FXML
    public void switchToSceneWithData2(ActionEvent event, long id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/rapport/sinisterRapportDetails.fxml"));
        Parent root = loader.load();

        SinisterReportDetailsController controller = loader.getController();
        controller.setSinisterReportDetails(id);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Sinister Report Details");
        popupStage.setScene(new Scene(root));
        popupStage.showAndWait();
    }
    @FXML
    public void switchToSceneWithData(ActionEvent event,int id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sinisterProperty/sinisterDetails.fxml"));
        Parent root = loader.load();

        SinisterReportDetailsController controller = loader.getController();
        controller.setSinisterReportDetails(id);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Sinister Report Details");
        popupStage.setScene(new Scene(root));
        popupStage.showAndWait();
    }
    @FXML
    private ComboBox<String> filterOrderByDate;
    @FXML
    private void handleFilterChangeOrderByDate(ActionEvent event) {
        String selectedOrder = filterOrderByDate.getValue();
        if (selectedOrder != null) {
            try {
                List<SinisterProperty> filteredProperties = sps.getAll();
                if (selectedOrder.equals("asc")) {
                    filteredProperties.sort(Comparator.comparing(SinisterProperty::getDate_sinister));
                } else if (selectedOrder.equals("desc")) {
                    filteredProperties.sort(Comparator.comparing(SinisterProperty::getDate_sinister).reversed());
                }
                sinisterTableView.getItems().setAll(filteredProperties);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve filtered sinister properties: " + e.getMessage());
            }
        }
    }
    @FXML
    private TextField keywordTextField;

    public void addSinisterPropertySearch() {
        // Get the complete list of properties from the database
        try {
            List<SinisterProperty> properties = sps.getAll();
            ObservableList<SinisterProperty> observableProperties = FXCollections.observableArrayList(properties);

            // Create a filtered list based on the complete list of properties
            FilteredList<SinisterProperty> filteredList = new FilteredList<>(observableProperties);

            // Add a listener to the keywordTextField
            keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                String searchKey = newValue.trim().toLowerCase(); // Trim and convert to lowercase

                if (searchKey.isEmpty()) {
                    // If the search key is empty, display all properties
                    filteredList.setPredicate(null);
                } else {
                    // Create a predicate to filter properties based on the search key
                    filteredList.setPredicate(property ->
                            String.valueOf(property.getId()).contains(searchKey)
                                    || property.getLocation().toLowerCase().contains(searchKey)
                                    || property.getDate_sinister().toString().toLowerCase().contains(searchKey)
                                    || property.getType_degat().toLowerCase().contains(searchKey)
                                    || property.getStatus_sinister().toLowerCase().contains(searchKey)
                    );
                }

                // Set the filtered list to the table view
                sinisterTableView.setItems(filteredList);
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve sinister properties: " + e.getMessage());
        }
    }


    @FXML
    private void handleFilterChange() {
        String selectedFilter = filterComboBox.getValue();
        if (selectedFilter != null) {
            try {
                List<SinisterProperty> filteredProperties;
                if (selectedFilter.equals("status = traité")) {
                    filteredProperties = sps.getStatus_sinister("traité");
                } else if (selectedFilter.equals("status = en cours")) {
                    filteredProperties = sps.getStatus_sinister("en_cours");
                } else {
                    System.out.println("filtrage mayekhdemch");
                    return;
                }
                sinisterTableView.getItems().setAll(filteredProperties);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve filtered sinister properties: " + e.getMessage());
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<SinisterProperty> properties = sps.getAll();
            sinisterTableView.getItems().setAll(properties);
            handleFilterChangeOrderByDate(new ActionEvent());
            addSinisterPropertySearch();
            ObservableList<String> filterOptions = FXCollections.observableArrayList("status = traité", "status = en cours");
            filterComboBox.setItems(filterOptions);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve sinister properties: " + e.getMessage());
        }
        actionColumn.setCellFactory(getButtonCellFactory());
    }
}
