package org.example.controllers;


import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.*;
import org.example.models.Question;
import org.example.models.Quote;
import org.example.models.User;
import org.example.services.QuestionService;
import org.example.services.QuoteService;
import org.example.services.UserService;
import org.example.utils.CurrentUser;
import org.example.utils.EmailSender;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class QuotesController implements Initializable {


    @FXML
    private VBox pnItems = null;

    private User user = CurrentUser.getCurrentUser();

    @FXML
    private ScrollPane pnlReclamation;
    private final QuestionService questionService = new QuestionService();


    public Document generatePDF(List<Question> questions , Quote quote) {
        Document document = new Document();
        try {
            String userHome = System.getProperty("user.home");
            String filePath = userHome + File.separator + "Downloads" + File.separator + "devis.pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            //PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\21652\\Downloads\\devis.pdf"));
            System.out.println(filePath);
            document.open();
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = currentDate.format(formatter);
            String headerContent = String.format("%s\nDate: %s", "Protechtini", formattedDate);
            document.add(new Paragraph(headerContent));
            document.add(new Paragraph(String.format("Devis Numéro: %s", quote.getId())));
            document.add(new Paragraph(String.format("Client: %s", user.getLastName()+" "+user.getFirstName())));
            document.add(new Paragraph(String.format("Assurance Type: %s", quote.getType())));
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
            document.add(new Paragraph(String.format("Total: %s", quote.getAmount())));
            document.close();
            writer.close();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }
    public void afficherPropertyRequest() {
        System.out.println("rtyhujklkjhgfdsdfghjklkjhgfd");
        try {
            QuoteService quoteService = new QuoteService();
            List<Quote> quotes = quoteService.getAllByUser(user.getId());
            FXMLLoader l = new FXMLLoader(getClass().getResource("/getDevis.fxml"));
            HBox it = l.load();
            Label idL = (Label) it.lookup("#id");
            Label typeL = (Label) it.lookup("#type");
            Label amountL = (Label) it.lookup("#amount");
            idL.setText("ID");
            typeL.setText("Type");
            amountL.setText("Date");


            idL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-padding: 0 0 0 20;");
            typeL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; ");
            amountL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-padding: 0 5 0 0;");


            // Style for DATE label
            pnItems.getChildren().add(it);

            for (int i = 0; i < quotes.size(); i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/getDevis.fxml"));
                HBox item = loader.load();
                Label idLabel = (Label) item.lookup("#id");
                Label typeLabel = (Label) item.lookup("#type");
                Label amountLabel = (Label) item.lookup("#amount");
                FontAwesomeIconView pd = (FontAwesomeIconView) item.lookup("#pdfIcon");
                pd.setVisible(true);
                pd.setGlyphName(String.valueOf(FontAwesomeIcon.FILE_PDF_ALT));
                pd.setStyle("-fx-fill: red;");
                pd.setFill(Color.web("#ff1744"));
                pd.setSize("1.5em");

                FontAwesomeIconView iconListen = (FontAwesomeIconView) item.lookup("#listenIcon");
                iconListen.setVisible(true);
                iconListen.setGlyphName(String.valueOf(FontAwesomeIcon.MUSIC));
                iconListen.setStyle("-fx-fill: red;");
                iconListen.setFill(Color.web("#ff1744"));
                iconListen.setSize("1.5em");
                FontAwesomeIconView email = (FontAwesomeIconView) item.lookup("#emailIcon");
                email.setVisible(true);
                email.setGlyphName(String.valueOf(FontAwesomeIcon.MAIL_FORWARD));
                email.setStyle("-fx-fill: red;");
                email.setFill(Color.web("#ff1744"));
                email.setSize("1.5em");
                Quote quote = quotes.get(i);
                idLabel.setText(String.valueOf(quote.getId()));
                typeLabel.setText(quote.getType());
                amountLabel.setText(quote.getAmount().toString());
                List<Question> questions = questionService.getByType(quote.getType());


                pd.setOnMouseClicked(event -> {
                    Document d = generatePDF(questions,quote);
                });
                email.setOnMouseClicked(event ->{
                    Document d = generatePDF(questions,quote);
                    String userHome = System.getProperty("user.home");
                    String filePath = userHome + File.separator + "Downloads" + File.separator + "devis.pdf";
                    String emailBody = "hello "+user.getFirstName()+" this is your quote ";
                    EmailSender.sendEmailWithAttachment("azizbamar16@gmail.com", "Quote",emailBody,filePath);});

                iconListen.setOnMouseClicked(event ->{
                    Document d = generatePDF(questions,quote);
                    try {
                        String[] command = {"curl", "--location", "--request", "POST", "http://127.0.0.1:8000/upload/",
                                "--header", "Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJheml6YmFtYXIxNkBnbWFpbC5jb20ifQ.2ituMuYj-zkkgNSVr9cEBGPXXoF7UGhmaOloZGxi2dQ",
                                "--form", "file=@\"+filePath"};
                        ProcessBuilder builder = new ProcessBuilder(command);
                        builder.redirectErrorStream(true);

                        try {
                            Process process = builder.start();
                            InputStream inputStream = process.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                            // Create an output stream to write the audio data
                            FileOutputStream outputStream = new FileOutputStream("response_audio.mp3");

                            // Read and write the response data
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }

                            // Close streams
                            outputStream.close();
                            inputStream.close();

                            // Wait for the process to complete
                            int exitCode = process.waitFor();
                            if (exitCode == 0) {
                                System.out.println("Curl command executed successfully.");
                                String audioFilePath = "/home/aziz/IdeaProjects/Protechtini/response_audio.mp3";
                                //Media media = new Media(new File(audioFilePath).toURI().toString());
                                //MediaPlayer mediaPlayer = new MediaPlayer(media);
                                //mediaPlayer.setAutoPlay(true);
                                //MediaView mediaView = new MediaView(mediaPlayer);
                                //BorderPane borderPane = new BorderPane();
                                //borderPane.setPrefSize(600, 400); // Set the preferred size of the BorderPane

                                //borderPane.setCenter(mediaView);


                                //FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/audio.fxml"));
                                //Parent popupRoot = loader1.load();
                                //Stage popupStage = new Stage();
                                //popupStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
                                //popupStage.setTitle("Audio Player"); // Set the title of the popup window
                                //Scene popupScene = new Scene(borderPane);
                                //popupStage.setScene(popupScene);
                                //popupStage.showAndWait();


                                FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/audio.fxml"));
                                Parent popupRoot = null;
                                try {
                                    popupRoot = loader1.load();
                                    MediaPlayerController controller = loader1.getController();
                                    controller.OpenFileMethod(audioFilePath);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Stage popupStage = new Stage();
                                popupStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
                                popupStage.setTitle("edit Question"); // Set the title of the popup window
                                // Set the scene with the loaded FXML for the popup
                                Scene popupScene = new Scene(popupRoot);
                                popupStage.setScene(popupScene);
                                // Show the popup window and wait for it to close
                                popupStage.showAndWait();
                            } else {
                                System.out.println("Curl command failed with exit code: " + exitCode);
                            }
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }



                    }catch (Exception e) {
                        e.printStackTrace();
                    }



                });
                pnItems.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshHBoxItems(VBox hBox) {
        try {
            QuoteService r = new QuoteService();
            List<Quote> quotes = r.getAllByUser(user.getId());

            hBox.getChildren().clear();
            FXMLLoader l = new FXMLLoader(getClass().getResource("/getDevis.fxml"));
            HBox it = l.load();

            Label idL = (Label) it.lookup("#id");
            Label typeL = (Label) it.lookup("#type");
            Label amountL = (Label) it.lookup("#amount");


            Button btn = (Button) it.lookup("#btn");
            btn.setText("ACTIONS");
            btn.setStyle(" -fx-border-color: transparent;-fx-fill: white; -fx-padding: 0;");



            idL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-padding: 0 0 0 20;");
            typeL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; ");
            amountL.setStyle("-fx-font-size: 16px; -fx-text-fill: #301bef; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-padding: 0 5 0 0;");


            // Style for DATE label
            btn.setStyle("-fx-background-color: transparent; " +
                    "-fx-border-color: transparent; " +
                    "-fx-padding: 0; " +
                    "-fx-text-fill: #301bef; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: bold;");


            // Add the updated list of items to the HBox
            for (Quote quote : quotes) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/getDevis.fxml"));
                HBox item = loader.load();


                Label idLabel = (Label) item.lookup("#id");
                Label typeLabel = (Label) item.lookup("#type");
                Label amountLabel = (Label) item.lookup("#amount");


                idLabel.setText(String.valueOf(quote.getId()));
                typeLabel.setText(quote.getType());
                amountLabel.setText(quote.getAmount().toString());

                Button p = (Button) item.lookup("#pdfIcon");
                p.setText("exporter");



                idLabel.setText(String.valueOf(quote.getId()));
                typeLabel.setText(quote.getType());
                amountLabel.setText(quote.getAmount().toString());
                List<Question> questions = questionService.getByType(quote.getType());
                p.setOnAction(event -> {
                    generatePDF(questions,quote);
                });


                pnItems.getChildren().add(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    ArrayList<Node> nodes = new <Node>ArrayList();
    @FXML
    ImageView profilePicImageView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String imageUUID = CurrentUser.getCurrentUser().getImage(); // Assuming this returns the UUID
        System.out.println(imageUUID);
        if (imageUUID != null && !imageUUID.isEmpty()) {
            String imagePath = "src/main/resources/images/profiles/" + imageUUID ; // Adjust file extension if necessary
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toURL().toString());
                    profilePicImageView.setImage(image);
                } else {
                    // Handle case where profile picture file does not exist
                    System.out.println("Profile picture file not found at: " + imagePath);
                }
            } catch (MalformedURLException e) {
                // Handle malformed URL exception
                e.printStackTrace();
            }
        } else {
            // Handle case where UUID is null or empty
            System.out.println("Profile picture UUID is null or empty.");
        }
        afficherPropertyRequest();
    }
    @FXML
    void listLifeRequest(ActionEvent event)
    {
        openWindow("/LifeRequest/test.fxml", "User Informations");
    }

    @FXML
    void userInformations(ActionEvent event)
    {
        openWindow("/users/userInfo.fxml", "User Informations");
    }
    @FXML
    void goToChangePassword(){
        System.out.println("aaa");
        openWindow("/users/changePassword.fxml", "User Informations");
    }

    @FXML
    void listPropertyRequest(ActionEvent event) {
        openWindow("/PropertyRequest/HboxGetProp.fxml", "List Property Request");
    }
    @FXML
    void listVehicleRequest(ActionEvent event) {
        openWindow("/VehicleRequest/HboxGetVehi.fxml", "List Vehicle Request");
    }

    @FXML
    void listUserReclamations(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/reclamation/userReclamations.fxml", "User Reclamations");
    }
    @FXML
    void listSinistreHabitat(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/sinisterProperty/showTable.fxml", "User Reclamations");
    }
    @FXML
    void listSinistreVehicule(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/sinisterVehicle/showTableV.fxml", "User Reclamations");
    }
    @FXML
    void listSinistreMaladie(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/ShowSinisterLifeForUSer.fxml", "User Reclamations");
    }

    @FXML
    void listDevis(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/test2.fxml", "User Reclamations");
    }
    @FXML
    void contrat(ActionEvent event)
    {
        System.out.println(profilePicImageView);
        openWindow("/ContratHabitat/ContratInUser.fxml", "User Reclamations");
    }


    private Stage stage ;
    private Scene scene ;

    private Parent root ;
    private void openWindow(String fxmlFilePath, String title) {
        try {
            root =FXMLLoader.load(getClass().getResource(fxmlFilePath));
            Stage stage = (Stage) pnItems.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button btnOverview;
    private FileChooser fileChooser;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;
    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlSettings;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;
    private final UserService userService = new UserService();
    private User selectedUser; // Holds the selected user for updating

    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK))
            {
                pnItems.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/users/login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}
