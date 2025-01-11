package org.example.controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.util.Comparator;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.models.Commentaire;

import org.example.services.CommentServices;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import java.util.List;
import java.util.Locale;

public class listcomment {
    @FXML
    public ComboBox<Commentaire> sortComboBox = new ComboBox<>();
    @FXML
    private TableView<Commentaire> commentTableView = new TableView<>();
    @FXML
    private TableColumn<Commentaire, String> descriptionColumn = new TableColumn<>();

    @FXML
    private TableColumn<Commentaire, String> idColumn = new TableColumn<>();

    @FXML
    private TableColumn<Commentaire, String> ratingColumn = new TableColumn<>();
    private final ObservableList<Commentaire> comment = FXCollections.observableArrayList();
    @FXML
    private TextField search;
    private FilteredList<Commentaire> filteredData;
    CommentServices commentservices = new CommentServices();


    private final ObservableList<Commentaire> commentaireObservableList = FXCollections.observableArrayList();


    @FXML
    public void initialize() throws Exception {
        sortComboBox.getItems().addAll(
                new Commentaire("Description (ASC)"),
                new Commentaire("Description (DESC)"),
                new Commentaire("Rate (ASC)"),
                new Commentaire("Rate (DESC)"));
        sortComboBox.getSelectionModel().selectFirst();
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection!= null) {
                sortTableView(newSelection.getDescription(), newSelection.getRate());
            }
        });
        comment.clear();

        List<Commentaire> commentaireList = null;
        sortTableView("description", true);

        commentaireList = commentservices.getAll();
        // Get all comments from the database
        comment.addAll(commentaireList);
        commentTableView.getItems().addAll(commentaireList);
        filteredData = new FilteredList<>(comment, p -> true);
        commentTableView.setItems(comment);
        commentTableView.setItems(filteredData);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        //load comments
        Search();

    }

    private void sortTableView(String description, int rate) {
        Comparator<Commentaire> comparator = null;
        switch (description) {
            case "description":
                comparator = Comparator.comparing(Commentaire::getDescription);
                break;
            case "rate":
                comparator = Comparator.comparing(Commentaire::getRate);
                break;
            default:
                break;
        }

        if (rate == 0) {
            comparator = comparator.reversed();
        }

        commentTableView.getItems().sort(comparator);

    }

    public listcomment() throws SQLException {

        List<Commentaire> comments = commentservices.getAll();
        commentTableView.getItems().addAll(comments);
        //set cell value factory to table view
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        commentTableView.getItems().setAll(comments);
        commentTableView.getSelectionModel().clearSelection();
        commentTableView.getItems().clear();
        commentTableView.getItems().addAll(comments);
        commentTableView.refresh();

    }

    @FXML
    void PDF(ActionEvent event) throws IOException {
        if (commentTableView != null && commentTableView.getSelectionModel().getSelectedItem() != null) {
            Commentaire comments = commentTableView.getSelectionModel().getSelectedItem();
            if (comments != null) {
                String path = "D:\\java\\PIDEVJAVA\\commentsPDF\\" + comments.getDescription() + ".pdf";
                exportToPDF(comments, path);

                try {
                    PdfWriter writer = new PdfWriter(path);
                    PdfDocument pdf = new PdfDocument(writer);
                    Document document = new Document(pdf);

                    document.add(new Paragraph("Comment Details"));
                    document.add(new Paragraph("Comment ID: " + comments.getId()));
                    document.add(new Paragraph("Description: " + comments.getDescription()));
                    document.add(new Paragraph("Rating: " + comments.getRate() + ("stars")));
                    document.add(new Paragraph("Article ID: " + comments.getArticle_id()));
                    document.add(new Paragraph("\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "\n"));
                    document.add(new Paragraph("Protechtini\n" +
                            "18, rue de l'Usine\n" +
                            "ZI Aéroport Charguia\n" +
                            "II 2035 Ariana\n" +
                            "Phone: (+216) 58 26 64 36\n" +
                            "Email: protechtini.synthcode@esprit.tn"));
                    document.close();

                    System.out.println("PDF exported successfully to: " + path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.err.println("An error occurred while exporting the PDF.");
                }
            } else {
                System.out.println("No Comment selected.");
            }
        }


    }

    private void exportToPDF(Commentaire comments, String path) {
        try {
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Comment Details"));
            document.add(new Paragraph("ID: " + comments.getId()));
            document.add(new Paragraph("Description: " + comments.getDescription()));
            document.add(new Paragraph("Rate: " + comments.getRate()));
            document.add(new Paragraph("Article ID: " + comments.getArticle_id()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("An error occurred while exporting the PDF.");
        }
    }

    @FXML
    private void Refresh() throws SQLException {
        comment.clear();
        List<Commentaire> commentaireList = commentservices.getAll();
        // Get all comments from the database
        comment.addAll(commentaireList);
        commentTableView.getItems().addAll(commentaireList);
        commentTableView.refresh();


    }

    @FXML
    void addcomment(ActionEvent event) throws SQLException {
        try {
            ObservableList<Commentaire> allcomments = commentTableView.getItems();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addcomment.fxml"));
            //parent root loader
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Comment");
            stage.showAndWait();
            Refresh();
            commentTableView.setItems(allcomments);
            stage.close();
            System.out.println("Comment added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deletecomment(javafx.event.ActionEvent event) {
        Commentaire commentaire = commentTableView.getSelectionModel().getSelectedItem();
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Comment");
            alert.setHeaderText("Are you sure you want to delete this comment?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                try {
                    ObservableList<Commentaire> allcomments = commentTableView.getItems();
                    commentservices.delete(commentaire.getId());
                    commentTableView.setItems(allcomments);
                    Refresh();
                    System.out.println("Comment deleted successfully!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @FXML
    private void Search() {
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(comment -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (comment.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(comment.getRate()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
    }

    private boolean containsIgnoreCase(String text, String searchTerm) {
        return text.toLowerCase(Locale.ROOT).contains(searchTerm);
    }

    @FXML
    private void updatecomment() throws IOException {
        Commentaire commentaire = commentTableView.getSelectionModel().getSelectedItem();
        if (commentaire != null) {
            try {
                ObservableList<Commentaire> allcomments = commentTableView.getItems();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatecomment.fxml"));
                //parent root loader
                Parent root = loader.load();
                updatecomment controller = loader.getController();
                controller.setCommentToUpdate(commentaire);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Update Comment");
                stage.showAndWait();
                commentTableView.setItems(allcomments);
                Refresh();
                System.out.println("Comment updated successfully!");
            } catch (IOException e) {
                e.printStackTrace();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No Comment selected.");
        }

    }



    public void ComboBox(ActionEvent actionEvent) {
        Commentaire selectedSortOption = sortComboBox.getValue();
        if (selectedSortOption != null) {
            System.out.println("aaaa");
            if (selectedSortOption.equals("Description (ASC)")) {
                sortTableView("description", true);
            } else if (selectedSortOption.equals("Description (DESC)")) {
                sortTableView("description", false);
            } else if (selectedSortOption.equals("Rate (ASC)")) {
                sortTableView("rate", true);
            } else if (selectedSortOption.equals("Rate (DESC)")) {
                sortTableView("rate", false);
            }
        } else {
            System.out.println("No sort option selected.");
        }
    }
    private void sortTableView(String column, boolean ascending) {
        Comparator<Commentaire> comparator = null;
        System.out.println("bbbb");
        switch (column) {
            case "description":
                comparator = Comparator.comparing(Commentaire::getDescription);
                break;
            case "rate":
                comparator = Comparator.comparing(Commentaire::getRate);
                break;
            default:
                break;
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        commentTableView.getItems().sort(comparator);
    }
}


