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

public class UpdateQuestionController {
    private QuestionService questionService =new QuestionService();
    @FXML
    private TextField questionTF;
    @FXML
    private ChoiceBox<String> typeTF;
    @FXML
    private TextField priorityTF;
    @FXML
    private Text error;
    private int id;

    public void show(int id) throws SQLException {
        try {
            this.id = id;
            Question q = questionService.getById(id);
            System.out.println(q.getQuestion());
            questionTF.setText(q.getQuestion());
            typeTF.setValue(q.getType());
            priorityTF.setText(q.getPriority().toString());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void updateQuestion(ActionEvent event) throws SQLException, IOException {
        String q1 = questionTF.getText();
        String t = typeTF.getValue();
        String p = priorityTF.getText();

        boolean test = validateForm(q1,t,p);
        if (!test){
            System.out.println("form not valid");
        }
        else {
            Question q = new Question(this.id,questionTF.getText(),typeTF.getValue(),Integer.parseInt(priorityTF.getText()));
            questionService.update(q);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("question updated successfully.");
            alert.showAndWait();
            closePopup();

        }


    }
    private void closePopup()  {
        priorityTF.getScene().getWindow().hide();
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        switchToScene(event,"/getQuestions.fxml");

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

    public boolean validateForm(String q,String t,String p) {
        if (q.isEmpty() || t.isEmpty() || p.isEmpty()) {
            error.setText("empty fields !");
            return false;
        }
        try {
            int p1 = Integer.parseInt(p);
            if (p1 < 0) {
                error.setText("priority is a number !");
                return false; // Priority is out of range

            }
        } catch (NumberFormatException e) {
            error.setText("priority is a number !");
            return false;
        }
        return true;
    }

    @FXML
    public void handleQuestionInput(){
        if (questionTF.getText().isEmpty()){
            questionTF.setStyle("-fx-border-color: red;");

        }
        else{
            questionTF.setStyle("-fx-border-color: green;");

        }
    }

    @FXML
    public void handlePriorityInput(){
        String p = priorityTF.getText();

        if(!p.isEmpty()){
            if (!p.matches("[0-9]+")){
                priorityTF.setStyle("-fx-border-color: red;");
            }
            else{
                priorityTF.setStyle("-fx-border-color: green;");
            }
        }
        else{
            priorityTF.setStyle("-fx-border-color: red;");

        }
    }
}
