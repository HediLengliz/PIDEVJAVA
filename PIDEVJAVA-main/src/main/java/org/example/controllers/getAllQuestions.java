package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.models.Question;
import org.example.models.SinisterLife;
import org.example.services.QuestionService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class getAllQuestions implements Initializable {
    private final QuestionService questionService = new QuestionService();

    @FXML
    private TableView<Question> QuestionTable;

    @FXML
    private TableColumn<Question, Void> actionColumn;

    @FXML
    private TableColumn<Question, Void> actionColumn1;

    private List<Question> questions;
    @FXML
    private ComboBox<String> sortComboBox;
    private ObservableList<Question> masterData = FXCollections.observableArrayList();

    @FXML
    void getAllQuestions() {
        try {
            questions = questionService.getAll();
            ObservableList<Question> observableQuestions = FXCollections.observableArrayList(questions);
            QuestionTable.setItems(observableQuestions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            questions = questionService.getAll();
            ObservableList<Question> observableQuestions = FXCollections.observableArrayList(questions);
            QuestionTable.setItems(observableQuestions);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set cell factory for action column
        actionColumn.setCellFactory(getButtonCellFactory());
        initializeSortComboBox();



    }

    // Define a method to create a cell factory for the action column
    private Callback<TableColumn<Question, Void>, TableCell<Question, Void>> getButtonCellFactory() {
        return new Callback<TableColumn<Question, Void>, TableCell<Question, Void>>() {
            @Override
            public TableCell<Question, Void> call(final TableColumn<Question, Void> param) {
                return new TableCell<Question, Void>() {
                    private final Button viewButton = new Button("show");
                    private final Button editbutton = new Button("edit");


                    {
                        viewButton.setOnAction(event -> {
                            Question rowData = getTableView().getItems().get(getIndex());
                            try {
                                Stage stage = (Stage) QuestionTable.getScene().getWindow();
                                Scene scene = QuestionTable.getScene();
                                switchToSceneWithData(event,"/showQuestion.fxml",rowData.getId(),stage,scene);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            System.out.println("View details button clicked for ID: " + rowData.getId());
                        });
                        editbutton.setOnAction(event -> {
                            Question rowData = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateQuestion.fxml"));
                            Parent popupRoot = null;
                            try {
                                popupRoot = loader.load();
                                UpdateQuestionController controller = loader.getController();
                                Scene scene = QuestionTable.getScene();
                                controller.show(rowData.getId());
                            } catch (IOException | SQLException e) {
                                throw new RuntimeException(e);
                            }
                            Stage popupStage = new Stage();
                            popupStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
                            popupStage.setTitle("edit Question"); // Set the title of the popup window
                            // Set the scene with the loaded FXML for the popup
                            Scene popupScene = new Scene(popupRoot);
                            popupStage.setScene(popupScene);
                            // Show the popup window and wait for it to close
                            popupStage.showAndWait();
                            refreshTable();


                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox();
                            buttons.setSpacing(5);
                            buttons.getChildren().add(viewButton);
                            buttons.getChildren().add(editbutton);
                            setGraphic(buttons);
                        }
                    }
                };
            }
        };
    }
    @FXML
    private void refreshTable() {


        getAllQuestions();





    }

    public void addNewQuestion(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newQuestion.fxml"));
        Parent popupRoot = loader.load();

        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
        popupStage.setTitle("Add Question"); // Set the title of the popup window
        // Set the scene with the loaded FXML for the popup
        Scene popupScene = new Scene(popupRoot);
        popupStage.setScene(popupScene);
        // Show the popup window and wait for it to close
        popupStage.showAndWait();
        refreshTable();

    }
    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    @FXML
    public void switchToScene(ActionEvent event,String  link) throws IOException {
        root = FXMLLoader.load(getClass().getResource(link));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void switchToSceneWithData(ActionEvent event,String  link,int id,Stage stage,Scene scene) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(link));
        root = loader.load();
        if (link == "/showQuestion.fxml"){
            ShowQuestionController controller = loader.getController();
            System.out.println(stage);
            controller.show(id,stage,scene);
        }
        else{
            UpdateQuestionController controller = loader.getController();
            controller.show(id);

        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private TextField keywordTextField;

    public void addSinisterVehicleSearch() {
        // Get the complete list of properties from the database
        try {
            List<Question> questions = questionService.getAll();
            ObservableList<Question> observableProperties = FXCollections.observableArrayList(questions);

            // Create a filtered list based on the complete list of properties
            FilteredList<Question> filteredList = new FilteredList<>(observableProperties);

            // Add a listener to the keywordTextField
            keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                String searchKey = newValue.trim().toLowerCase(); // Trim and convert to lowercase

                if (searchKey.isEmpty()) {
                    // If the search key is empty, display all properties
                    filteredList.setPredicate(null);
                } else {
                    // Create a predicate to filter properties based on the search key
                    filteredList.setPredicate(question ->
                            String.valueOf(question.getId()).contains(searchKey)
                                    || question.getType().toLowerCase().contains(searchKey)
                                    || question.getQuestion().toLowerCase().contains(searchKey)

                                    || question.getPriority().toString().toLowerCase().contains(searchKey)
                    );
                }

                // Set the filtered list to the table view
                QuestionTable.setItems(filteredList);
            });
        } catch (SQLException e) {
        }
    }
    private void initializeSortComboBox() {
        sortComboBox.getSelectionModel().selectFirst();
    }
    ObservableList<Question> filteredData = FXCollections.observableArrayList();

    @FXML
    private void onSortOptionSelected() throws SQLException {
        QuestionTable.getItems().removeAll();
        String selectedOption = sortComboBox.getSelectionModel().getSelectedItem();

        filterQuestionsByType(selectedOption);

    }

    private void filterQuestionsByType(String type) throws SQLException {
        if (type.equals("None")){
            questions = questionService.getAll();
            ObservableList<Question> observableQuestions = FXCollections.observableArrayList(questions);
            QuestionTable.setItems(observableQuestions);        }
        else{
            filteredData.clear();
            for (Question question : questions) {
                if (question.getType().equals(type)) {
                    filteredData.add(question);
                }
            }
            QuestionTable.getItems().setAll(filteredData);

        }

    }
        // Since masterData is the base of filteredData, changes there will reflect automatically
    }

