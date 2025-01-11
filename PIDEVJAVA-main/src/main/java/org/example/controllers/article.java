package org.example.controllers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.bouncycastle.oer.its.etsi102941.AaEntry;
import org.example.models.Article;
import org.example.services.articleservices;
import org.example.utils.MyDatabase;
//import org.w3c.dom.Node;


public class article {
    @FXML
    private Button commentSectionButton;
    @FXML
    public ListView<Article> articleList;
    // mock method to load list items
    public void initialize() {

        // add a click Listener to ListView
        articleList.setOnMouseClicked(this::handleMouseClicked);

        articleservices articleservices = new articleservices();

        List<Article> articles = articleservices.getAll(); // assuming getAll returns a list of articles
        articleList.getItems().addAll(articles);

        articleList.setCellFactory(param -> new ListCell<Article>() {
            @Override
            protected void updateItem(Article item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle() + "\n" + item.getDescription() + "\n" + item.getAuthorname()+"\n" + item.getId() + "\n" + item.getDatepub());
                }
            }
        });
    }


    @FXML
    public void handleMouseClicked(MouseEvent event) {
        try {
            Parent commentSection = FXMLLoader.load(getClass().getResource("/commentsection.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(commentSection));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleCommentSectionButtonAction(ActionEvent actionEvent){
        try {

            Parent commentSection = FXMLLoader.load((getClass().getResource("/commentsection.fxml")));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(commentSection));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void handleArticleClick(MouseEvent event){
//        try {
//            Parent commentSection = FXMLLoader.load(getClass().getResource("/commmentsection.fxml"));
//            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(commentSection));
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
