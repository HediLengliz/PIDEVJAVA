package org.example.services;

import org.example.models.Rapport;
import org.example.models.SinisterProperty;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RapportService implements IService<Rapport> {
    private Connection connection;

    public RapportService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Rapport rapport) throws SQLException {
        String sql = "INSERT INTO rapport (decision, justification, sinister_rapport_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, rapport.getDecision());
            statement.setString(2, rapport.getJustification());
            statement.setLong(3, rapport.getSinisterRapport().getId()); // Set the sinister_rapport_id
            statement.executeUpdate();

            // Retrieve the auto-generated ID and set it in the Rapport object
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                rapport.setId(id);
            } else {
                throw new SQLException("Failed to get auto-generated ID for Rapport.");
            }
        }
    }

    public void add2(Rapport rapport) throws SQLException {
        String sql = "INSERT INTO rapport (decision, justification, sinister_rapport_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, rapport.getDecision());
            statement.setString(2, rapport.getJustification());
            statement.setLong(3, rapport.getSinisterRapport().getId());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                rapport.setId(id);
            } else {
                throw new SQLException("Failed to get auto-generated ID for Rapport.");
            }
        }
    }


    @Override
    public void update(Rapport rapport) throws SQLException {

    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public List<Rapport> getAll() throws SQLException {
        List<Rapport> rapports = new ArrayList<>();
        String sql = "SELECT * FROM rapport";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Rapport rapport = new Rapport(
                        resultSet.getInt("id"),

                        resultSet.getString("decision"),

                        resultSet.getString("justification")

                );
                rapports.add(rapport);
            }
        }
        return rapports;
    }

    @Override
    public Rapport getById(int id) throws SQLException {
        String sql = "SELECT * FROM rapport WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Rapport rapport = new Rapport(
                        resultSet.getInt("id"),
                        resultSet.getString("decision"),
                        resultSet.getString("justification")
                );

                // Fetch the associated sinister
                int sinisterId = resultSet.getInt("sinister_rapport_id");
                SinisterPropertyService sinisterService = new SinisterPropertyService();
                SinisterProperty sinister = sinisterService.getById(sinisterId);

                rapport.setSinisterRapport(sinister);

                return rapport;
            }
        }
        return null;
    }


    public Rapport getBySinisterId(long id) throws SQLException {

        String sql = "SELECT * FROM rapport WHERE sinister_rapport_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Rapport rapport = new Rapport(
                        resultSet.getInt("id"),
                        resultSet.getString("decision"),
                        resultSet.getString("justification")
                );

                // Fetch the associated sinister
                int sinisterId = resultSet.getInt("sinister_rapport_id");
                SinisterPropertyService sinisterService = new SinisterPropertyService();
                SinisterProperty sinister = sinisterService.getById(sinisterId);

                rapport.setSinisterRapport(sinister);

                return rapport;
            }
        }
        return null;
    }
}
