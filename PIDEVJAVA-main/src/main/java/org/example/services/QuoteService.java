package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.Quote;
import org.example.models.User;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.util.*;

public class QuoteService implements IService<Quote>{
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Connection connection;
    public QuoteService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void add(Quote quote) throws SQLException {
        String serviceJson;
        try {
            serviceJson = objectMapper.writeValueAsString(quote.getServices());
        } catch (Exception e) {
            throw new SQLException("Error serializing roles to JSON: " + e.getMessage());
        }
        String query = "INSERT INTO quote (type, amount, user_id, services) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, quote.getType());
            statement.setDouble(2, quote.getAmount());
            statement.setInt(3, quote.getUser().getId());
            statement.setString(4,serviceJson);
            statement.executeUpdate();
        }

    }

    @Override
    public void update(Quote quote) throws SQLException {

    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public List<Quote> getAll() throws SQLException {
        List<Quote> quotes = new ArrayList<>();
        String query = "SELECT * FROM quote";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String[] servicesString = resultSet.getString("services").split(",");
                String[] servicesArray = servicesString;
                List<String> serviceList = List.of(servicesArray);
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                Float amount = resultSet.getFloat("amount");
                User u = new User();
                Quote quote = new Quote(id,type,amount, u);
                quote.setServices(serviceList);
                quotes.add(quote);
            }
        }
        return quotes;
    }


    public List<Quote> getAllByUser(int id) throws SQLException {
        List<Quote> quotes = new ArrayList<>();
        String query = "SELECT * FROM quote WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String[] servicesString = resultSet.getString("services").split(",");
                    List<String> serviceList = Arrays.asList(servicesString);
                    int quoteId = resultSet.getInt("id");
                    String type = resultSet.getString("type");
                    float amount = resultSet.getFloat("amount");
                    User user = new User(); // You should retrieve user information from the database based on user_id
                    Quote quote = new Quote(quoteId, type, amount, user);
                    quote.setServices(serviceList);
                    quotes.add(quote);
                }
            }
        }
        return quotes;
    }

    @Override
    public Quote getById(int id) throws SQLException {
        return null;
    }
}
