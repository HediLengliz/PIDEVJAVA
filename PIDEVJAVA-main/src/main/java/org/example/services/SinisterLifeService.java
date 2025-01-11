package org.example.services;

import org.example.models.SinisterLife;
import org.example.models.User;
import org.example.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SinisterLifeService implements IService<SinisterLife> {
    private final Connection connection;

    public SinisterLifeService() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(SinisterLife sinisterLife) throws SQLException {
        String query = "INSERT INTO sinistre (sinister_type, description, beneficiary_name, location, amount_sinister, status_sinister, date_sinister, sinister_user_id) VALUES ('SinisterLife', ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sinisterLife.getDescription());
            preparedStatement.setString(2, sinisterLife.getBeneficiaryName());
            preparedStatement.setString(3, sinisterLife.getLocation());
            preparedStatement.setFloat(4, sinisterLife.getAmountSinister());
            preparedStatement.setString(5, sinisterLife.getStatusSinister());
            preparedStatement.setDate(6, new java.sql.Date(sinisterLife.getDateSinister().getTime()));
            preparedStatement.setLong(7, sinisterLife.getSinisterUser().getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(SinisterLife sinisterLife) throws SQLException {
        String query = "UPDATE sinistre SET status_sinister = ?, description = ?, beneficiary_name = ?, location = ?, amount_sinister = ?, date_sinister = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sinisterLife.getStatusSinister());
            preparedStatement.setString(2, sinisterLife.getDescription());
            preparedStatement.setString(3, sinisterLife.getBeneficiaryName());
            preparedStatement.setString(4, sinisterLife.getLocation());
            preparedStatement.setFloat(5, sinisterLife.getAmountSinister());
            preparedStatement.setDate(6, new java.sql.Date(sinisterLife.getDateSinister().getTime()));
            preparedStatement.setInt(7, sinisterLife.getId());

            preparedStatement.executeUpdate();
        }
    }


    public void delete(int id) throws SQLException {
        String query = "DELETE FROM sinistre WHERE id = ? AND sinister_type = 'SinisterLife'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting sinister life failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to delete sinister life with ID: " + id, e);
        }
    }

    @Override
    public List<SinisterLife> getAll() throws SQLException {
        List<SinisterLife> sinisterLives = new ArrayList<>();
        String query = """
    SELECT sl.*, u.phone_number
    FROM sinistre sl
    JOIN user u ON sl.sinister_user_id = u.id
    WHERE sl.sinister_type = 'sinisterLife'
    """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                SinisterLife sinisterLife = new SinisterLife();

                sinisterLife.setId(resultSet.getInt("id"));
                sinisterLife.setDescription(resultSet.getString("description"));
                sinisterLife.setBeneficiaryName(resultSet.getString("beneficiary_name"));
                sinisterLife.setLocation(resultSet.getString("location"));
                sinisterLife.setAmountSinister(resultSet.getFloat("amount_sinister"));
                sinisterLife.setStatusSinister(resultSet.getString("status_sinister"));
                sinisterLife.setDateSinister(resultSet.getDate("date_sinister"));
                User user = new User();
                user.setId(resultSet.getInt("sinister_user_id"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                sinisterLife.setSinisterUser(user);

                sinisterLives.add(sinisterLife);
            }
        }
        return sinisterLives;
    }
    public List<SinisterLife> getAllSortedByIdDesc() throws SQLException {
        List<SinisterLife> sinisterLives = new ArrayList<>();
        // Assuming 'sinister' table has an 'id' column. Adjust if necessary.
        String query = "SELECT * FROM sinistre ORDER BY id DESC";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                SinisterLife sinisterLife = new SinisterLife();
                sinisterLife.setId(resultSet.getInt("id"));
                sinisterLife.setDescription(resultSet.getString("description"));
                sinisterLife.setBeneficiaryName(resultSet.getString("beneficiary_name"));
                sinisterLife.setLocation(resultSet.getString("location"));
                sinisterLife.setAmountSinister(resultSet.getFloat("amount_sinister"));
                sinisterLife.setStatusSinister(resultSet.getString("status_sinister"));
                sinisterLife.setDateSinister(resultSet.getDate("date_sinister"));

                User user = new User();
                user.setId(resultSet.getInt("sinister_user_id"));
                sinisterLife.setSinisterUser(user);

                sinisterLives.add(sinisterLife);
            }
        }
        return sinisterLives;
    }

    public SinisterLife getLastAddedSinisterLife() throws SQLException {
        List<SinisterLife> sinisterLives = getAllSortedByIdDesc();
        if (!sinisterLives.isEmpty()) {
            return sinisterLives.get(0);
        }
        return null;
    }

    public List<SinisterLife> getByUserId(int userId) throws SQLException {
        List<SinisterLife> sinisterLives = new ArrayList<>();
        String query = """
        SELECT *
        FROM sinistre
        WHERE sinister_type = 'sinisterLife' AND sinister_user_id = ?
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    SinisterLife sinisterLife = mapRowToSinisterLife(resultSet);
                    sinisterLives.add(sinisterLife);
                }
            }
        }
        return sinisterLives;
    }

    private SinisterLife mapRowToSinisterLife(ResultSet resultSet) throws SQLException {
        SinisterLife sinisterLife = new SinisterLife();
        sinisterLife.setId(resultSet.getInt("id"));
        sinisterLife.setDescription(resultSet.getString("description"));
        sinisterLife.setBeneficiaryName(resultSet.getString("beneficiary_name"));
        sinisterLife.setLocation(resultSet.getString("location"));
        sinisterLife.setAmountSinister(resultSet.getFloat("amount_sinister"));
        sinisterLife.setStatusSinister(resultSet.getString("status_sinister"));
        sinisterLife.setDateSinister(resultSet.getDate("date_sinister"));

        return sinisterLife;
    }



    public int countByStatusAndUserId(String status, int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM sinistre WHERE status_sinister = ? AND sinister_user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }




    @Override
    public SinisterLife getById(int id) throws SQLException {

        return null;
    }
}
