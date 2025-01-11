package org.example.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.models.Question;
import org.example.models.Quote;
import org.example.models.Service;
import org.example.models.User;
import org.example.services.AnswerService;
import org.example.services.QuestionService;
import org.example.services.QuoteService;
import org.example.utils.CurrentUser;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class QuoteController{
    private QuestionService questionService = new QuestionService();
    private QuoteService quoteService = new QuoteService();
    private AnswerService answerService = new AnswerService();
    @FXML
    private VBox questionContainer;

    @FXML
    private Label questionLabel;

    @FXML
    private VBox serviceContainer;

    @FXML
    private Text error;

    @FXML
    private Button next;
    @FXML
    private Button submit;
    @FXML
    private Button previous;

    private int currentQuestionIndex = 0;

    private String type ="life";

    private List<Question> questions;
    private List <Service> servicesHelper;
    private Map<Integer, Integer> selectedServices = new HashMap<>();

    private List<Integer> questionServices = new ArrayList<>();

    public void show(String type,int userId) throws SQLException {
        this.type = type;
        System.out.println(this.type);
        try {
            questions = questionService.getByType(this.type);
            submit.setVisible(false);
            previous.setVisible(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Initialize your UI components and load questions
        try {
            showQuestion(currentQuestionIndex);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void showQuestion(int index) throws SQLException {
        Question currentQuestion = questions.get(index);
        questionLabel.setText(currentQuestion.getQuestion());
        serviceContainer.getChildren().clear(); // Clear previous services
        List<Service> services = answerService.getAllServicesByQuestionId(currentQuestion.getId());
        int idService = -1;

        if (currentQuestionIndex == questions.size() - 1) {
            next.setVisible(false);
            submit.setVisible(true);
        } else {
            next.setVisible(true);
            submit.setVisible(false);
        }

        if (!selectedServices.isEmpty() && currentQuestionIndex < selectedServices.size()) {
            idService = selectedServices.get(currentQuestionIndex);
        }
        this.servicesHelper = services;

        int elementsPerRow = 3;
        ToggleGroup toggleGroup = new ToggleGroup();


        // Iterate over services, creating RadioButton elements and adding them to HBox containers
        for (int i = 0; i < services.size(); i++) {
            RadioButton radioButton = new RadioButton(services.get(i).getName());
            radioButton.getStyleClass().add("service-radio-button"); // Add the style class
            radioButton.setToggleGroup(toggleGroup);
            System.out.println("#" + String.valueOf(i));
            radioButton.setId(String.valueOf(i));
            if (idService == services.get(i).getId()) {
                radioButton.setSelected(true);
            }
            radioButton.setOnAction(event -> selectService(event));

            // Create a new HBox for each row of elements
            if (i % elementsPerRow == 0) {
                HBox row = new HBox();
                row.setSpacing(5); // Adjust spacing as needed
                row.setAlignment(Pos.CENTER); // Center align the elements in the row
                serviceContainer.getChildren().add(row);
            }

            // Get the last added HBox (current row) and add the RadioButton to it
            radioButton.getStyleClass().addAll("button-label", "hidden"); // Add necessary style classes
            HBox currentRow = (HBox) serviceContainer.getChildren().get(serviceContainer.getChildren().size() - 1);
            currentRow.getChildren().add(radioButton);
        }
    }

    public void selectService(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) event.getSource();
        String id = selectedRadioButton.getId();
        int serviceId = this.servicesHelper.get(Integer.parseInt(id)).getId();
        selectedServices.put(currentQuestionIndex, serviceId);
    }

    public void showPrevious() throws SQLException {
        if (currentQuestionIndex > 0) {
            error.setText("");
            currentQuestionIndex--;
            if (currentQuestionIndex == 0){
                previous.setVisible(false);
            }
            showQuestion(currentQuestionIndex);
        }

    }

    public void showNext() throws SQLException {
        if(selectedServices.containsKey(currentQuestionIndex)){
            error.setText("");

            if (currentQuestionIndex < questions.size() - 1) {
                previous.setVisible(true);
                currentQuestionIndex++;
                showQuestion(currentQuestionIndex);
            }
        }
        else{
            error.setText("please answer the question");
        }

    }

    public void submitForm(ActionEvent actionEvent) throws SQLException, IOException {
        if (selectedServices.size() == questions.size()){
            float price = 1;
            List<String> services = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : selectedServices.entrySet()) {
                int questionId = entry.getKey();
                int answer = entry.getValue();
                Service s = answerService.getServiceById(answer);
                services.add(s.getName());
                price = price * s.getPrice();
                System.out.println("Question ID: " + questionId + ", Answer: " + answer);
            }
            User u = CurrentUser.getCurrentUser();
            Quote quote = new Quote(this.type,price,u);
            quote.setServices(services);
            quoteService.add(quote);
            switchToSceneWithData(actionEvent,"/test2.fxml",u.getId());
        }
        else{
            error.setText("please answer all the questions");
        }


    }

    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    @FXML
    public void switchToSceneWithData(ActionEvent event,String  link,int id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(link));
        root = loader.load();
        if (link == "/test2.fxml"){
            QuotesController controller = loader.getController();
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
    void backHome(ActionEvent event) {
        openWindow("/home.fxml", "List Vehicle Request");
    }
    @FXML
    void goProfile(ActionEvent event) {
        openWindow("/users/profil.fxml", "List Life Request");
    }



    @FXML
    private Pane pnItems = null;

    private void openWindow(String fxmlFilePath, String title) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFilePath));
            Stage stage = (Stage) pnItems.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

