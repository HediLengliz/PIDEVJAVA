package org.example.controllers;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.example.models.Question;
import org.example.models.Service;
import org.example.models.User;
import org.example.services.AnswerService;
import org.example.services.QuestionService;
import org.example.services.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ShowQuestionController {
    private QuestionService questionService = new QuestionService();
    private AnswerService answerService = new AnswerService();

    @FXML
    private Label questionLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label priorityLabel;
    @FXML
    private TableView<Service> ServiceTable;
    @FXML
    private TableColumn<Service, Void> actionColumn;
    Stage st;
    Scene sc;

    private int id;

    public void show(int id, Stage stage, Scene scene) throws SQLException {
        try {
            st = stage;
            sc = scene;
            this.id = id;
            System.out.println(id);
            Question q = questionService.getById(id);
            questionLabel.setText(q.getQuestion());
            typeLabel.setText(q.getType());
            priorityLabel.setText(q.getPriority().toString());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            List<Service> services = answerService.getAllServicesByQuestionId(this.id);
            ObservableList<Service> observableServices = FXCollections.observableArrayList(services);
            ServiceTable.setItems(observableServices);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        actionColumn.setCellFactory(getButtonCellFactory("show"));

    }


    private Stage stage;
    private Scene scene;

    private Parent root;

    @FXML
    public void switchToScene(ActionEvent event, String link) throws IOException {
        root = FXMLLoader.load(getClass().getResource(link));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void returnToQuestions(ActionEvent actionEvent) throws IOException {
        switchToScene(actionEvent, "/getQuestions.fxml");
    }

    public void deleteQuestion(ActionEvent actionEvent) throws SQLException, IOException {
        questionService.delete(this.id);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Question deleted successfully.");
        alert.showAndWait();
        //this.st.close();
        //this.st.setScene(this.sc);
        //this.st.show();


    }

    private Callback<TableColumn<Service, Void>, TableCell<Service, Void>> getButtonCellFactory(String btnName) {
        return new Callback<TableColumn<Service, Void>, TableCell<Service, Void>>() {
            @Override
            public TableCell<Service, Void> call(final TableColumn<Service, Void> param) {
                return new TableCell<Service, Void>() {
                    private final Button viewButton = new Button(btnName);
                    private final FontAwesomeIconView showIcon = new FontAwesomeIconView(FontAwesomeIcon.EYE);
                    private final FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);

                    {
                        showIcon.setOnMouseClicked(event -> {
                            Service rowData = getTableView().getItems().get(getIndex());
                            String action = viewButton.getText();
                            if ("show".equals(action)) { // Utiliser equals pour comparer les chaînes
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/showService.fxml"));
                                    Parent popupRoot = null;
                                    try {
                                        popupRoot = loader.load();
                                        ShowServiceController controller = loader.getController();
                                        controller.show(rowData.getId());
                                    } catch (IOException | SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Stage popupStage = new Stage();
                                    popupStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
                                    popupStage.setTitle("Service details"); // Set the title of the popup window
                                    // Set the scene with the loaded FXML for the popup
                                    Scene popupScene = new Scene(popupRoot);
                                    popupStage.setScene(popupScene);
                                    // Show the popup window and wait for it to close
                                    popupStage.showAndWait();
                                    refreshTable();


                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        editIcon.setOnMouseClicked(event -> {
                            Service rowData = getTableView().getItems().get(getIndex());
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateService.fxml"));
                                Parent popupRoot = null;
                                try {
                                    popupRoot = loader.load();
                                    UpdateServiceController controller = loader.getController();
                                    controller.show(rowData.getId());
                                } catch (IOException | SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                Stage popupStage = new Stage();
                                popupStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
                                popupStage.setTitle("Service details"); // Set the title of the popup window
                                // Set the scene with the loaded FXML for the popup
                                Scene popupScene = new Scene(popupRoot);
                                popupStage.setScene(popupScene);
                                // Show the popup window and wait for it to close
                                popupStage.showAndWait();
                                refreshTable();
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
                            HBox managebtn = new HBox(showIcon, editIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(showIcon, new Insets(2, 2, 0, 3));
                            HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                            setGraphic(managebtn);
                        }
                    }
                };
            }
        };
    }


    @FXML
    public void switchToSceneWithData(MouseEvent event, String link, int id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(link));
        root = loader.load();
        if (link == "/showService.fxml") {
            ShowServiceController controller = loader.getController();
            controller.show(id);
        } else if (link == "/newService.fxml") {
            AddServiceController controller = loader.getController();
            controller.show(id);

        } else {
            UpdateServiceController controller = loader.getController();
            controller.show(id);

        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToSceneWithData1(Event event, String link, int id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(link));
        root = loader.load();
        if (link == "/showService.fxml") {
            ShowServiceController controller = loader.getController();
            controller.show(id);
        } else if (link == "/newService.fxml") {
            AddServiceController controller = loader.getController();
            controller.show(id);

        } else {
            UpdateServiceController controller = loader.getController();
            controller.show(id);

        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addService(ActionEvent actionEvent) throws SQLException, IOException {
        //switchToSceneWithData1(actionEvent,"/newService.fxml",this.id);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newService.fxml"));
        Parent popupRoot = loader.load();

// Get the controller instance
        AddServiceController controller = loader.getController();

// Call the method on the controller and pass the parameters
        controller.show(id);

// Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Service");
        Scene popupScene = new Scene(popupRoot);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
        refreshTable();
    }

    @FXML
    private void refreshTable() throws SQLException {

        Scene scene;
        show(this.id, this.stage, this.scene);


    }

    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnQuestion;
    @FXML
    private Button btnContratVehicule;
    @FXML
    private Button btnContratHabitat;
    @FXML
    private Button btnContratVie;

    @FXML
    private Button btnVehicule;

    @FXML
    private Button btnHabitat;

    @FXML
    private Button btnVie;

    @FXML
    private Label lblTitle;

    @FXML
    private Pane pnlMenus;
    @FXML
    private Button btnSinistresV;

    @FXML
    private Button btnSinistresH;

    @FXML
    private Button btnRapport;

    @FXML
    private Button btnSinisterLife;

    @FXML
    private Button btnGetUsers;
    @FXML
    private Button btnGetReclamations;

    @FXML
    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnVehicule) {
            loadFXML("/VehicleRequest/get_deleteVehicleRequest.fxml");
            updateLabelText("Demandes de véhicules");

        } else if (actionEvent.getSource() == btnHabitat) {
            loadFXML("/PropertyRequest/get_deletePropertyRequest.fxml");
            updateLabelText("Demandes de habitat");

        } else if (actionEvent.getSource() == btnVie) {
            loadFXML("/LifeRequest/get_deleteLifeRequest.fxml");
            updateLabelText("Demandes de vie");

        } else if (actionEvent.getSource() == btnContratVie) {
            loadFXML("/ContratVie/get_deleteContratVie.fxml");
            updateLabelText("Contrats assurances vie");
        } else if (actionEvent.getSource() == btnContratHabitat) {
            loadFXML("/ContratHabitat/get_deleteContratHabitat.fxml");
            updateLabelText("Contrats assurances habitat");
        } else if (actionEvent.getSource() == btnContratVehicule) {
            loadFXML("/ContratVehicule/get_deleteContratVehicule.fxml");
            updateLabelText("Contrats assurances vehicule");
        } else if (actionEvent.getSource() == btnSinistresV) {
            loadFXML("/sinisterVehicle/getAllSinisterVehicle.fxml");
            updateLabelText("Sinistres véhicules");

        } else if (actionEvent.getSource() == btnSinistresH) {
            loadFXML("/sinisterProperty/getAllSinisterProperty.fxml");
            updateLabelText("Sinistres habitats");
        } else if (actionEvent.getSource() == btnRapport) {
            loadFXML("/rapport/getAllRapport.fxml");
            updateLabelText("Rapports");
        } else if (actionEvent.getSource() == btnSinisterLife) {
            loadFXML("/ShowSinisterLifeList.fxml");

        } else if (actionEvent.getSource() == btnGetUsers) {
            loadFXML("/users/allUsers.fxml");
            updateLabelText("All users");
        } else if (actionEvent.getSource() == btnGetReclamations) {
            loadFXML("/users/allReclamation.fxml");
            updateLabelText("All users");
        } else if (actionEvent.getSource() == btnQuestion) {
            loadFXML("/getQuestions.fxml");
            updateLabelText("Contrats assurances vehicule");
        }

    }

    private void loadFXML(String fxmlFilePath) {
        try {
            Node itemNode = FXMLLoader.load(getClass().getResource(fxmlFilePath));
            pnItems.getChildren().clear();
            pnItems.getChildren().add(itemNode);
            pnItems.setPrefWidth(itemNode.getLayoutBounds().getWidth());
            pnItems.setPrefHeight(itemNode.getLayoutBounds().getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLabelText(String text) {
        lblTitle.setText(text);
    }


    private final UserService userService = new UserService();
    private User selectedUser; // Holds the selected user for updating

    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {
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