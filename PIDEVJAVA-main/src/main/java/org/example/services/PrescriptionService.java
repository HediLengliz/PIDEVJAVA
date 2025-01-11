package org.example.services;

import org.example.models.Prescription;
import org.example.models.User;
import org.example.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionService implements IService<Prescription> {
    private final Connection connection;

    public PrescriptionService() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Prescription prescription) throws SQLException {
        String query = "INSERT INTO prescription (date_prescription, medications, status_prescription, additional_notes, validity_duration, user_cin_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(prescription.getDatePrescription()));
            preparedStatement.setString(2, prescription.getMedications());
            preparedStatement.setString(3, prescription.getStatusPrescription());
            preparedStatement.setString(4, prescription.getAdditionalNotes());
            preparedStatement.setInt(5, prescription.getValidityDuration());
            preparedStatement.setLong(6, prescription.getUserCIN().getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(Prescription prescription) throws SQLException {
        String query = "UPDATE prescription SET date_prescription = ? ,  medications = ?, status_prescription = ?, additional_notes = ?, validity_duration = ?, user_cin_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, String.valueOf(prescription.getDatePrescription()));

            preparedStatement.setString(2, prescription.getMedications());
            preparedStatement.setString(3, prescription.getStatusPrescription());
            preparedStatement.setString(4, prescription.getAdditionalNotes());
            preparedStatement.setInt(5, prescription.getValidityDuration());
            preparedStatement.setLong(6, prescription.getUserCIN().getId());
            preparedStatement.setLong(7, prescription.getId());

            preparedStatement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM prescription WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting prescription failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to delete prescription with ID: " + id, e);
        }
    }

    @Override
    public List<Prescription> getAll() throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        // Adjusted query to fetch first name and last name
        String query = "SELECT p.*, u.first_name, u.last_name, u.email FROM prescription p INNER JOIN user u ON p.user_cin_id = u.id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Prescription prescription = new Prescription();
                prescription.setId(resultSet.getLong("id"));
                prescription.setDatePrescription(resultSet.getDate("date_prescription").toLocalDate());
                prescription.setMedications(resultSet.getString("medications"));
                prescription.setStatusPrescription(resultSet.getString("status_prescription"));
                prescription.setAdditionalNotes(resultSet.getString("additional_notes"));
                prescription.setValidityDuration(resultSet.getInt("validity_duration"));

                User user = new User();
                user.setId(resultSet.getInt("user_cin_id"));
                // Setting the first name, last name, and email fetched from the database
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));

                prescription.setUserCIN(user);

                prescriptions.add(prescription);
            }
        }
        return prescriptions;
    }


    @Override
    public Prescription getById(int id) throws SQLException {
        return null;
    }

    public List<Prescription> getPrescriptionsByUser(Integer userId) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        String query = "SELECT p.*, u.first_name, u.last_name, u.email " +
                "FROM prescription p " +
                "JOIN user u ON p.user_cin_id = u.id " +
                "WHERE p.user_cin_id = ?";


        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Prescription prescription = new Prescription();
                    prescription.setId(resultSet.getLong("id"));
                    prescription.setDatePrescription(resultSet.getDate("date_prescription").toLocalDate());
                    prescription.setMedications(resultSet.getString("medications"));
                    prescription.setStatusPrescription(resultSet.getString("status_prescription"));
                    prescription.setAdditionalNotes(resultSet.getString("additional_notes"));
                    prescription.setValidityDuration(resultSet.getInt("validity_duration"));

                    User user = new User();
                    user.setId(userId);
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setEmail(resultSet.getString("email"));

                    prescription.setUserCIN(user);
                    prescriptions.add(prescription);
                }
            }
        }
        return prescriptions;
    }


}
