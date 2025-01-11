package org.example.controllers.User;
import com.google.gson.Gson; // You may need to add the Gson library to your project
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.controllers.Reclamation.ShowReclamationDetals;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.example.models.*;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;

import org.example.services.ReclamationService;
import org.example.services.UserService;
import org.example.utils.CurrentUser;
import org.example.utils.MyDatabase;

import javax.swing.*;


public class dashboardController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Label username;

    @FXML
    private Button home_btn;

    @FXML
    private Button addEmployee_btn;

    @FXML
    private Button salary_btn;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_totalEmployees;

    @FXML
    private Label home_totalPresents;

    @FXML
    private Label home_totalInactiveEm;

    @FXML
    private BarChart<?, ?> home_chart;

    @FXML
    private AnchorPane addEmployee_form;

    @FXML
    private TableView<User> addEmployee_tableView;

    @FXML
    private TableColumn<User, String> col_userID;

    @FXML
    private TableColumn<User, String> col_firstName;

    @FXML
    private TableColumn<User, String> col_lastName;
    @FXML
    private TableColumn<User, String> col_cin;

    @FXML
    private TableColumn<User, String> col_address;

    @FXML
    private TableColumn<User, String> col_phoneNum;

    @FXML
    private TableColumn<User, String> col_email;



    @FXML
    private TextField addEmployee_search;

    @FXML
    private TextField selectedUser_id;

    @FXML
    private TextField selectedUser_firstName;

    @FXML
    private TextField selectedUser_lastName;

    @FXML
    private CheckComboBox<String> selectedUser_role;
    @FXML
    private ComboBox<String> addEmployee_position;
    @FXML
    private TextField selectedUser_phoneNum;

    @FXML
    private TextField selectedUser_email;

    @FXML
    private ImageView selectedUser_image;
    @FXML
    private TextField selectedUser_cin;


    @FXML
    private Button addEmployee_importBtn;

    @FXML
    private Button addEmployee_addBtn;

    @FXML
    private Button addEmployee_updateBtn;

    @FXML
    private Button addEmployee_deleteBtn;

    @FXML
    private Button addEmployee_clearBtn;

    @FXML
    private AnchorPane salary_form;

    @FXML
    private TextField salary_employeeID;

    @FXML
    private Label salary_firstName;

    @FXML
    private Label salary_lastName;

    @FXML
    private Label salary_position;

    @FXML
    private TextField salary_salary;

    @FXML
    private Button salary_updateBtn;

    @FXML
    private Button salary_clearBtn;

    @FXML
    private TableView<User> salary_tableView;

    @FXML
    private TableColumn<User, String> salary_col_employeeID;

    @FXML
    private TableColumn<User, String> salary_col_firstName;

    @FXML
    private TableColumn<User, String> salary_col_lastName;

    @FXML
    private TableColumn<User, String> salary_col_position;

    @FXML
    private TableColumn<User, String> salary_col_salary;

    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    private Image image;
    MyDatabase db=MyDatabase.getInstance();

    public void homeTotalEmployees() {

    }

    public void homeEmployeeTotalPresent() {
    }
    @FXML
    private void addEmployeePositionList(ActionEvent event) {
        // Handle ComboBox selection change event here
        String selectedTable = (String) addEmployee_position.getValue();
        if (selectedTable != null) {
            List<String> privileges = tablePrivileges.get(selectedTable);
            // Display privileges or perform other actions based on selection
            System.out.println("Privileges for " + selectedTable + ": " + privileges);
        }
    }
    Map<String, List<String>> tablePrivileges = new HashMap<>();

