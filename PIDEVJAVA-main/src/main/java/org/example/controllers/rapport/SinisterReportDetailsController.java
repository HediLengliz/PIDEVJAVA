package org.example.controllers.rapport;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.models.Rapport;
import org.example.models.SinisterProperty;
import org.example.services.RapportService;
import org.example.services.SinisterPropertyService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.HashMap;
import java.util.Map;

public class SinisterReportDetailsController {
    private RapportService rapportService = new RapportService();
    private SinisterPropertyService sinisterService = new SinisterPropertyService();

    @FXML
    private TextField textFieldD;
    @FXML
    private TextField textFieldJ;
    private Stage stage;

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/dashboard.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {

    }

    public void setSinisterReportDetails(long id) throws SQLException {
        // Fetch the Sinister
        SinisterProperty sinister = sinisterService.getByIdRep(id);

         Rapport rapport2 =rapportService.getBySinisterId(id);
        System.out.println(rapport2.getDecision());
        if (rapport2 != null) {
            // Display Report details
            textFieldD.setText( rapport2.getDecision());
            textFieldJ.setText( rapport2.getJustification());
        } else {
            // Handle if Report details are not found
            System.out.println("Rapport not found");
        }
    }
public void setReportDetails(int id) throws SQLException {
        Rapport rapport = rapportService.getById(id);
        if (rapport != null) {
            textFieldD.setText( rapport.getDecision());
            textFieldJ.setText( rapport.getJustification());
      }}

    private Image generateQRCodeImage(String text, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return new Image(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException | WriterException var8) {
            var8.printStackTrace();
            return null;
        }
    }
    public ImageView generateQRCodeImage;
    public Button generateQRCode;
    public ImageView testimage;
    @FXML
    private void generateQRCode() {

        String Title = this.textFieldD.getText().trim();
        String Content = this.textFieldJ.getText().trim();
        String fileName = "report.pdf"; // Name of the PDF file
        String filePath = "C:\\Users\\21652\\Downloads\\" + fileName;
        String qrDetails = "Decision: " + Title + "\n\nJustification:\n" + Content + "\n\n The report was downloaded in your computer.";
        int width = 350;
        int height = 350;

        exportPrescriptionToPDF(new Rapport(Title, Content), filePath);
        Image qrImage = this.generateQRCodeImage(qrDetails, width, height);
        this.generateQRCodeImage.setImage(qrImage);
    }
    public void exportPrescriptionToPDF(Rapport prescription, String filePath) {
        if (prescription != null) {
            try {
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Rapport Details"));
                document.add(new Paragraph("Decision: " + prescription.getDecision()));
                document.add(new Paragraph("Justification: " + prescription.getJustification()));
                document.add(new Paragraph("Protechtini\n18, rue de l'Usine\nZI Aéroport Charguia\nII 2035 Ariana\nPhone: (+216) 58 26 64 36\nEmail: protechtini.synthcode@esprit.tn"));
                document.close();

                System.out.println("PDF exported successfully to: " + filePath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.err.println("An error occurred while exporting the PDF.");
            }
        } else {
            System.out.println("No Prescription selected.");
        }
    }
}

