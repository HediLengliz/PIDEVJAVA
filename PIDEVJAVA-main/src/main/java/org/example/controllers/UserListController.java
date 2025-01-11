package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.models.User;
import org.example.services.UserService;

import java.sql.SQLException;

public class UserListController {

    @FXML
    private TableView<User> tableViewUsers;

    @FXML
    private TableColumn<User, String> columnFirstName;

    @FXML
    private TableColumn<User, String> columnLastName;

    @FXML
    private TableColumn<User, String> columnEmail;

    // UserService instance
    private final UserService userService = new UserService();

    // This method is called when the FXML file is loaded
    public void initialize() {
        // Set up the columns in the table
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Load user data
        loadUserData();
    }

    private void loadUserData() {
        try {
            ObservableList<User> userList = FXCollections.observableArrayList(userService.getAll());
            tableViewUsers.setItems(userList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, maybe show an alert to the user
        }
    }
}
