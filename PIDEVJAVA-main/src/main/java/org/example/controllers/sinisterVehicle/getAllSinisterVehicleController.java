package org.example.controllers.sinisterVehicle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.models.SinisterProperty;
import org.example.models.SinisterVehicle;
import org.example.services.SinisterVehicleService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class getAllSinisterVehicleController implements Initializable {
    private final SinisterVehicleService svService = new SinisterVehicleService();

    @FXML
    private TableView<SinisterVehicle> sinisterTableView;

    @FXML
    private TableColumn<SinisterVehicle, Void> actionColumn;

    private Stage stage ;
    private Scene scene ;

    private Parent root ;

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
//    @FXML
//    public void switchToSceneWithData(ActionEvent event,int id) throws IOException, SQLException {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/sinisterVehicle/viewSVDet.fxml"));
//        root = loader.load();
//
//        SinisterDetailsVController controller = loader.getController();
//        controller.setSinisterDetails(id);
//
//
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
    @FXML
    public void switchToSceneWithData(ActionEvent event, int id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sinisterVehicle/viewSVDet.fxml"));
        Parent root = loader.load();

        SinisterDetailsVController controller = loader.getController();
        controller.setSinisterDetails(id);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Sinister Report Details");
        popupStage.setScene(new Scene(root));
        popupStage.showAndWait();
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
    private Callback<TableColumn<SinisterVehicle, Void>, TableCell<SinisterVehicle, Void>> getButtonCellFactory() {
        return new Callback<TableColumn<SinisterVehicle, Void>, TableCell<SinisterVehicle, Void>>() {
            @Override
            public TableCell<SinisterVehicle, Void> call(final TableColumn<SinisterVehicle, Void> param) {
                return new TableCell<SinisterVehicle, Void>() {

                    private final Button viewButton = new Button("View Details");
                    private final Button treatButton = new Button("Treat");
                    {
                        viewButton.setOnAction(event -> {
                            SinisterVehicle rowData = getTableView().getItems().get(getIndex());

                            try {
                                switchToSceneWithData(event, rowData.getId().intValue());
                            } catch (IOException | SQLException e) {
                                e.printStackTrace();
                            }
                        });
                        treatButton.setOnAction(event -> {
                            SinisterVehicle rowData = getTableView().getItems().get(getIndex());

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/rapport/addRapportV.fxml"));
                                Parent root = loader.load();
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

                            SinisterVehicle rowData = getTableView().getItems().get(getIndex());
                            HBox buttons = new HBox();
                            buttons.setSpacing(5);


                            if (rowData.getStatus_sinister().equals("en_cours")) {
                                Button viewButton = new Button(" Details");

                                viewButton.setStyle("-fx-background-color: #e7e7e7;-fx-text-fill: black");
                                viewButton.setOnAction(event -> {
                                    try {
                                        switchToSceneWithData(event, rowData.getId().intValue());
                                    } catch (IOException | SQLException e) {
                                        e.printStackTrace();
                                    }
                                });
                                buttons.getChildren().add(viewButton);

                                Button treatButton = new Button("Treat");
                                treatButton.setStyle("-fx-background-color: #cbddfb;-fx-text-fill: black");
                                treatButton.setOnAction(event -> {
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/rapport/addRapportV.fxml"));
                                        Parent root = loader.load();
                                        addRapportController controller = loader.getController();
                                        controller.setSinisterVehicle(rowData);
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
                                Button viewReportButton = new Button("View Report");
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
    private ComboBox<String> filterComboBox;
    @FXML
    private ComboBox<String> filterOrderByDate;

    @FXML
    private void handleFilterChangeOrderByDate(ActionEvent event) {
        String selectedOrder = filterOrderByDate.getValue();
        if (selectedOrder != null) {
            try {
                List<SinisterVehicle> filteredProperties = svService.getAll();
                if (selectedOrder.equals("asc")) {
                    filteredProperties.sort(Comparator.comparing(SinisterVehicle::getDate_sinister));
                } else if (selectedOrder.equals("desc")) {
                    filteredProperties.sort(Comparator.comparing(SinisterVehicle::getDate_sinister).reversed());
                }
                sinisterTableView.getItems().setAll(filteredProperties);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve filtered sinister properties: " + e.getMessage());
            }
        }
    }
    @FXML
    private void handleFilterChange() {
        String selectedFilter = filterComboBox.getValue();
        if (selectedFilter != null) {
            try {
                List<SinisterVehicle> filteredProperties;
                if (selectedFilter.equals("all")) {
                    filteredProperties = svService.getAll();
                } else if (selectedFilter.equals("status = traité")) {
                    filteredProperties = svService.getStatus_sinister("traité");
                } else if (selectedFilter.equals("status = en_cours")) {
                    filteredProperties = svService.getStatus_sinister("en_cours");
                } else {
                    // Handle invalid filter option
                    return;
                }
                sinisterTableView.getItems().setAll(filteredProperties);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve filtered sinister properties: " + e.getMessage());
            }
        }
    }
    @FXML
    private TextField keywordTextField;

    public void addSinisterVehicleSearch() {
        // Get the complete list of properties from the database
        try {
            List<SinisterVehicle> properties = svService.getAll();
            ObservableList<SinisterVehicle> observableProperties = FXCollections.observableArrayList(properties);

            // Create a filtered list based on the complete list of properties
            FilteredList<SinisterVehicle> filteredList = new FilteredList<>(observableProperties);

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
    private TableColumn<SinisterVehicle, String> imageColumn;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            List<SinisterVehicle> properties = svService.getAll();
            System.out.println(properties+"aaa");
            sinisterTableView.getItems().setAll(properties);
            addSinisterVehicleSearch();
            handleFilterChangeOrderByDate(new ActionEvent());
            handleFilterChange();
        } catch (SQLException e) {

            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve sinister properties: " + e.getMessage());
        }
        actionColumn.setCellFactory(getButtonCellFactory());
        // Assuming TableColumn is named 'imageColumn'
        imageColumn.setCellFactory(column -> {
            return new TableCell<SinisterVehicle, String>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null || item.isEmpty()) {
                        setGraphic(null); // Clear the cell if no imageUUID is provided
                    } else {
                        String imagePath = "images/profiles/" + item; // Assuming item is the UUID
                        File imageFile = new File(imagePath);

                        if (imageFile.exists()) {
                            Image image = new Image(imageFile.toURI().toString());
                            imageView.setImage(image);
                            imageView.setFitWidth(67.0);
                            imageView.setFitHeight(73.0);
                            setGraphic(imageView);
                        } else {
                            System.out.println("Profile picture file not found at: " + imagePath);
                            setGraphic(null); // Clear the cell if profile picture file not found
                        }
                    }
                }
            };
        });

    }
}
