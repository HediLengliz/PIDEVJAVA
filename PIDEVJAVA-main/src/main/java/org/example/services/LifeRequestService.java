package org.example.services;

import org.example.models.LifeRequest;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LifeRequestService implements IService<LifeRequest> {

    private Connection connection;

    public LifeRequestService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(LifeRequest lifeRequest) throws SQLException {
        String insuranceRequestSql = "INSERT INTO insurance_request ( user_id, date_request, type_insurance, status, type) " +
                "VALUES ( " + lifeRequest.getUser().getId() + ", '" + lifeRequest.getDateRequest() + "', '" +
                lifeRequest.getTypeInsurance() + "', '" + lifeRequest.getStatus() + "', 'LifeRequest')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(insuranceRequestSql, Statement.RETURN_GENERATED_KEYS);
        System.out.println("blalall" + insuranceRequestSql);
        ResultSet generatedKeys = statement.getGeneratedKeys();
        int insuranceRequestId = -1;
        if (generatedKeys.next()) {
            insuranceRequestId = generatedKeys.getInt(1);
        } else {
            throw new SQLException("Failed to retrieve the generated id for insurance_request");
        }

        String lifeRequestSql = "INSERT INTO life_request (id, age, chron_disease, surgery, civil_status, occupation, chron_disease_description) " +
                "VALUES (" + insuranceRequestId + ", '" + lifeRequest.getAge() + "', '" + lifeRequest.getChronicDisease() + "', '" + lifeRequest.getSurgery() + "', '" +
                lifeRequest.getCivilStatus() + "', '" + lifeRequest.getOccupation() + "', '" + lifeRequest.getChronicDiseaseDescription() + "')";

        statement.executeUpdate(lifeRequestSql);
        System.out.println("Life Request SQL: " + lifeRequestSql);
        statement.close();

    }


    @Override
    public void update(LifeRequest lifeRequest) throws SQLException {
        String sql = "UPDATE life_request SET  age = ?, chron_disease = ?, surgery = ?, civil_status = ?, occupation = ?, chron_disease_description = ?  WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, lifeRequest.getAge());
        preparedStatement.setString(2, lifeRequest.getChronicDisease());
        preparedStatement.setString(3, lifeRequest.getSurgery());
        preparedStatement.setString(4, lifeRequest.getCivilStatus());
        preparedStatement.setString(5, lifeRequest.getOccupation());
        preparedStatement.setString(6, lifeRequest.getChronicDiseaseDescription());
        preparedStatement.setInt(7, lifeRequest.getId_life());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    @Override
    public void delete(int id) throws SQLException {
        String lifeRequestSql = "DELETE FROM life_request WHERE id = ?";
        String insuranceRequestSql = "DELETE FROM insurance_request WHERE id = ?";

        PreparedStatement lifeRequestStatement = connection.prepareStatement(lifeRequestSql);
        PreparedStatement insuranceRequestStatement = connection.prepareStatement(insuranceRequestSql);
        lifeRequestStatement.setInt(1, id);
        lifeRequestStatement.executeUpdate();

        insuranceRequestStatement.setInt(1, id);
        insuranceRequestStatement.executeUpdate();
    }

    @Override
    public List<LifeRequest> getAll() throws SQLException {
        String sql = "SELECT sp.id, sp.age, sp.chron_disease, sp.surgery, sp.civil_status, sp.occupation, sp.chron_disease_description, s.date_request, s.status, s.id " +
                "FROM life_request sp " +
                "INNER JOIN insurance_request s ON sp.id = s.id ";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<LifeRequest> lifeRequests = new ArrayList<>();
        while (rs.next()) {
            LifeRequest lr = new LifeRequest();
            lr.setId(rs.getInt("id"));
            lr.setDateRequest(LocalDate.parse(rs.getString("date_request")));
            lr.setStatus(rs.getString("status"));
            lr.setAge(rs.getString("age"));
            lr.setChronicDisease(rs.getString("chron_disease"));
            lr.setSurgery(rs.getString("surgery"));
            lr.setCivilStatus(rs.getString("civil_status"));
            lr.setOccupation(rs.getString("occupation"));
            lr.setChronicDiseaseDescription(rs.getString("chron_disease_description"));

            lifeRequests.add(lr);
        }
        return lifeRequests;
    }



    @Override
    public LifeRequest getById(int id) throws SQLException {

            String sql = "SELECT id, age, chron_disease, surgery, civil_status, occupation, chron_disease_description FROM life_request WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println(id);

            if (resultSet.next()) {
                int id_1 = Integer.parseInt(resultSet.getString("id"));
                String age = resultSet.getString("age");
                String chronDisease = resultSet.getString("chron_disease");
                String surgery = resultSet.getString("surgery");
                String civilStatus = resultSet.getString("civil_status");
                String Occupation = resultSet.getString("occupation");
                String detailsDisease = resultSet.getString("chron_disease_description");

                return new LifeRequest(id_1 ,age, chronDisease, surgery, civilStatus, Occupation, detailsDisease);
            } else {
                return null;
            }
        }

    public List<LifeRequest> getByUserId(int userId) throws SQLException {
        String sql = "SELECT sp.id, sp.age, sp.chron_disease, sp.surgery, sp.civil_status, sp.occupation, sp.chron_disease_description, s.date_request, s.status, s.id " +
                "FROM life_request sp " +
                "INNER JOIN insurance_request s ON sp.id = s.id " +
                "WHERE s.user_id = ?";
        List<LifeRequest> properties = new ArrayList<>();
        System.out.println(userId);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LifeRequest property = new LifeRequest(
                        resultSet.getInt("id"),
                        LocalDate.parse(resultSet.getString("date_request")),
                        resultSet.getString("status"),
                        resultSet.getString("age"),
                        resultSet.getString("chron_disease"),
                        resultSet.getString("surgery"),
                        resultSet.getString("civil_status"),
                        resultSet.getString("occupation"),
                        resultSet.getString("chron_disease_description")

                );
                properties.add(property);

            }
            System.out.println("dddd");


        }
        catch (SQLException e) {
            throw new RuntimeException("Error fetching sinister properties by User ID: " + userId, e);
        }
        return properties;


    }

    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE insurance_request SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }
}

