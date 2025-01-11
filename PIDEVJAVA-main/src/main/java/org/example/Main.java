package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage)  throws  Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/users/login.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("ProTechtini");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}