//    public void priviliges() throws SQLException {
//        // Initialize a map to store table names and their CRUD privileges
//
//        // Get tables and their CRUD privileges
//        List<String> tables = db.getAllTables();
//        for (String tableName : tables) {
//            List<String> privileges = getPrivilegesForTable(tableName); // Implement this method to get privileges for a table
//            tablePrivileges.put(tableName, privileges);
//        }
//
//        // Convert the map to JSON string using Gson
//        Gson gson = new Gson();
//        String json = gson.toJson(tablePrivileges);
//
//        System.out.println(json);
//    }

    // Example method to get privileges for a table (replace with your logic)
    private List<String> getPrivilegesForTable(String tableName) {
        // Example logic to get privileges based on table name
        List<String> privileges = new ArrayList<>();
        privileges.add("can read");
        privileges.add("can create");
        privileges.add("can delete");
        privileges.add("can update");

        return privileges;
    }
    public void addEmployeeShowListData() throws SQLException {
        if (addEmployee_tableView != null) {
            List<User> userListData = addEmployeeListData();
            addEmployeeList = FXCollections.observableArrayList(userListData);

            col_userID.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            col_cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
            col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
            col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
            col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

            addEmployee_tableView.setItems(addEmployeeList);
        }

    }
    public void addEmployeeSearch() {
        addEmployeeReset();
        addEmployee_search.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchKey = newValue.trim().toLowerCase(); // Trim and convert to lowercase

            if (searchKey.isEmpty()) {
                addEmployee_tableView.setItems(addEmployeeList); // Show all items when search text is empty
                return;
            }

            addEmployee_tableView.setItems(addEmployeeList.stream()
                    .filter(user -> user.getId().toString().contains(searchKey)
                            || user.getFirstName().toLowerCase().contains(searchKey)
                            || user.getLastName().toLowerCase().contains(searchKey)
                            || user.getCin().toLowerCase().contains(searchKey)
                            || user.getEmail().toLowerCase().contains(searchKey)
                            || user.getAddress().toLowerCase().contains(searchKey)
                            || user.getRoles().toString().contains(searchKey))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        });
    }


    @FXML
    TextField selectedUser_claims;


    public void selectUser() {

        User employeeD = addEmployee_tableView.getSelectionModel().getSelectedItem();
        if (employeeD == null) {
            return;
        }

        selectedUser_id.setText(String.valueOf(employeeD.getId()));
        selectedUser_firstName.setText(employeeD.getFirstName());
        selectedUser_lastName.setText(employeeD.getLastName());
        selectedUser_phoneNum.setText(employeeD.getPhoneNumber());
        selectedUser_email.setText(employeeD.getEmail());
        List<String> listroles= Arrays.stream(rolesArray).toList();
        List<String> cleanedRolesList = employeeD.getRoles().stream()
                .map(role -> role.replaceAll("\"", "")) // Remove all double quotes from each role
                .map(role -> role.replaceAll("^\\[|\\]$", "")) // Remove square brackets only if quotes were not removed

                .collect(Collectors.toList());
        System.out.println(cleanedRolesList);
        selectedUser_role.getCheckModel().clearChecks(); // Clear previous selections

        for (int i = 0; i < cleanedRolesList.size(); i++) {
            selectedUser_role.getCheckModel().check(listroles.indexOf(cleanedRolesList.get(i)));
            ;
        }
        selectedUser_cin.setText(employeeD.getCin());
        selectedUser_address.setText(employeeD.getAddress());
        selectedUser_claims.setText(employeeD.getClaims());
        System.out.println(selectedUser_claims.getText());
        String jsonString = employeeD.getClaims();

        if (jsonString != null) {
            // Parse the JSON string
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

            // Check if the key "blocked" exists in the JSON object
            if (jsonObject.has("blocked")) {
                // Get the value of the "blocked" key
                boolean isBlocked = jsonObject.get("blocked").getAsBoolean();
                userBlocked=isBlocked;
                if (userBlocked) {
                    addEmployee_deleteBtn.setText("Unblock");
                }

                else{
                    addEmployee_deleteBtn.setText("Block");
                }
                System.out.println("Is blocked: " + isBlocked);
            } else {
                userBlocked=false;
                addEmployee_deleteBtn.setText("Block");

                System.out.println("Key 'blocked' not found in JSON.");
            }
        } else {
            System.out.println("JSON string is null.");
        }
    }
    Boolean userBlocked=false;
    UserService us=new UserService();
    private final String[] rolesArray = {
            "Expert",
            "Pharmacist",
            "Doctor",
            "Agent",
            "Sous Admin",
            "Admin",
            "Super Admin"
    };

    public List<User> addEmployeeListData() throws SQLException {
        UserService us=new UserService();
        ArrayList <User> userList= (ArrayList<User>) us.getAll();
        return userList;
    }
    public void homeTotalInactive() {
    }

    public void homeChart() {
    }
    public void refresh() throws SQLException {
        // Convert your ArrayList to ObservableList using FXCollections.observableArrayList()
        ObservableList<User> observableList = FXCollections.observableArrayList(addEmployeeListData());

        // Set the ObservableList to your TableView
        addEmployee_tableView.setItems(observableList);
    }
    public void addEmployeeAdd() throws SQLException {
        System.out.println("a");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/users/addUser.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Ensure the popup is modal
        stage.setTitle("Add New User");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        addEmployeeShowListData();


    }
    @FXML
    private TextArea selectedUser_address;
    @FXML
    private void addEmployeeUpdate() {
        try {

            int userId = Integer.parseInt(selectedUser_id.getText());
            String firstName = selectedUser_firstName.getText();
            String lastName = selectedUser_lastName.getText();
            List<String> role =  selectedUser_role.getCheckModel().getCheckedItems(); // Assuming the ComboBox contains String items

            System.out.println(role);
            String email = selectedUser_email.getText(); // Assuming the ComboBox contains String items
            String phoneNumber = selectedUser_phoneNum.getText();
            if (email.isEmpty()  || firstName.isEmpty()) {
                System.out.println("ab");
                return;
            }
            if(role.isEmpty()){
                role=new ArrayList<>();
                role.add("User");
            }
            User updatedUser = new User(email, role, firstName, lastName, selectedUser_cin.getText(), selectedUser_address.getText(), phoneNumber);
            updatedUser.setId(userId);
            UserService us=new UserService();
            us.update(updatedUser);
            notifactionAlert("User updated successfully.");
            addEmployeeShowListData();
            System.out.println("User updated successfully.");
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Select a user first.");
            alert.showAndWait();
            System.err.println("Invalid user ID format.");
        } catch (SQLException | IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Error updating user: " + e.getMessage());
            alert.showAndWait();

        }
    }
    private void notifactionAlert(String text) {
        FontAwesomeIconView confirmationIcon = new FontAwesomeIconView();
        confirmationIcon.setGlyphName("CHECK_CIRCLE"); // Utilisation de l'icône de confirmation
        confirmationIcon.setSize("2em"); // Taille de l'icône
        confirmationIcon.setFill(Color.GREEN); // Changer la couleur de l'icône en vert

        Notifications notificationBuilder = Notifications.create()
                .title("Success")
                .text(text)
                .graphic(confirmationIcon)
                .hideAfter(Duration.seconds(3))
                .position(Pos.TOP_RIGHT);
        notificationBuilder.show();
    }
    private boolean isCurrentUserBlocked() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            String claimsJson = currentUser.getClaims();
            JsonObject jsonObject = JsonParser.parseString(claimsJson).getAsJsonObject();
            return jsonObject.has("blocked") && jsonObject.get("blocked").getAsBoolean();
        }
        return false;
    }

    // Example usage in a method in your controller

    @FXML
    public void addEmployeeDelete() throws SQLException {
        String cin = selectedUser_cin.getText();
        if (cin == null || cin.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Select a user first.");
            alert.showAndWait();
        } else {
            if ( userBlocked!=null && userBlocked) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmation");
                confirmAlert.setHeaderText("Block User");
                confirmAlert.setContentText("Are you sure you want to block this user?");
                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        int userId = Integer.parseInt(selectedUser_id.getText());
                        us.unblockUserById(userId);
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("User Unblocked successfully.");
                        successAlert.showAndWait();
                        addEmployeeShowListData(); // Assuming this method updates your UI list
                    } catch (SQLException | NumberFormatException e) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Error deleting user: " + e.getMessage());
                        errorAlert.showAndWait();
                    }
                }
                return ;
            }
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Block User");
            confirmAlert.setContentText("Are you sure you want to block this user?");
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    int userId = Integer.parseInt(selectedUser_id.getText());
                    us.blockUserById(userId);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("User Blocked successfully.");
                    successAlert.showAndWait();
                    addEmployeeShowListData(); // Assuming this method updates your UI list
                } catch (SQLException | NumberFormatException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Error deleting user: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        }
    }
    public void addEmployeeReset() {
        selectedUser_cin.clear();
        selectedUser_address.clear();
        selectedUser_email.clear();
        selectedUser_id.clear();
        selectedUser_firstName.clear();
        selectedUser_lastName.clear();
        selectedUser_role.getCheckModel().clearChecks();
        selectedUser_phoneNum.clear();
    }

    public void addEmployeeInsertImage() {}

    private String[] positionList = {"Marketer Coordinator", "Web Developer (Back End)", "Web Developer (Front End)", "App Developer"};

    public void addEmployeePositionList() {
    }

    private String[] listGender = {"Male", "Female", "Others"};

    public void addEmployeeGendernList() {
    }




    private ObservableList<User> addEmployeeList;



    public void salaryUpdate() {
    }

    public void salaryReset() {
        salary_employeeID.setText("");
        salary_firstName.setText("");
        salary_lastName.setText("");
        salary_position.setText("");
        salary_salary.setText("");
    }

    public ObservableList<User> salaryListData() {
        return new ObservableList<User>() {
            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<User> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(User user)
            {
                System.out.println("a");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("users/addUserForm.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL); // Ensure the popup is modal
                stage.setTitle("Add New User");
                stage.setScene(new Scene(root));
                stage.showAndWait(); // Show the popup and wait for it to close
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends User> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends User> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public User get(int index) {
                return null;
            }

            @Override
            public User set(int index, User element) {
                return null;
            }

            @Override
            public void add(int index, User element) {

            }

            @Override
            public User remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<User> listIterator() {
                return null;
            }

            @Override
            public ListIterator<User> listIterator(int index) {
                return null;
            }

            @Override
            public List<User> subList(int fromIndex, int toIndex) {
                return null;
            }

            @Override
            public void addListener(ListChangeListener<? super User> listChangeListener) {

            }

            @Override
            public void removeListener(ListChangeListener<? super User> listChangeListener) {

            }

            @Override
            public boolean addAll(User... users) {
                return false;
            }

            @Override
            public boolean setAll(User... users) {
                return false;
            }

            @Override
            public boolean setAll(Collection<? extends User> collection) {
                return false;
            }

            @Override
            public boolean removeAll(User... users) {
                return false;
            }

            @Override
            public boolean retainAll(User... users) {
                return false;
            }

            @Override
            public void remove(int i, int i1) {

            }
        };
    }

    private ObservableList<User> salaryList;

    public void salaryShowListData() {



    }

    public void salarySelect() {

    }

    public void defaultNav() {
        if (addEmployee_btn != null) {
            addEmployee_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #6681ea, #6084ef);");

        }
    }

    public void displayUsername() {

    }
    @FXML
    Label labeltitrereclamations;
    public void switchForm(ActionEvent event) {

        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(false);
            labeltitrereclamations.setVisible(false);

            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #6681ea, #6084ef);");
            addEmployee_btn.setStyle("-fx-background-color:transparent");
            salary_btn.setStyle("-fx-background-color:transparent");

            homeTotalEmployees();
            homeEmployeeTotalPresent();
            homeTotalInactive();
            homeChart();

        } else if (event.getSource() == addEmployee_btn) {
            home_form.setVisible(false);
            addEmployee_form.setVisible(true);
            salary_form.setVisible(false);
            labeltitrereclamations.setVisible(false);

            addEmployee_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #6681ea, #6084ef);");
            home_btn.setStyle("-fx-background-color:transparent");
            salary_btn.setStyle("-fx-background-color:transparent");

            addEmployeeGendernList();
            addEmployeePositionList();
            addEmployeeSearch();

        } else if (event.getSource() == salary_btn) {
            home_form.setVisible(false);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(true);
            labeltitrereclamations.setVisible(true);

            salary_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #6681ea, #6084ef);");
            addEmployee_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:transparent");

            salaryShowListData();

        }

    }


    @FXML
    private TableView<Reclamation> userReclamation;

    @FXML
    private TableColumn<Reclamation, Integer> idColumn;

    @FXML
    private TableColumn<Reclamation, String> titleColumn;

    @FXML
    private TableColumn<Reclamation, String> dateColumn;

    private ReclamationService reclamationService;

    public dashboardController() {
        // Initialize the ReclamationService
        reclamationService = new ReclamationService(); // You need to implement this class
    }

    public void initialize() throws SQLException {

        if(addEmployee_tableView!=null){
            addEmployee_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectUser();
                }
            });
        }
        UserService uc=new UserService();

        if(CurrentUser.getCurrentUser()==null){
            CurrentUser.setCurrentUser(uc.getById(113));

        }
        if (this.username != null) {
            username.setText(CurrentUser.getCurrentUser().getFirstName() +" "+CurrentUser.getCurrentUser().getLastName());

        }

