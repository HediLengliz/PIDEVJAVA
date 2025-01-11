package org.example.controllers.Reclamation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.User;
import org.example.utils.CurrentUser;

import java.io.IOException;

public class ShowReclamationDetals {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField datereclamation;

    public TextField getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField.setText(titleField);
    }
    public void setDatereclamation(String titleField) {
        this.datereclamation.setText(titleField);
    }

    public TextArea getDescriptionArea() {
        return descriptionArea;
    }

    public void setDescriptionArea(String descriptionArea) {
        this.descriptionArea.setText(descriptionArea);
    }

    public void switchToSceneOne() throws IOException {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser != null) {


            root = FXMLLoader.load(getClass().getResource("/reclamation/userReclamation.fxml"));
            stage = (Stage) descriptionArea.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }


    }
}
