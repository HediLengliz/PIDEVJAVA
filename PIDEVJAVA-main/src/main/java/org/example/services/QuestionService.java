package org.example.services;

import org.example.models.Question;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService implements IService<Question> {
    private Connection connection;

    public QuestionService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    public void add(Question question) throws SQLException {
        String query = "INSERT INTO question (question, type, priority) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, question.getQuestion());
            statement.setString(2, question.getType());
            statement.setInt(3, question.getPriority());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
        }
    }

    @Override
    public void update(Question question) throws SQLException {
        String query = "UPDATE question SET question=?, type=?, priority=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, question.getQuestion());
            statement.setString(2, question.getType());
            statement.setInt(3, question.getPriority());
            statement.setInt(4, question.getId());
            statement.executeUpdate();
        }

    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM question WHERE id=?";
        String query1 = "DELETE FROM service WHERE question_id=?";
        Question q = this.getById(id);
        String table = "service_" + q.getType();
        String query2 = "DELETE FROM "+table+" WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(query1)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
        try (PreparedStatement statement = connection.prepareStatement(query2)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }

    }

    @Override
    public List<Question> getAll() throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM question";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Question question = new Question();
                question.setId(resultSet.getInt("id"));
                question.setQuestion(resultSet.getString("question"));
                question.setType(resultSet.getString("type"));
                question.setPriority(resultSet.getInt("priority"));
                questions.add(question);
            }
        }
        return questions;
    }

    @Override
    public Question getById(int id) throws SQLException {
        String query = "SELECT * FROM question WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Question question = new Question();
                    question.setId(resultSet.getInt("id"));
                    question.setQuestion(resultSet.getString("question"));
                    question.setType(resultSet.getString("type"));
                    question.setPriority(resultSet.getInt("priority"));
                    return question;
                }
            }
        }
        return null;
    }

    public List<Question> getByType(String type) throws SQLException {
        String query = "SELECT * FROM question WHERE type=? ORDER BY priority";
        List<Question> questions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Question question = new Question();
                    question.setId(resultSet.getInt("id"));
                    question.setQuestion(resultSet.getString("question"));
                    question.setType(resultSet.getString("type"));
                    question.setPriority(resultSet.getInt("priority"));
                    questions.add(question);
                }
            }
        }
        return questions;
    }
}
