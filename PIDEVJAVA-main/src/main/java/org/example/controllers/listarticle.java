package org.example.controllers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.models.Article;
import org.example.services.articleservices;


import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class listarticle {
    public ImageView imageVieww;

    //create constructor
    public listarticle(){}
    public Button addarticle;
    @FXML
    private TextField search;
    public TextArea commentTextArea;
    public VBox commentContainer;
    public TextArea descriptionArea;
    public Label dateLabel;
    public Label categoryLabel;
    public Label authorLabel;
    public Label titlelable;
    public ImageView articleImage;
    public Button PDF;

    articleservices articleServices = new articleservices();

    @FXML
    private TableView<Article> articleTableView = new TableView<>();
    @FXML
    private TableColumn<Article, String> idColumn = new TableColumn<>();

    @FXML
    private TableColumn<Article, String> titleColumn = new TableColumn<>();

    @FXML
    private TableColumn<Article, String> descriptionColumn = new TableColumn<>();

    @FXML
    private TableColumn<Article, String> authorNameColumn = new TableColumn<>();

    @FXML
    private TableColumn<Article, String> dateColumn = new TableColumn<>();

    @FXML
    private TableColumn<Article, String> categoryColumn = new TableColumn<>();

    @FXML
    private Button deleteArticle;
    @FXML
    private Button UpdateArticle;
    @FXML
    private TableColumn<Article,ImageView > imageColumn = new TableColumn<>();

    @FXML
    private ImageView imageView;


private FilteredList<Article> filteredData;

    @FXML
    private TextField tableViewSearchFilter = new TextField();
    private final ObservableList<Article> articles = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        ObservableList<String> filterOptions = FXCollections.observableArrayList("auto", "habitat","maladie");
        filterComboBox.setItems(filterOptions);
        // Add a listener to the selection model of the TableView
        articleTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Article>() {
            @Override
            public void changed(ObservableValue<? extends Article> observable, Article oldValue, Article newValue) {
                if (newValue != null) {
                    // Update the image view with the selected article's image
                    String imagePath = "src/main/resources/imgs/"+newValue.getImage();
                    File imageFile = new File(imagePath);
                    System.out.println(imagePath);
                    if (imageFile.exists()) {
                        Image image = null;
                        try {
                            image = new Image(imageFile.toURI().toURL().toString());
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                        imageVieww.setImage(image);
                    }
                    else
                    {

                        System.out.println("Profile picture file not found at: " + imagePath);
                    }

                }
            }
        });

        // Initialize the radio button

        List<Article> articleList = articleServices.getAll();
        articles.addAll(articleList);
        filteredData = new FilteredList<>(articles, p -> true);
        articleTableView.setItems(articles);
        articleTableView.setItems(filteredData);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        authorNameColumn.setCellValueFactory(new PropertyValueFactory<>("authorname"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("datepub"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        search();
    }



    @FXML
    private void search() {
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(article -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (article.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (article.getAuthorname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (article.getCategorie().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (article.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return article.getDatepub().toString().contains(lowerCaseFilter);
            });
        });
    }

    public void  listarticle(TextField tableViewSearchFilter) {
        this.tableViewSearchFilter = tableViewSearchFilter;
        List<Article> articleList = articleServices.getAll();
        articleTableView.getItems().setAll(articleList);

        // Set cell value factories for each column
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        authorNameColumn.setCellValueFactory(new PropertyValueFactory<>("authorname"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("datepub"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        articleTableView.getItems().setAll(articleList);
        // Clear the selection
        articleTableView.getSelectionModel().clearSelection();
        // Clear the table
        articleTableView.getItems().clear();
        // Add the updated list to the table
        articleTableView.getItems().addAll(articleList);
        // Refresh the table
        articleTableView.refresh();
    }


    public void deleteArticle(javafx.event.ActionEvent actionEvent) {
        Article article = articleTableView.getSelectionModel().getSelectedItem();
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Article");
            alert.setHeaderText("Are you sure you want to delete the article titled: " + article.getTitle() + "?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                try {
                    ObservableList<Article> allArticles = articleTableView.getItems();
                    articleServices.delete(article.getId());

                    refreshTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @FXML
    public void UpdateArticle(ActionEvent actionEvent) {
        Article article = articleTableView.getSelectionModel().getSelectedItem();
        if (article != null) {

            try {
                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatearticle.fxml"));
                Parent root = loader.load();
                // Pass selected article to the controller
                updatearticle controller = loader.getController();
                controller.setArticleToUpdate(article);
                // Show update article window
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Update Article");
                stage.showAndWait();
                refreshTableView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Handle case where no article is selected
            System.out.println("No article selected for update.");
        }

    }


    public void addarticle(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addarticle.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Article");
            stage.showAndWait();
            refreshTableView();
            // Handle case where no article is selected
            System.out.println("No article selected for update.");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void refreshTableView() {
        // Clear existing items and reload articles from the service
        articles.clear();
        List<Article> articleList = articleServices.getAll();
        articles.addAll(articleList);
        articleTableView.refresh();

    }


    public void PDF(ActionEvent actionEvent) throws IOException {
        if (articleTableView != null && articleTableView.getSelectionModel().getSelectedItem() != null) {
            Article article = articleTableView.getSelectionModel().getSelectedItem();
            if (article != null) {
                String path = "D:\\java\\PIDEVJAVA\\" + article.getTitle() + ".pdf";
                exportToPDF(article, path);

                try {
                    PdfWriter writer = new PdfWriter(path);
                    PdfDocument pdf = new PdfDocument(writer);
                    Document document = new Document(pdf);

                    document.add(new Paragraph("Articles Details"));
                    document.add(new Paragraph("TITLE: " + article.getTitle()));
                    document.add(new Paragraph("Date: " + article.getDatepub().toString()));
                    document.add(new Paragraph("Author: " + article.getAuthorname()));
                    document.add(new Paragraph("Image: " + article.getImage()));
                    document.add(new Paragraph("Description: " + article.getDescription()));
                    document.add(new Paragraph("Category: " + article.getCategorie()));
                    document.add(new Paragraph("image: " + article.getImage()));
//                document.add(new Paragraph("Prescribed to User ID: " + Article.getUserCIN().getId()));
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
                System.out.println("No Article selected.");
            }
        }


    }

    private void exportToPDF(Article article, String path) {
        try {
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Prescription Details"));
            document.add(new Paragraph("TITLE: " + article.getTitle()));
            document.add(new Paragraph("Date: " + article.getDatepub().toString()));
            document.add(new Paragraph("Author: " + article.getAuthorname()));
            document.add(new Paragraph("Image: " + article.getImage()));
            document.add(new Paragraph("Description: " + article.getDescription()));
            document.add(new Paragraph("Category: " + article.getCategorie()));
        }   catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("An error occurred while exporting the PDF.");
        }
    }
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private void handleFilterChange() {
        String selectedFilter = filterComboBox.getValue();
        articleservices a = new articleservices();
        if (selectedFilter != null) {
            try {
                List<Article> filteredProperties = new ArrayList<Article>();
                if (selectedFilter.equals("auto")) {
                    filteredProperties = a.getStatus_sinister("auto");
                } else if (selectedFilter.equals("habitat")) {
                    filteredProperties = a.getStatus_sinister("habitat");
                }else if (selectedFilter.equals("maladie")) {
                        filteredProperties = a.getStatus_sinister("maladie");
                } else {
                    System.out.println("filtrage mayekhdemch");
                    refreshTableView();
                    return;
                }
                articles.clear();
                articles.addAll(filteredProperties);
               articleTableView.setItems(articles);
            } catch (SQLException e) {

            }
        }
    }

    public void Refresh(ActionEvent actionEvent) {
        articles.clear();
        List<Article> articleList = articleServices.getAll();
        articles.addAll(articleList);
        articleTableView.refresh();

    }






}
