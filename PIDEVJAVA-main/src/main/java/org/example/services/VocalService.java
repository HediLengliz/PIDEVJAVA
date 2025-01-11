package org.example.services;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.InputStream;
import javafx.scene.media.Media;
import java.nio.file.*;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

public class VocalService {

    public void selectPDFAndGenerateAudio() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF File");
        File pdfFile = fileChooser.showOpenDialog(null);

        if (pdfFile != null) {
            try {
                HttpClient httpClient = HttpClient.newHttpClient();

                // Build multipart/form-data request body
                HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofFile(pdfFile.toPath());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8000/upload/")) // Replace with your API URL
                        .header("Content-Type", "multipart/form-data")
                        .POST(bodyPublisher)
                        .build();

                // Send the request asynchronously
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                        .thenAccept(response -> {
                            System.out.println("Response from server: " + response.statusCode());
                            // Play the audio if the response is successful
                            Path tempFile = null;
                            try {
                                tempFile = Files.createTempFile("temp-audio", ".mp3");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                Files.copy(response.body(), tempFile, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            if (response.statusCode() == 200) {
                                Media media = new Media(tempFile.toUri().toString());
                                MediaPlayer mediaPlayer = new MediaPlayer(media);
                                mediaPlayer.play();
                            }
                        })
                        .join(); // Wait for the response
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
