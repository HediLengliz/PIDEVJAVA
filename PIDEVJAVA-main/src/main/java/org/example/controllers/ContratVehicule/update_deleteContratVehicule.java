package org.example.controllers.ContratVehicule;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.models.ContratVehicule;

import org.example.services.ContratVehiculeService;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class update_deleteContratVehicule {


    @FXML
    private TableColumn<ContratVehicule, Integer> idContrat;

    @FXML
    private TableColumn<ContratVehicule, String> dateDebutColum;

    @FXML
    private TableColumn<ContratVehicule, String> dateFinColumn;

    @FXML
    private TableColumn<ContratVehicule, String> descriptionColumn;
    @FXML
    private TableColumn<ContratVehicule, String> matriculeAgentColumn;
    @FXML
    private TableColumn<ContratVehicule, String> prixColumn;

    @FXML
    private TableView<ContratVehicule> ContratVehiculeTable;


    private ContratVehiculeService ContratVehiculeService = new ContratVehiculeService();



    public void initialize() {
        try {
            List<ContratVehicule> ContratVehicules = ContratVehiculeService.getAll();
            System.out.println(ContratVehicules);
            ObservableList<ContratVehicule> observableContratVehicules = FXCollections.observableArrayList(ContratVehicules);
            ContratVehiculeTable.setItems(observableContratVehicules);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        loadDate();
    }



    private void showAlert(String title, String content) {
        // Implement your alert method here
    }
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableColumn<ContratVehicule, String> editCol;



    ObservableList<ContratVehicule> ContratVehiculeObservableList = FXCollections.observableArrayList();

    @FXML
    private void refreshTable() {



        try {
            ContratVehiculeObservableList.clear();
            ContratVehicule rs=new ContratVehicule();
            List<ContratVehicule> rl= ContratVehiculeService.getAll();
            ContratVehiculeObservableList.addAll(rl);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }



    }
    private void loadDate() {

        refreshTable();
        idContrat.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateDebutColum.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        matriculeAgentColumn.setCellValueFactory(new PropertyValueFactory<>("matriculeAgent"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));



        // Fetch and display user's ContratVehicules

        Callback<TableColumn<ContratVehicule, String>, TableCell<ContratVehicule, String>> cellFoctory = (TableColumn<ContratVehicule, String> param) -> {
            // make cell containing buttons
            final TableCell<ContratVehicule, String> cell = new TableCell<ContratVehicule, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
                        FontAwesomeIconView pdfIcon = new FontAwesomeIconView(FontAwesomeIcon.FILE_PDF_ALT);

                        editIcon.setFill(Color.web("#5462ff")); // Vert
                        deleteIcon.setFill(Color.web("#ff1744")); // Rouge
                        deleteIcon.setFill(Color.web("#ff1744")); // Rouge

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
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );
                        pdfIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            ContratVehicule rowData = getTableView().getItems().get(getIndex());
                            if (rowData instanceof ContratVehicule) {
                                int ContratVehiculeId = ((ContratVehicule) rowData).getId();
                                System.out.println("Delete clicked for vehicle request ID: " + ContratVehiculeId);
                                ContratVehiculeService ContratVehiculeService = new ContratVehiculeService();
                                try {
                                    ContratVehiculeService.delete(ContratVehiculeId);
                                    showAlert(Alert.AlertType.INFORMATION, "Success", "User Deleted", "User deleted successfully.");

                                    ObservableList<ContratVehicule> ContratVehiculeList = (ObservableList<ContratVehicule>) getTableView().getItems();
                                    ContratVehiculeList.removeIf(user -> user.getId() == ContratVehiculeId);
                                } catch (SQLException e) {
                                    showAlert(Alert.AlertType.ERROR, "Error", "Delete Failed", "Error deleting user: " + e.getMessage());
                                }
                            }
                        });

                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            ContratVehicule rowData = getTableView().getItems().get(getIndex());

                            ContratVehicule selectedContratVehicule = (ContratVehicule) rowData;
                            if (selectedContratVehicule != null) {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ContratVehicule/update_ContratVehicule.fxml"));

                                    Parent root = loader.load();
                                    System.out.print("hgj");

                                    Stage stage = new Stage();
                                    stage.initModality(Modality.APPLICATION_MODAL); // Ensure the popup is modal
                                    stage.setTitle("Update contrat vehicule");
                                    update_ContratVehicule controller = loader.getController();
                                    controller.initData(selectedContratVehicule);
                                    stage.setScene(new Scene(root));
                                    stage.showAndWait(); // Show the popup and wait for it to close
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            refreshTable();
                        });

                        pdfIcon.setOnMouseClicked((MouseEvent event) -> {
                            ContratVehicule rowData = getTableView().getItems().get(getIndex());

                            ContratVehicule selectedContratVehicule = (ContratVehicule) rowData;
                            if (selectedContratVehicule != null) {
                                String path = "C:\\Users\\21652\\Downloads\\ContratVehicule_" + selectedContratVehicule.getId() + ".pdf";

                                try {
                                    PdfWriter writer = new PdfWriter(path);
                                    PdfDocument pdf = new PdfDocument(writer);
                                    Document document = new Document(pdf);

                                    document.add(new Paragraph("Contrat Details"));
                                    document.add(new Paragraph("Date Debut: " + selectedContratVehicule.getDateDebut().toString()));
                                    document.add(new Paragraph("Date Fin: " + selectedContratVehicule.getDateFin().toString()));
                                    document.add(new Paragraph("Description du contrat: " + selectedContratVehicule.getDescription()));
                                    document.add(new Paragraph("Montant à payer: " + selectedContratVehicule.getPrix()));

                                    document.add(new Paragraph("\n" +
                                            "\n" +
                                            "\n" +
                                            "\n" +
                                            "\n" ));
                                    document.add(new Paragraph("Protechtini\n" +
                                            "18, rue de l'Usine\n" +
                                            "ZI Aéroport Charguia\n" +
                                            "II 2035 Ariana\n" +
                                            "Phone: (+216) 58 26 64 36\n" +
                                            "Email: protechtini.synthcode@esprit.tn"));
                                    document.close();

                                    System.out.println("PDF exported successfully to: " + path);

                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                System.out.println("No Prescription selected.");
                            }

                        });



                        HBox managebtn = new HBox(editIcon, deleteIcon, pdfIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                        HBox.setMargin(pdfIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                    }
                    setText(null);
                }

            };

            return cell;
        };

        editCol.setCellFactory(cellFoctory);
        ContratVehiculeTable.setItems(ContratVehiculeObservableList);



        //add cell of button edit

    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    private TextField keywordTextField;

    public void addEmployeeSearch() {
        keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchKey = newValue.trim().toLowerCase(); // Trim and convert to lowercase

            if (searchKey.isEmpty()) {

                    ContratVehiculeTable.setItems(ContratVehiculeObservableList);
                refreshTable();
                return;
            }

            FilteredList<ContratVehicule> filteredList = new FilteredList<>(ContratVehiculeObservableList, contratVehicule ->
                    String.valueOf(contratVehicule.getId()).contains(searchKey)
                            || contratVehicule.getMatriculeAgent().toLowerCase().contains(searchKey)
                            || contratVehicule.getDateDebut().toString().toLowerCase().contains(searchKey)
                            || contratVehicule.getDateFin().toString().toLowerCase().contains(searchKey)
                            || contratVehicule.getDescription().toLowerCase().contains(searchKey)
                            || contratVehicule.getPrix().toLowerCase().contains(searchKey)
            );

            SortedList<ContratVehicule> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(ContratVehiculeTable.comparatorProperty());

            ContratVehiculeTable.setItems(sortedList);
        });
    }





}