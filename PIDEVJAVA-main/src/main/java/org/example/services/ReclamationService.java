package org.example.services;

import org.example.models.Reclamation;
import org.example.utils.CurrentUser;
import org.example.utils.MyDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements org.example.services.IService<Reclamation> {

    private final Connection connection;
    private final ObjectMapper objectMapper;

    public ReclamationService() {
        connection = MyDatabase.getInstance().getConnection();
        objectMapper = new ObjectMapper();
    }
    public List<Reclamation> getReclamationsByUser() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (CurrentUser.getCurrentUser() != null) {
                preparedStatement.setInt(1, CurrentUser.getCurrentUser().getId());

            }
            else preparedStatement.setInt(1, 113);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(resultSet.getInt("id"));
                reclamation.setTitle(resultSet.getString("title"));
                reclamation.setDateReclamation(LocalDate.parse(String.valueOf(resultSet.getDate("date_reclamation"))).atStartOfDay());
                reclamation.setDescription(resultSet.getString("description"));
                reclamations.add(reclamation);
            }
        }
        return reclamations;
    }
    @Override
    public void add(Reclamation reclamation) throws SQLException {
        String sql = "INSERT INTO reclamation (title, description, user_id, date_reclamation) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, reclamation.getTitle());
            preparedStatement.setString(2, reclamation.getDescription());
            preparedStatement.setInt(3, reclamation.getUser().getId());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(reclamation.getDateReclamation()));
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                reclamation.setId(generatedKeys.getInt(1));
            }
        }
    }

    @Override
    public void update(Reclamation reclamation) throws SQLException {
        String sql = "UPDATE reclamation SET title = ?, description = ?, user_id = ?, date_reclamation = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, reclamation.getTitle());
            preparedStatement.setString(2, reclamation.getDescription());
            preparedStatement.setInt(3, reclamation.getUser().getId());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(reclamation.getDateReclamation()));
            preparedStatement.setInt(5, reclamation.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM reclamation WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Reclamation> getAll() throws SQLException {
        String sql = "SELECT * FROM reclamation";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            List<Reclamation> reclamations = new ArrayList<>();
            while (resultSet.next()) {
                Reclamation reclamation = extractReclamationFromResultSet(resultSet);
                reclamations.add(reclamation);
            }
            return reclamations;
        }
    }

    @Override
    public Reclamation getById(int id) throws SQLException {
        String sql = "SELECT * FROM reclamation WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractReclamationFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    private Reclamation extractReclamationFromResultSet(ResultSet resultSet) throws SQLException {
        Reclamation reclamation = new Reclamation();
        reclamation.setId(resultSet.getInt("id"));
        reclamation.setTitle(resultSet.getString("title"));
        reclamation.setDescription(resultSet.getString("description"));
        reclamation.setDateReclamation(resultSet.getTimestamp("date_reclamation").toLocalDateTime());
        // Assuming you have a UserService to get the user details
        org.example.services.UserService userService = new org.example.services.UserService();
        reclamation.setUser(userService.getById(resultSet.getInt("user_id")));
        return reclamation;
    }
}
