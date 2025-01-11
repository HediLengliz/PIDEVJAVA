package org.example.controllers.User;
import javafx.scene.control.Alert;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.models.User;
import org.example.services.UserService;
import org.example.utils.ActionsCellFactory;
import org.example.utils.CurrentUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsersController {
    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, Void> actionsColumn;

    @FXML
    private TableColumn<User, String> emailColumn;


    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    private final UserService userService = new UserService();

    private void showDetails(User request) {
        List<String> rolesList = request.getRoles();

        // Remove square brackets from roles list
        List<String> cleanedRolesList = rolesList.stream()
                .map(role -> role.replaceAll("\"", "")) // Remove all double quotes from each role
                .map(role -> role.replaceAll("^\\[|\\]$", "")) // Remove square brackets only if quotes were not removed

                .collect(Collectors.toList());

        System.out.println(cleanedRolesList);

        String rolesString = String.join("-", cleanedRolesList);
        System.out.println(rolesString);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'utilisateur");
        alert.setHeaderText("Détails de la demande sélectionnée");

        alert.setContentText("ID : " + request.getId() + "\n" +
                "Prénom : " + request.getFirstName() + "\n" +
                "Nom : " + request.getLastName() + "\n" +
                "Email : " + request.getEmail() + "\n" +
                "Cin :" + request.getCin() + "\n" +
                "Phone number: " + request.getPhoneNumber() + "\n" +
                "Roles :" + rolesString + "\n"
        );

        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(cellData -> {
            ObservableValue<String> value = new ObservableValue<String>() {
                @Override
                public String getValue() {
                    User user = cellData.getValue();
                    return user != null ? user.getLastName() : "";
                }

                @Override
                public void addListener(ChangeListener<? super String> listener) {
                    // Not needed for read-only property
                }

                @Override
                public void removeListener(ChangeListener<? super String> listener) {
                    // Not needed for read-only property
                }

                @Override
                public void addListener(InvalidationListener listener) {
                    // Not needed for read-only property
                }

                @Override
                public void removeListener(InvalidationListener listener) {
                    // Not needed for read-only property
                }
            };
            return value;
        });

        try {
            List<User> userList = userService.getAll();

            for (User user : userList) {
                List<String> rolesList = user.getRoles();
                String rolesString = String.join("-", rolesList); // Join roles with a delimiter "-"

                // Create a new mutable list to hold the formatted roles
                List<String> formattedRolesList = new ArrayList<>();
                formattedRolesList.add(rolesString);

                // Set the formatted roles list back to the User object
                user.setRoles(formattedRolesList);
            }

            usersTable.getItems().addAll(userList);

            // Set a custom cell factory for the rolesColumn to display formatted roles

            actionsColumn.setCellFactory(new ActionsCellFactory<>());
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
        usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showDetails(newSelection);
            }
        });
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void logoutAction() throws IOException {
        CurrentUser.clearCurrentUser();
        root = FXMLLoader.load(getClass().getResource("/users/login.fxml"));
        stage = (Stage) usersTable.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println("Logout successful!");
    }

    public TableView<User> getUsersTable() {
        return usersTable;
    }

    public void setUsersTable(TableView<User> usersTable) {
        this.usersTable = usersTable;
    }


    public void switchToSceneOne(User selectedUser) throws IOException {
        UpdateUserController controller = new UpdateUserController();
        System.out.println("abc");
        controller.initData(selectedUser);

        root = FXMLLoader.load(getClass().getResource("/users/UpdateUser.fxml"));
        stage = (Stage) usersTable.getScene().getWindow();
        System.out.println("d");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void addUser() throws IOException {
        root = FXMLLoader.load(getClass().getResource("/users/addUser.fxml"));
        stage = (Stage) usersTable.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
