package org.example.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatGPTService {


    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String callChatGPT(String query) {
        String apiKey = "sk-proj-Iu5P71vu6DJR70LKFUQKT3BlbkFJZ8WQ8i9ZzaaNQ807ldgQ";
        String apiURL = "https://api.openai.com/v1/chat/completions";
        String requestBody = String.format("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}", query);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Detailed logging of response details
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response headers: " + response.headers());
            System.out.println("Response body: " + response.body());

            return parseResponse(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Failed to get response: " + e.getMessage();
        }
    }

    private String parseResponse(String responseBody) {
        try {
            var node = objectMapper.readTree(responseBody);
            return node.get("choices").get(0).get("message").get("content").asText();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error parsing response";
        }
    }
}
