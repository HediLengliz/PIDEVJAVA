package org.example.controllers.sinisterProperty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.models.SinisterProperty;
import org.example.services.SinisterPropertyService;

import javafx.util.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class GetByUserIdSinisterPropertyController implements Initializable {

    private final SinisterPropertyService sinisterPropertyService = new SinisterPropertyService();
    @FXML
    private Label labelDescription;
    @FXML
    private Label labelLocation;
    @FXML
    private Label labelAmountSinister;


    @FXML
    private TextField userIdTextField;


    @FXML
    private TableView<SinisterProperty> propertyTableView;
    @FXML
    private TableColumn<SinisterProperty, Long> idColumn;
    @FXML
    private TableColumn<SinisterProperty, String> dateColumn;

    @FXML
    private TableColumn<SinisterProperty, String> locationColumn;
    @FXML
    private TableColumn<SinisterProperty, String> labelStatusSinister;
    @FXML
    private TableColumn<SinisterProperty, Void> actionColumn;
    @FXML
    private TableColumn<SinisterProperty, String> typeColumn;

    @FXML
    private TableColumn<SinisterProperty, String> descriptionColumn;
    private Stage stage ;
    private Scene scene ;

    private Parent root ;
//    @FXML
//    public void switchToSceneone() throws IOException {
//        System.out.println("zz");
//        root = FXMLLoader.load(getClass().getResource("/sinisterProperty/sinisterDetails.fxml"));
//        System.out.println("1");
//        stage = (Stage) userIdTextField.getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
    @FXML
    public void switchToSceneWithData(ActionEvent event,int id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sinisterProperty/sinisterDetails.fxml"));
        root = loader.load();

           SinisterDetailsController controller = loader.getController();
            controller.setSinisterDetails(id);


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void getByUserId(ActionEvent event) {
        try {
            int userId = Integer.parseInt(userIdTextField.getText());
            List<SinisterProperty> properties = sinisterPropertyService.getByUserId(userId);
            if (!properties.isEmpty()) {
                propertyTableView.getItems().setAll(properties);

            } else {
                showAlert(Alert.AlertType.INFORMATION, "Information", "No Sinister Properties found for User ID: " + userId);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid User ID.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve Sinister Properties: " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Callback<TableColumn<SinisterProperty, Void>, TableCell<SinisterProperty, Void>> getButtonCellFactory() {
        System.out.println("hhh");
        return new Callback<TableColumn<SinisterProperty, Void>, TableCell<SinisterProperty, Void>>() {
            @Override
            public TableCell<SinisterProperty, Void> call(final TableColumn<SinisterProperty, Void> param) {
                return new TableCell<SinisterProperty, Void>() {
                    private final Button viewButton = new Button("View Details");

                    {
                        viewButton.setOnAction(event -> {
                            SinisterProperty rowData = getTableView().getItems().get(getIndex());

                            try {
                                System.out.println("ddd");

                                switchToSceneWithData(event,rowData.getId().intValue());

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });

                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(viewButton);
                        }
                    }
                };
            }
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            System.out.println("sdfghjkl");
            List<SinisterProperty> properties = sinisterPropertyService.getAll();

            propertyTableView.getItems().setAll(properties);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve sinister properties: " + e.getMessage());
        }

        actionColumn.setCellFactory(getButtonCellFactory());
   }


    }