//        dateColumn.setCellValueFactory(cellData -> {
//            ObservableValue<String> value = new ObservableValue<String>() {
//                @Override
//                public String getValue() {
//                    Reclamation reclamation = cellData.getValue();
//                    return reclamation != null ? reclamation.getDescription() : "";
//                }
//
//                @Override
//                public void addListener(ChangeListener<? super String> listener) {
//                    // Not needed for read-only property
//                }
//
//                @Override
//                public void removeListener(ChangeListener<? super String> listener) {
//                    // Not needed for read-only property
//                }
//
//                @Override
//                public void addListener(InvalidationListener listener) {
//                    // Not needed for read-only property
//                }
//
//                @Override
//                public void removeListener(InvalidationListener listener) {
//                    // Not needed for read-only property
//                }
//            };
//            return value;
//        });
//

        if (salary_tableView == null) {
            loadDate();

        }




    }

    private User getCurrentUser() {
        // Implement this method to get the current user
        return CurrentUser.getCurrentUser(); // Placeholder, replace with actual implementation
    }

    private void showAlert(String title, String content) {
        // Implement your alert method here
    }
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableColumn<Reclamation, String> editCol;
    public void switchToSceneOne() throws IOException {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser!=null){
            root = FXMLLoader.load(getClass().getResource("/reclamation/AddReclamation.fxml"));
            stage = (Stage) userReclamation.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }

    }
    public void backToprofil() throws IOException {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser!=null){
            root = FXMLLoader.load(getClass().getResource("/users/profil.fxml"));
            stage = (Stage) userReclamation.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }

    }

    ObservableList<Reclamation> reclamationList = FXCollections.observableArrayList();

    @FXML
    private void refreshTable() {



        try {
            reclamationList.clear();
            ReclamationService rs=new ReclamationService();
            List<Reclamation> rl= reclamationService.getAll();
            reclamationList.addAll(rl);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }



    }
    private void loadDate() {
        if (salary_form != null) {
            refreshTable();
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateReclamation"));

            // Fetch and display user's reclamations
            User currentUser = getCurrentUser(); // Implement this method to get the current user
            if (currentUser != null) {
                try {
                    List<Reclamation> userReclamations = reclamationService.getAll();
                    userReclamation.getItems().addAll(userReclamations);
                } catch (SQLException e) {
                    showAlert("Error", "An error occurred while fetching reclamations.");
                    e.printStackTrace();
                }
            } else {
                showAlert("Error", "Unable to determine current user.");
            }

            Callback<TableColumn<Reclamation, String>, TableCell<Reclamation, String>> cellFoctory = (TableColumn<Reclamation, String> param) -> {
                // make cell containing buttons
                final TableCell<Reclamation, String> cell = new TableCell<Reclamation, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {

                            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                            FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EYE);

                            deleteIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                            + "-glyph-size:28px;"
                                            + "-fx-fill:#ff1744;"
                            );
                            editIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                            + "-glyph-size:28px;"
                                            + "-fx-fill:#00E676;"
                            );
                            deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                                try {
                                    Reclamation reclamation = userReclamation.getSelectionModel().getSelectedItem();
                                    if (reclamation != null) {
                                        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete reclamation with ID " + reclamation.getId() + "?");

                                        if (choice == JOptionPane.YES_OPTION) {
                                            ReclamationService rs = new ReclamationService();
                                            rs.delete(reclamation.getId());
                                            JOptionPane.showMessageDialog(null, "Reclamation with ID " + reclamation.getId() + " deleted successfully.");
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Deletion cancelled.");
                                        }
                                        refreshTable();
                                    }
                                } catch (SQLException ex) {
                                    System.out.println(ex.getMessage());
                                }
                            });

                            editIcon.setOnMouseClicked((MouseEvent event) -> {
                                Reclamation reclamation = userReclamation.getSelectionModel().getSelectedItem();


                                if (reclamation != null) {
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("/reclamation/showReclamationDetals.fxml"));
                                    try {
                                        loader.load();
                                    } catch (IOException ex) {
                                        System.out.println(ex.getMessage());
                                    }
                                    ShowReclamationDetals addStudentController = loader.getController();

                                    addStudentController.setTitleField(reclamation.getTitle());
                                    addStudentController.setDescriptionArea(reclamation.getDescription());
                                    addStudentController.setDatereclamation(String.valueOf(reclamation.getDateReclamation()));

                                    Parent parent = loader.getRoot();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(parent));
                                    stage.initStyle(StageStyle.UTILITY);
                                    stage.show();

                                }
                            });

                            HBox managebtn = new HBox(editIcon, deleteIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                            HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                            setGraphic(managebtn);

                        }
                        setText(null);
                    }

                };

                return cell;
            };

            editCol.setCellFactory(cellFoctory);
            userReclamation.setItems(reclamationList);



            //add cell of button edit

        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    private void showDetails(Reclamation request) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'utilisateur");
        alert.setHeaderText("Détails de la demande sélectionnée");

        alert.setContentText("ID : " + request.getId() + "\n" +
                "Title : " + request.getTitle() + "\n" +
                "Date : " + request.getDateReclamation() + "\n"+
                "Description : " + request.getDescription() + "\n"
        );

        alert.showAndWait();
    }




    private double x = 0;
    private double y = 0;

    public void logout() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {

                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/users/login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        System.exit(0);
    }
    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        defaultNav();
        try {
            initialize();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        homeTotalEmployees();


        try {
            addEmployeeShowListData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private List<Reclamation> reclamations;
    @FXML
    private ComboBox<String> filterOrderByDate;
    private ObservableList<Reclamation> masterData = FXCollections.observableArrayList();

    ObservableList<Reclamation> filteredData = FXCollections.observableArrayList();

    private void initializeSortComboBox() {
        filterOrderByDate.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleFilterChangeOrderByDate(ActionEvent event) {
        System.out.println("enter fn");
        String selectedOrder = filterOrderByDate.getValue();
        if (selectedOrder != null) {
            try {
                List<Reclamation> filteredProperties = reclamationService.getAll();
                System.out.println(selectedOrder);
                if (selectedOrder.equals("asc")) {
                    filteredProperties.sort(Comparator.comparing(Reclamation::getDateReclamation));
                } else if (selectedOrder.equals("desc")) {
                    filteredProperties.sort(Comparator.comparing(Reclamation::getDateReclamation).reversed());
                }
                userReclamation.getItems().setAll(filteredProperties);
            } catch (SQLException e) {

            }
        }
    }
}