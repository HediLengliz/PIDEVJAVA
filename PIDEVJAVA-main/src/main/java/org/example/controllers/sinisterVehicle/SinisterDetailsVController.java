
package org.example.controllers.sinisterVehicle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.models.SinisterVehicle;
import org.example.services.SinisterVehicleService;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

    public class SinisterDetailsVController {

        private SinisterVehicleService sinisterVehicleService = new SinisterVehicleService();

        @FXML
        private TextField textFieldLocation1;

        @FXML
        private TextField textFieldLocation;

        @FXML
        private TextField textFieldStatus;

        @FXML
        private TextField textFieldNomConducteurA;

        @FXML
        private TextField textFieldNomConducteurB;

        @FXML
        private TextField textFieldPrenomConducteurA;

        @FXML
        private TextField textFieldPrenomConducteurB;

        @FXML
        private TextField textFieldAdresseConducteurA;

        @FXML
        private TextField textFieldAdresseConducteurB;

        @FXML
        private TextField textFieldNumPermisA;

        @FXML
        private TextField textFieldNumPermisB;

        @FXML
        private TextField textFieldDelivreA;

        @FXML
        private TextField textFieldDelivreB;

        @FXML
        private TextField textFieldNumContratA;

        @FXML
        private TextField textFieldNumContratB;

        @FXML
        private TextField textFieldMarqueVehiculeA;

        @FXML
        private TextField textFieldMarqueVehiculeB;

        @FXML
        private TextField textFieldImmatriculationA;

        @FXML
        private TextField textFieldImmatriculationB;

        @FXML
        private TextField textFieldBvehiculeAssurePar;

        @FXML
        private TextField textFieldAgence;

        @FXML
        private ImageView sinisterImageView;
        @FXML
        private Stage stage;
        public void setSinisterDetails(int id) throws SQLException {
            SinisterVehicle sinisterVehicle = sinisterVehicleService.getById(id);
            if (sinisterVehicle != null) {
                textFieldLocation1.setText(sinisterVehicle.getDate_sinister().toString());
                textFieldLocation.setText(sinisterVehicle.getLocation());
                textFieldStatus.setText(sinisterVehicle.getStatus_sinister());
                textFieldNomConducteurA.setText(sinisterVehicle.getNom_conducteur_a());
                textFieldNomConducteurB.setText(sinisterVehicle.getNom_conducteur_b());
                textFieldPrenomConducteurA.setText(sinisterVehicle.getPrenom_conducteur_a());
                textFieldPrenomConducteurB.setText(sinisterVehicle.getPrenom_conducteur_b());
                textFieldAdresseConducteurA.setText(sinisterVehicle.getAdresse_conducteur_a());
                textFieldAdresseConducteurB.setText(sinisterVehicle.getAdresse_conducteur_b());
                textFieldNumPermisA.setText(sinisterVehicle.getNum_permis_a());
                textFieldNumPermisB.setText(sinisterVehicle.getNum_permis_b());
                textFieldDelivreA.setText(sinisterVehicle.getDelivre_a().toString());
                textFieldDelivreB.setText(sinisterVehicle.getDelivre_b().toString());

                textFieldMarqueVehiculeA.setText(sinisterVehicle.getMarque_vehicule_a());
                textFieldMarqueVehiculeB.setText(sinisterVehicle.getMarque_vehicule_b());
                textFieldImmatriculationA.setText(sinisterVehicle.getImmatriculation_a());
                textFieldImmatriculationB.setText(sinisterVehicle.getImmatriculation_b());
                textFieldBvehiculeAssurePar.setText(sinisterVehicle.getBvehicule_assure_par());
                textFieldAgence.setText(sinisterVehicle.getAgence());
                String imageName = sinisterVehicle.getImage_name();
                System.out.println(imageName+"eeeeeeeeeeee");
                if (imageName != null && !imageName.isEmpty()) {
                    try {
                        String imagePath = "images/profiles/" + imageName;
                        Image image = new Image(new FileInputStream(imagePath));
                        sinisterImageView.setImage(image);
                    } catch (FileNotFoundException e) {
                        System.out.println("Image file not found: " + e.getMessage());
                        // Handle exception if image file is not found
                    }
                } else {
                    // Handle case where image name is null or empty
                    System.out.println("Image name is null or empty.");
                }
            } else {
                System.out.println("No data found for the specified id.");
            }
        }





    @FXML
    private void goBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/dashboard.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        // This method is called automatically after the FXML file has been loaded
    }

}
