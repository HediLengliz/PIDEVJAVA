package org.example.controllers;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.models.Question;
import org.example.models.Quote;
import org.example.models.Service;
import org.example.services.QuestionService;
import org.example.services.QuoteService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class GetQuoteController implements Initializable {
    private final QuestionService questionService = new QuestionService();
    private final QuoteService quoteService = new QuoteService();
    @FXML
    private TableView<Quote> QuoteTable;
    @FXML
    private TableColumn<Quote, Void> actionColumn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actionColumn.setCellFactory(getButtonCellFactory("export pdf"));

    }

    public void getAllQuotes(int id) {
        try {
            List<Quote> quotes = quoteService.getAll();
            ObservableList<Quote> observableQuote = FXCollections.observableArrayList(quotes);
            QuoteTable.setItems(observableQuote);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(id);
    }
    public void addQuote(ActionEvent event)throws IOException {
        switchToScene(event,"/quoteType.fxml");

    }
    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    @FXML
    public void switchToScene(ActionEvent event, String  link) throws IOException {
        root = FXMLLoader.load(getClass().getResource(link));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    // Define a method to create a cell factory for the action column
    private Callback<TableColumn<Quote, Void>, TableCell<Quote, Void>> getButtonCellFactory(String btnName) {
        return new Callback<TableColumn<Quote, Void>, TableCell<Quote, Void>>() {
            @Override
            public TableCell<Quote, Void> call(final TableColumn<Quote, Void> param) {
                return new TableCell<Quote, Void>() {
                    private final Button viewButton = new Button(btnName);

                    {
                        viewButton.setOnAction(event -> {
                            Quote rowData = getTableView().getItems().get(getIndex());;
                            String action = viewButton.getText();

                            System.out.println(rowData.getType());
                            try {
                                List<Question> questions = questionService.getByType(rowData.getType());
                                generatePDF(questions,rowData);

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("View details button clicked for ID: " + rowData.getId());
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(viewButton);
                        }
                    }
                };
            }
        };
    }

    public void generatePDF(List<Question> questions , Quote quote) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/home/aziz/3a44/pidev/devisTest.pdf"));
            document.open();
            LocalDate currentDate = LocalDate.now();

            // Create a DateTimeFormatter for the desired format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Format the current date using the formatter
            String formattedDate = currentDate.format(formatter);
            // Header
            String headerContent = String.format("%s\nDate: %s", "Protechtini", formattedDate);
            document.add(new Paragraph(headerContent));

            // Title
            document.add(new Paragraph(String.format("Devis Numéro: %s", quote.getId())));

            // Client
            document.add(new Paragraph(String.format("Client: %s", "Aziz")));

            // Assurance Type
            document.add(new Paragraph(String.format("Assurance Type: %s", quote.getType())));

            // Details
            document.add(new Paragraph("Détails du devis:"));
            @SuppressWarnings("unchecked")
            List<String> services = quote.getServices();
            int s = 0;
            for (Question question : questions) {
                // Access each question object here
                String ser = services.get(s).replace("[", "").replace("]", "").replace("\"", "");
                document.add(new Paragraph(question.getQuestion() + ": " +ser));
                s++;
            }

            // Total
            document.add(new Paragraph(String.format("Total: %s", quote.getAmount())));

            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}