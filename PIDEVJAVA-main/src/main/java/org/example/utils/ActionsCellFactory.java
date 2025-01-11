package org.example.utils;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.controllers.User.UpdateUserController;
import org.example.models.User;
import org.example.services.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class ActionsCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        return new TableCell<>() {
            final Button updateButton = new Button("Update");
            final Button deleteButton = new Button("Delete");
            final HBox buttonBox = new HBox(updateButton, deleteButton);

            {
                updateButton.getStyleClass().add("action-button");
                deleteButton.getStyleClass().add("action-button-delete");
                buttonBox.getStyleClass().add("button-box"); // Assign an ID to the HBox

                updateButton.setOnAction(event -> {
                    System.out.println("aa");
                    S rowData = getTableView().getItems().get(getIndex());

                    User selectedUser = (User) rowData; // Get the selected user
                    if (selectedUser != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/users/UpdateUser.fxml"));
                            Parent root = loader.load();
                            UpdateUserController controller = loader.getController();
                            controller.initData(selectedUser); // Pass the ID of the selected user

                            Scene scene = new Scene(root);
                            Stage currentStage = (Stage) updateButton.getScene().getWindow();
                            currentStage.setScene(scene);
                            currentStage.show();
                        } catch (IOException e) {
                            e.printStackTrace(); // Handle the exception as needed
                        }
                    }
                });


                deleteButton.setOnAction(event -> {
                    S rowData = getTableView().getItems().get(getIndex());
                    if (rowData instanceof User) {
                        int userId = ((User) rowData).getId();
                        System.out.println("Delete clicked for user ID: " + userId);
                        UserService userService = new UserService();
                        try {
                            userService.delete(userId);
                            showAlert(Alert.AlertType.INFORMATION, "Success", "User Deleted", "User deleted successfully.");

                            // Refresh the table view by updating the underlying data source
                            ObservableList<User> userList = (ObservableList<User>) getTableView().getItems();
                            userList.removeIf(user -> user.getId() == userId);
                        } catch (SQLException e) {
                            showAlert(Alert.AlertType.ERROR, "Error", "Delete Failed", "Error deleting user: " + e.getMessage());
                        }
                    }
                });
            }

            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        };
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
