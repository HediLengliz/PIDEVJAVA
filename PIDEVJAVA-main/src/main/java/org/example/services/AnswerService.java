package org.example.services;

import org.example.models.Question;
import org.example.models.Service;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerService{
    private Connection connection;
    private QuestionService questionService = new QuestionService();

    public AnswerService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    public void add(Service service ,int questionId) throws SQLException {
        Question q = questionService.getById(questionId);
        String table = "service_" + q.getType();
        String query = "INSERT INTO service (question_id,name, price,type) VALUES (?, ?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, q.getId());
            statement.setString(2, service.getName());
            statement.setDouble(3, service.getPrice());
            statement.setString(4, q.getType());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                String query1 = "INSERT INTO " + table + " (id) VALUES (?)";
                try (PreparedStatement statement1 = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS)) {
                    statement1.setInt(1, generatedId);
                    statement1.executeUpdate();
                }
            }

        }
    }



    public void update(Service service) throws SQLException {
        String query = "UPDATE service SET name = ?, price = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, service.getName());
            statement.setDouble(2, service.getPrice());
            statement.setInt(3, service.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM service WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<Service> getAllServicesByQuestionId(int questionId) throws SQLException {
        List<Service> services = new ArrayList<>();
        String query = "SELECT s.id, s.name, s.price FROM service s where s.question_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, questionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int serviceId = resultSet.getInt("id");
                    String serviceName = resultSet.getString("name");
                    Float servicePrice = resultSet.getFloat("price");

                    // Create Service object and add it to the list
                    Service service = new Service(serviceId, serviceName, servicePrice);
                    services.add(service);
                }
            }
        }
        return services;
    }


    public Service getServiceById(int serviceId) throws SQLException {
        Service service = null;
        String query = "SELECT id, name, price FROM service WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, serviceId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    Float price = resultSet.getFloat("price");

                    // Create Service object
                    service = new Service(id, name, price);
                }
            }
        }
        return service;
    }

    public int getQuestion(int serviceId)throws SQLException{
        String query = "SELECT question_id FROM service WHERE id = ?";
        int questionId=0;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, serviceId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("question_id");
                    questionId =id;
                }
            }
        }
        return questionId;
    }


}
