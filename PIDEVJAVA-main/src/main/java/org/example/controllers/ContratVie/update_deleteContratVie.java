package org.example.controllers.ContratVie;

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
import org.example.models.ContratVie;

import org.example.services.ContratVieService;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class update_deleteContratVie {


    @FXML
    private TableColumn<ContratVie, Integer> idContrat;

    @FXML
    private TableColumn<ContratVie, String> dateDebutColum;

    @FXML
    private TableColumn<ContratVie, String> dateFinColumn;

    @FXML
    private TableColumn<ContratVie, String> descriptionColumn;
    @FXML
    private TableColumn<ContratVie, String> matriculeAgentColumn;
    @FXML
    private TableColumn<ContratVie, String> prixColumn;

    @FXML
    private TableView<ContratVie> ContratVieTable;


    private ContratVieService ContratVieService = new ContratVieService();



    public void initialize() {
        try {
            List<ContratVie> ContratVies = ContratVieService.getAll();
            System.out.println(ContratVies);
            ObservableList<ContratVie> observableContratVies = FXCollections.observableArrayList(ContratVies);
            ContratVieTable.setItems(observableContratVies);
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
    private TableColumn<ContratVie, String> editCol;



    ObservableList<ContratVie> ContratVieObservableList = FXCollections.observableArrayList();

    @FXML
    private void refreshTable() {



        try {
            ContratVieObservableList.clear();
            ContratVie rs=new ContratVie();
            List<ContratVie> rl= ContratVieService.getAll();
            ContratVieObservableList.addAll(rl);

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



        // Fetch and display user's ContratVies

        Callback<TableColumn<ContratVie, String>, TableCell<ContratVie, String>> cellFoctory = (TableColumn<ContratVie, String> param) -> {
            // make cell containing buttons
            final TableCell<ContratVie, String> cell = new TableCell<ContratVie, String>() {
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
                        pdfIcon.setFill(Color.web("#ff1744")); // Rouge

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
                                        + "-fx-fill:#00E676;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            ContratVie rowData = getTableView().getItems().get(getIndex());
                            if (rowData instanceof ContratVie) {
                                int ContratVieId = ((ContratVie) rowData).getId();
                                System.out.println("Delete clicked for vehicle request ID: " + ContratVieId);
                                ContratVieService ContratVieService = new ContratVieService();
                                try {
                                    ContratVieService.delete(ContratVieId);
                                    showAlert(Alert.AlertType.INFORMATION, "Success", "User Deleted", "User deleted successfully.");

                                    ObservableList<ContratVie> ContratVieList = (ObservableList<ContratVie>) getTableView().getItems();
                                    ContratVieList.removeIf(user -> user.getId() == ContratVieId);
                                } catch (SQLException e) {
                                    showAlert(Alert.AlertType.ERROR, "Error", "Delete Failed", "Error deleting user: " + e.getMessage());
                                }
                            }
                        });

                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            ContratVie rowData = getTableView().getItems().get(getIndex());

                            ContratVie selectedContratVie = (ContratVie) rowData;
                            if (selectedContratVie != null) {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ContratVie/update_ContratVie.fxml"));

                                    Parent root = loader.load();
                                    System.out.print("hgj");



                                    Stage stage = new Stage();
                                    stage.initModality(Modality.APPLICATION_MODAL); // Ensure the popup is modal
                                    stage.setTitle("Update contrat vie");
                                    update_ContratVie controller = loader.getController();
                                    controller.initData(selectedContratVie); // Pass the ID of the selected user
                                    stage.setScene(new Scene(root));
                                    stage.showAndWait(); // Show the popup and wait for it to close
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            refreshTable();
                        });

                        pdfIcon.setOnMouseClicked((MouseEvent event) -> {
                            ContratVie rowData = getTableView().getItems().get(getIndex());

                            ContratVie selectedContratVie = (ContratVie) rowData;
                            if (selectedContratVie != null) {
                                String path = "C:\\Users\\21652\\Downloads\\ContratVie_" + selectedContratVie.getId() + ".pdf";

                                try {
                                    PdfWriter writer = new PdfWriter(path);
                                    PdfDocument pdf = new PdfDocument(writer);
                                    Document document = new Document(pdf);

                                    document.add(new Paragraph("Contrat Details"));
                                    document.add(new Paragraph("Date Debut: " + selectedContratVie.getDateDebut().toString()));
                                    document.add(new Paragraph("Date Fin: " + selectedContratVie.getDateFin().toString()));
                                    document.add(new Paragraph("Description du contrat: " + selectedContratVie.getDescription()));
                                    document.add(new Paragraph("Montant à payer: " + selectedContratVie.getPrix()));

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
        ContratVieTable.setItems(ContratVieObservableList);



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

                ContratVieTable.setItems(ContratVieObservableList);
                refreshTable();
                return;
            }

            FilteredList<ContratVie> filteredList = new FilteredList<>(ContratVieObservableList, contratVie ->
                    String.valueOf(contratVie.getId()).contains(searchKey)
                            || contratVie.getMatriculeAgent().toLowerCase().contains(searchKey)
                            || contratVie.getDateDebut().toString().toLowerCase().contains(searchKey)
                            || contratVie.getDateFin().toString().toLowerCase().contains(searchKey)
                            || contratVie.getDescription().toLowerCase().contains(searchKey)
                            || contratVie.getPrix().toLowerCase().contains(searchKey)
            );

            SortedList<ContratVie> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(ContratVieTable.comparatorProperty());

            ContratVieTable.setItems(sortedList);
        });
    }




}