package org.example.controllers;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;

public class QuoteTypeController {

    @FXML
    private AnchorPane autoInsurance;

    @FXML
    private AnchorPane lifeInsurance;

    @FXML
    private AnchorPane propertyInsurance;

    @FXML
    void initialize() {
        // Initialize the hover effect and redirection for each insurance type
        setupHoverEffect(autoInsurance);
        setupHoverEffect(lifeInsurance);
        setupHoverEffect(propertyInsurance);
    }

    private void setupHoverEffect(AnchorPane pane) {
        Pane pane1 = (Pane) pane.getChildren().get(0); // Assuming ImageView is the first child
        ImageView imageView = (ImageView) pane1.getChildren().get(0);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), imageView);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        pane.setOnMouseEntered(event -> {
            scaleTransition.playFromStart();
        });

        pane.setOnMouseExited(event -> {
            scaleTransition.stop();
            imageView.setScaleX(1);
            imageView.setScaleY(1);
        });

        // Redirect to quote creation on click
        pane.setOnMouseClicked(event -> {
            String insuranceType = pane.getId();
            try {
                redirectToQuoteCreation(event, insuranceType);
            } catch (IOException | SQLException e) {
                e.printStackTrace(); // Handle the exception properly in your application
            }
        });
    }

    private void redirectToQuoteCreation(Event event, String insuranceType) throws IOException, SQLException {
        System.out.println(insuranceType);
        if (insuranceType.equals("autoInsurance")){
            insuranceType = "auto";
        } else if (insuranceType.equals("lifeInsurance")) {
            insuranceType = "life";
        }
        else {
            insuranceType = "property";
        }

        switchToSceneWithData(event, "/newQuote.fxml", insuranceType);
    }

    @FXML
    public void switchToSceneWithData(Event event, String link, String type) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(link));
        Parent root = loader.load();
        if (link.equals("/newQuote.fxml")) {
            QuoteController controller = loader.getController();
            controller.show(type, 1);
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void backHome(ActionEvent event) {
        openWindow("/home.fxml", "List Vehicle Request");
    }
    @FXML
    void goProfile(ActionEvent event) {
        openWindow("/users/userInfo.fxml", "List Life Request");
    }
    @FXML
    private Pane pnItems = null;
    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    private void openWindow(String fxmlFilePath, String title) {
        try {
            root =FXMLLoader.load(getClass().getResource(fxmlFilePath));
            Stage stage = (Stage) pnItems.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
