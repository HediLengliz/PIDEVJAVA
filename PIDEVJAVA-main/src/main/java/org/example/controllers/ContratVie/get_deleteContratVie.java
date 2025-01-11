package org.example.controllers.ContratVie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.example.models.ContratVie;
import org.example.services.ContratVieService;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class get_deleteContratVie implements Initializable {

    @FXML
    private Label contratVie;

    @FXML
    private TableView<ContratVie> contratVieTable;
    @FXML
    private final ContratVieService CV = new ContratVieService();


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<ContratVie> contratVies = CV.getAll();
            ObservableList<ContratVie> observableContratVies = FXCollections.observableArrayList(contratVies);
            contratVieTable.setItems(observableContratVies);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

