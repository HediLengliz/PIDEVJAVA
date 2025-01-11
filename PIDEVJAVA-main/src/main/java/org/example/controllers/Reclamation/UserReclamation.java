package org.example.controllers.Reclamation;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.example.models.Reclamation;
import org.example.models.User;
import org.example.services.ReclamationService;
import org.example.utils.CurrentUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserReclamation {
    @FXML
    private TableView<Reclamation> userReclamation;

    @FXML
    private TableColumn<Reclamation, Integer> idColumn;

    @FXML
    private TableColumn<Reclamation, String> titleColumn;

    @FXML
    private TableColumn<Reclamation, String> dateColumn;

    private ReclamationService reclamationService;

    public UserReclamation() {
        // Initialize the ReclamationService
        reclamationService = new ReclamationService(); // You need to implement this class
    }

    public void initialize() {
        dateColumn.setCellValueFactory(cellData -> {
            ObservableValue<String> value = new ObservableValue<String>() {
                @Override
                public String getValue() {
                    Reclamation reclamation = cellData.getValue();
                    return reclamation != null ? reclamation.getDescription() : "";
                }

                @Override
                public void addListener(ChangeListener<? super String> listener) {
                    // Not needed for read-only property
                }

                @Override
                public void removeListener(ChangeListener<? super String> listener) {
                    // Not needed for read-only property
                }

                @Override
                public void addListener(InvalidationListener listener) {
                    // Not needed for read-only property
                }

                @Override
                public void removeListener(InvalidationListener listener) {
                    // Not needed for read-only property
                }
            };
            return value;
        });






    loadDate();
    }

    private User getCurrentUser() {
        // Implement this method to get the current user
        return CurrentUser.getCurrentUser(); // Placeholder, replace with actual implementation
    }

    private void showAlert(String title, String content) {
        // Implement your alert method here
    }
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableColumn<Reclamation, String> editCol;
    public void switchToSceneOne() throws IOException {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser!=null){
            root = FXMLLoader.load(getClass().getResource("/reclamation/AddReclamation.fxml"));
            stage = (Stage) userReclamation.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }

    }
    public void backToprofil() throws IOException {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser!=null){
            root = FXMLLoader.load(getClass().getResource("/users/profil.fxml"));
            stage = (Stage) userReclamation.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }

    }

    ObservableList<Reclamation> reclamationList = FXCollections.observableArrayList();

    @FXML
    private void refreshTable() {



        try {
            reclamationList.clear();
            ReclamationService rs=new ReclamationService();
           List<Reclamation> rl= reclamationService.getReclamationsByUser();
           reclamationList.addAll(rl);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }



    }
    private void loadDate() {

        refreshTable();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateReclamation"));

        // Fetch and display user's reclamations
        User currentUser = getCurrentUser(); // Implement this method to get the current user
        if (currentUser != null) {
            try {
                List<Reclamation> userReclamations = reclamationService.getReclamationsByUser();
                userReclamation.getItems().addAll(userReclamations);
            } catch (SQLException e) {
                showAlert("Error", "An error occurred while fetching reclamations.");
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Unable to determine current user.");
        }
        Callback<TableColumn<Reclamation, String>, TableCell<Reclamation, String>> cellFoctory = (TableColumn<Reclamation, String> param) -> {
            // make cell containing buttons
            final TableCell<Reclamation, String> cell = new TableCell<Reclamation, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EYE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                Reclamation reclamation = userReclamation.getSelectionModel().getSelectedItem();
                                if (reclamation != null) {
                                    ReclamationService rs = new ReclamationService();
                                    rs.delete(reclamation.getId());
                                    refreshTable();
                                }
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        });

                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            Reclamation reclamation = userReclamation.getSelectionModel().getSelectedItem();


                            if (reclamation != null) {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/reclamation/showReclamationDetals.fxml"));
                                try {
                                    loader.load();
                                } catch (IOException ex) {
                                    System.out.println(ex.getMessage());
                                }
                                ShowReclamationDetals addStudentController = loader.getController();

                                addStudentController.setTitleField(reclamation.getTitle());
                                addStudentController.setDescriptionArea(reclamation.getDescription());
                                addStudentController.setDatereclamation(String.valueOf(reclamation.getDateReclamation()));

                                Parent parent = loader.getRoot();
                                Stage stage = new Stage();
                                stage.setScene(new Scene(parent));
                                stage.initStyle(StageStyle.UTILITY);
                                stage.show();

                                                          }
                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                    }
                    setText(null);
                }

            };

            return cell;
        };

        editCol.setCellFactory(cellFoctory);
        userReclamation.setItems(reclamationList);



        //add cell of button edit

    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    private void showDetails(Reclamation request) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'utilisateur");
        alert.setHeaderText("Détails de la demande sélectionnée");

        alert.setContentText("ID : " + request.getId() + "\n" +
                "Title : " + request.getTitle() + "\n" +
                "Date : " + request.getDateReclamation() + "\n"+
                "Description : " + request.getDescription() + "\n"
        );

        alert.showAndWait();
    }


}
