package org.example.controllers.ContratHabitat;

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
import org.example.models.ContratHabitat;

import org.example.services.ContratHabitatService;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class update_deleteContratHabitat {


    @FXML
    private TableColumn<ContratHabitat, Integer> idContrat;

    @FXML
    private TableColumn<ContratHabitat, String> dateDebutColum;

    @FXML
    private TableColumn<ContratHabitat, String> dateFinColumn;

    @FXML
    private TableColumn<ContratHabitat, String> descriptionColumn;
    @FXML
    private TableColumn<ContratHabitat, String> matriculeAgentColumn;
    @FXML
    private TableColumn<ContratHabitat, String> prixColumn;

    @FXML
    private TableView<ContratHabitat> ContratHabitatTable;


    private ContratHabitatService ContratHabitatService = new ContratHabitatService();



    public void initialize() {
        try {
            List<ContratHabitat> ContratHabitats = ContratHabitatService.getAll();
            System.out.println(ContratHabitats);
            ObservableList<ContratHabitat> observableContratHabitats = FXCollections.observableArrayList(ContratHabitats);
            ContratHabitatTable.setItems(observableContratHabitats);
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
    private TableColumn<ContratHabitat, String> editCol;



    ObservableList<ContratHabitat> ContratHabitatObservableList = FXCollections.observableArrayList();

    @FXML
    private void refreshTable() {



        try {
            ContratHabitatObservableList.clear();
            ContratHabitat rs=new ContratHabitat();
            List<ContratHabitat> rl= ContratHabitatService.getAll();
            ContratHabitatObservableList.addAll(rl);

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



        // Fetch and display user's ContratHabitats

        Callback<TableColumn<ContratHabitat, String>, TableCell<ContratHabitat, String>> cellFoctory = (TableColumn<ContratHabitat, String> param) -> {
            // make cell containing buttons
            final TableCell<ContratHabitat, String> cell = new TableCell<ContratHabitat, String>() {
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
                        pdfIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            ContratHabitat rowData = getTableView().getItems().get(getIndex());
                            if (rowData instanceof ContratHabitat) {
                                int ContratHabitatId = ((ContratHabitat) rowData).getId();
                                System.out.println("Delete clicked for vehicle request ID: " + ContratHabitatId);
                                ContratHabitatService ContratHabitatService = new ContratHabitatService();
                                try {
                                    ContratHabitatService.delete(ContratHabitatId);
                                    showAlert(Alert.AlertType.INFORMATION, "Success", "User Deleted", "User deleted successfully.");

                                    ObservableList<ContratHabitat> ContratHabitatList = (ObservableList<ContratHabitat>) getTableView().getItems();
                                    ContratHabitatList.removeIf(user -> user.getId() == ContratHabitatId);
                                } catch (SQLException e) {
                                    showAlert(Alert.AlertType.ERROR, "Error", "Delete Failed", "Error deleting user: " + e.getMessage());
                                }
                            }
                        });

                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            ContratHabitat rowData = getTableView().getItems().get(getIndex());

                            ContratHabitat selectedContratHabitat = (ContratHabitat) rowData;
                            if (selectedContratHabitat != null) {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ContratHabitat/update_ContratHabitat.fxml"));

                                    Parent root = loader.load();
                                    System.out.print("hgj");


                                    Stage stage = new Stage();
                                    stage.initModality(Modality.APPLICATION_MODAL); // Ensure the popup is modal
                                    stage.setTitle("Update contrat habitat");
                                    update_ContratHabitat controller = loader.getController();
                                    controller.initData(selectedContratHabitat); // Pass the ID of the selected user
                                    stage.setScene(new Scene(root));
                                    stage.showAndWait(); // Show the popup and wait for it to close
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            refreshTable();
                        });

                        pdfIcon.setOnMouseClicked((MouseEvent event) -> {
                            ContratHabitat rowData = getTableView().getItems().get(getIndex());

                            ContratHabitat selectedContratHabitat = (ContratHabitat) rowData;
                            if (selectedContratHabitat != null) {
                                String path = "C:\\Users\\21652\\Downloads\\ContratHabitat_" + selectedContratHabitat.getId() + ".pdf";

                                try {
                                    PdfWriter writer = new PdfWriter(path);
                                    PdfDocument pdf = new PdfDocument(writer);
                                    Document document = new Document(pdf);

                                    document.add(new Paragraph("Contrat Details"));
                                    document.add(new Paragraph("Date Debut: " + selectedContratHabitat.getDateDebut().toString()));
                                    document.add(new Paragraph("Date Fin: " + selectedContratHabitat.getDateFin().toString()));
                                    document.add(new Paragraph("Description du contrat: " + selectedContratHabitat.getDescription()));
                                    document.add(new Paragraph("Montant à payer: " + selectedContratHabitat.getPrix()));

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
        ContratHabitatTable.setItems(ContratHabitatObservableList);



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

                ContratHabitatTable.setItems(ContratHabitatObservableList);
                refreshTable();
                return;
            }

            FilteredList<ContratHabitat> filteredList = new FilteredList<>(ContratHabitatObservableList, contratHabitat ->
                    String.valueOf(contratHabitat.getId()).contains(searchKey)
                            || contratHabitat.getMatriculeAgent().toLowerCase().contains(searchKey)
                            || contratHabitat.getDateDebut().toString().toLowerCase().contains(searchKey)
                            || contratHabitat.getDateFin().toString().toLowerCase().contains(searchKey)
                            || contratHabitat.getDescription().toLowerCase().contains(searchKey)
                            || contratHabitat.getPrix().toLowerCase().contains(searchKey)
                          );

            SortedList<ContratHabitat> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(ContratHabitatTable.comparatorProperty());

            ContratHabitatTable.setItems(sortedList);
        });
    }



}