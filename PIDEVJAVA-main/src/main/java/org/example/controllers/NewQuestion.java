package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.models.Question;
import org.example.services.QuestionService;

import java.io.IOException;
import java.sql.SQLException;

public class NewQuestion {
    private final QuestionService questionService = new QuestionService();
    @FXML
    private TextField question;
    @FXML
    private ChoiceBox<String> type;
    @FXML
    private TextField priority;
    @FXML
    private Text error;
    @FXML
    private Label errorLabel;
    @FXML
    void addQuestion(ActionEvent event) throws SQLException {

        String q = question.getText();
        String t = type.getValue();
        String p = priority.getText();
        boolean test = validateForm(q,t,p);
        if (!test){
            System.out.println("form not valid");
        }
        else {
            Question nQuestion = new Question(q, t, Integer.parseInt(p));
            try {
                this.questionService.add(nQuestion);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("question created successfully.");
                alert.showAndWait();
                closePopup();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }



    }

    private void closePopup() throws SQLException {
        // Code to close the popup/stage
        // You can get the stage from the root node of the FXML scene
        dashboard dashboardController = new dashboard();
        errorLabel.getScene().getWindow().hide();
    }
    public void initialize() {
        // Optionally, you can set a default value
        type.setValue("life");
    }
    public boolean validateForm(String q,String t,String p) {
        if (q.isEmpty() || t.isEmpty() || p.isEmpty()) {
            errorLabel.setText("empty fields !");
            return false;
        }
        try {
            int p1 = Integer.parseInt(p);
            if (p1 < 0) {
                errorLabel.setText("priority is a number !");
                return false; // Priority is out of range

            }
        } catch (NumberFormatException e) {
            errorLabel.setText("priority is a number !");
            return false;
        }
        return true;
    }

    @FXML
    public void handleQuestionInput(){
        if (question.getText().isEmpty()){
            question.setStyle("-fx-border-color: red;");

        }
        else{
            question.setStyle("-fx-border-color: green;");

        }
    }

    @FXML
    public void handlePriorityInput(){
        String p = priority.getText();

        if(!p.isEmpty()){
            if (!p.matches("[0-9]+")){
                priority.setStyle("-fx-border-color: red;");
            }
            else{
                priority.setStyle("-fx-border-color: green;");
            }
        }
        else{
            priority.setStyle("-fx-border-color: red;");

        }
    }


    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    @FXML
    public void switchToScene(ActionEvent event, String  link) throws IOException {
        root = FXMLLoader.load(getClass().getResource(link));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void returnToQuestions(ActionEvent actionEvent) throws IOException {
        switchToScene(actionEvent,"/getQuestions.fxml");
    }


}
