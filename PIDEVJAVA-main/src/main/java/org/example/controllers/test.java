package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class test implements Initializable {
    @FXML
    private VBox pnItems;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Node[] nodes = new Node[10];
        for (int i = 0; i < nodes.length; i++) {
            try {

                nodes[i] = FXMLLoader.load(getClass().getResource("test.fxml"));


                pnItems.getChildren().add(nodes[i]);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
