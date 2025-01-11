package org.example.controllers.rapport;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.models.Rapport;
import org.example.services.RapportService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class GetAllRapportController  implements Initializable {

    private final RapportService rapportService = new RapportService();

    @FXML
    private TableView<Rapport> rapportTableView;

    @FXML
    private TableColumn<Rapport, Void> actionColumn;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Callback<TableColumn<Rapport, Void>, TableCell<Rapport, Void>> getButtonCellFactory() {
        return new Callback<TableColumn<Rapport, Void>, TableCell<Rapport, Void>>() {
            @Override
            public TableCell<Rapport, Void> call(final TableColumn<Rapport, Void> param) {
                return new TableCell<Rapport, Void>() {
                    private final Button viewButton = new Button("View Details");


                    private final Button exportButton = new Button("Export PDF");

                    {
                        viewButton.setOnAction(event -> {
                            viewButton.setStyle("-fx-background-color: #e7e7e7;-fx-text-fill: black");
                            Rapport rowData = getTableView().getItems().get(getIndex());
                            try {
                                switchToSceneWithData(event, rowData.getId().intValue());
                            } catch (IOException | SQLException e) {
                                e.printStackTrace();
                            }
                        });

                        exportButton.setOnAction(event -> {
                            exportButton.setStyle("-fx-background-color: #d6e0d9;-fx-text-fill: black");
                            Rapport rowData = getTableView().getItems().get(getIndex());
                            exportPrescriptionToPDF(rowData);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            viewButton.setStyle("-fx-background-color: #e7e7e7;-fx-text-fill: black");
                            exportButton.setStyle("-fx-background-color: #d6e0d9;-fx-text-fill: black");
                            HBox buttonsContainer = new HBox(10, viewButton, exportButton); // Use HBox for layout
                            setGraphic(buttonsContainer);
                        }
                    }
                };
            }
        };
    }

    public void exportPrescriptionToPDF(Rapport prescription) {
        if (prescription != null) {
            String path = "C:\\Users\\yassmine\\Downloads\\sinisterProperty" + prescription.getId() + ".pdf";

            try {
                PdfWriter writer = new PdfWriter(path);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Rapport Details"));
                document.add(new Paragraph("Decision: " + prescription.getDecision()));
                document.add(new Paragraph("Justification: " + prescription.getJustification()));
                document.add(new Paragraph("Protechtini\n18, rue de l'Usine\nZI Aéroport Charguia\nII 2035 Ariana\nPhone: (+216) 58 26 64 36\nEmail: protechtini.synthcode@esprit.tn"));
                document.close();

                System.out.println("PDF exported successfully to: " + path);
                showAlert(Alert.AlertType.INFORMATION, "SUCCESS", "PDF downloaded successfully");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.err.println("An error occurred while exporting the PDF.");
            }
        } else {
            System.out.println("No Prescription selected.");
        }
    }
    @FXML
    public void switchToSceneWithData(ActionEvent event, int id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/rapport/sinisterRapportDetails.fxml"));
        Parent root = loader.load();

        SinisterReportDetailsController controller = loader.getController();
        controller.setReportDetails(id);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Sinister Report Details");
        popupStage.setScene(new Scene(root));
        popupStage.showAndWait();
    }
//    @FXML
//    public void switchToSceneWithData(ActionEvent event,int id) throws IOException, SQLException {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/rapport/sinisterRapportDetails.fxml"));
//        root = loader.load();
//
//        SinisterReportDetailsController controller = loader.getController();
//        controller.setReportDetails(id);
//
//
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            List<Rapport> rapports = rapportService.getAll();
            rapportTableView.getItems().setAll(rapports);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the error
        }
        actionColumn.setCellFactory(getButtonCellFactory());
    }

//    private Callback<TableColumn<Rapport, Void>, TableCell<Rapport, Void>> getButtonCellFactory() {
//        return new Callback<TableColumn<Rapport, Void>, TableCell<Rapport, Void>>() {
//            @Override
//            public TableCell<Rapport, Void> call(final TableColumn<Rapport, Void> param) {
//                return new TableCell<Rapport, Void>() {
//                    private final Button viewButton = new Button("View Details");
//                    private final Button downloadButton = new Button("download");
//                    {
//                        viewButton.setOnAction(event -> {
//                            Rapport rowData = getTableView().getItems().get(getIndex());
//
//                            try {
//                                switchToSceneWithData(event, rowData.getId().intValue());
//                            } catch (IOException | SQLException e) {
//                                e.printStackTrace();
//                            }
//                        });
//                        downloadButton.setOnAction(event -> {
//                            Rapport rowData = getTableView().getItems().get(getIndex());
//
//                            try {
//                                switchToSceneWithData(event, rowData.getId().intValue());
//
//                            } catch (IOException | SQLException e) {
//                                e.printStackTrace();
//                            }
//                        });
//                    }
//
//                    @Override
//                    protected void updateItem(Void item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                        } else {
//                            setGraphic(viewButton);
//                        }
//                    }
//                };
//            }
//        };
//    }


//    @FXML
//    private void handleViewReport(Rapport rapport) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rapport/sinisterRapportDetails.fxml"));
//            Parent root = loader.load();
//
//            // Get the controller
//            SinisterReportDetailsController controller = loader.getController();
//
//            // Initialize the controller with the selected report
//            controller.setReportDetails(rapport.getId());
//
//            // Create a new stage and set the scene
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.setTitle("Rapport Details");
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
