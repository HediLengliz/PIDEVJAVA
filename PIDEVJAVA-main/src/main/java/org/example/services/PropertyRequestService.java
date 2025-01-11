package org.example.services;

import org.example.models.PropretyRequest;
import org.example.services.IService;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PropertyRequestService implements IService<PropretyRequest> {

    private Connection connection;

    public PropertyRequestService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(PropretyRequest propretyRequest) throws SQLException {
        String insuranceRequestSql = "INSERT INTO insurance_request ( user_id, date_request, type_insurance, status, type) " +
                "VALUES ( " + propretyRequest.getUser().getId() + ", '" + propretyRequest.getDateRequest() + "', '" +
                propretyRequest.getTypeInsurance() + "', '" + propretyRequest.getStatus() + "', 'PropertyRequest')";

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

        String PropertyRequestSql = "INSERT INTO proprety_request (id,property_forme, number_rooms,address,property_value,surface) " +
                "VALUES (" + insuranceRequestId + ", '" + propretyRequest.getPropertyForm() + "', '" + propretyRequest.getNumberRooms() + "', '" + propretyRequest.getAddress() + "', '" +
                propretyRequest.getSurface() + "', '" + propretyRequest.getPropertyValue() + "')";

        statement.executeUpdate(PropertyRequestSql);
        System.out.println("Property Request SQL: " + PropertyRequestSql);
        statement.close();
    }

    @Override
    public void update(PropretyRequest propretyRequest) throws SQLException {
        String sql = "UPDATE proprety_request SET property_forme = ?, number_rooms = ?, address = ?, property_value = ?, surface = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, propretyRequest.getPropertyForm());
        preparedStatement.setString(2, propretyRequest.getNumberRooms());
        preparedStatement.setString(3, propretyRequest.getAddress());
        preparedStatement.setString(4, propretyRequest.getPropertyValue());
        preparedStatement.setString(5, propretyRequest.getSurface());
        preparedStatement.setInt(6, propretyRequest.getId_property());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    @Override
    public void delete(int id) throws SQLException {
        String propertyRequestSql = "DELETE FROM proprety_request WHERE id = ?";
        String insuranceRequestSql = "DELETE FROM insurance_request WHERE id = ?";

        PreparedStatement propertyRequestStatement = connection.prepareStatement(propertyRequestSql);
        PreparedStatement insuranceRequestStatement = connection.prepareStatement(insuranceRequestSql);
        propertyRequestStatement.setInt(1, id);
        propertyRequestStatement.executeUpdate();
        insuranceRequestStatement.setInt(1, id);
        insuranceRequestStatement.executeUpdate();

    }

    @Override
    public List<PropretyRequest> getAll() throws SQLException {
        String sql = "SELECT sp.id, sp.property_forme, sp.number_rooms, sp.address, sp.property_value, sp.surface, s.date_request, s.status, s.id " +
                "FROM proprety_request sp " +
                "INNER JOIN insurance_request s ON sp.id = s.id ";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<PropretyRequest> propretyRequests = new ArrayList<>();
        while (rs.next()) {
            PropretyRequest lr = new PropretyRequest();
            lr.setId(rs.getInt("id"));
            lr.setDateRequest(LocalDate.parse(rs.getString("date_request")));
            lr.setStatus(rs.getString("status"));
            lr.setPropertyValue(rs.getString("property_value"));
            lr.setPropertyForm(rs.getString("property_forme"));
            lr.setAddress(rs.getString("address"));
            lr.setSurface(rs.getString("surface"));
            lr.setNumberRooms(rs.getString("number_rooms"));

            propretyRequests.add(lr);
        }
        return propretyRequests;
    }

    @Override
    public PropretyRequest getById(int id) throws SQLException {

        String sql = "SELECT id, property_forme, number_rooms,address,property_value,surface FROM proprety_request WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println(id);

        if (resultSet.next()) {
            int id_1 = Integer.parseInt(resultSet.getString("id"));
            String propertyForm = resultSet.getString("age");
            String numberRooms = resultSet.getString("chron_disease");
            String address = resultSet.getString("surgery");
            String propertyValue = resultSet.getString("civil_status");
            String surface = resultSet.getString("occupation");

            return new PropretyRequest(id_1 ,propertyForm, numberRooms, address, propertyValue, surface);
        } else {
            return null;
        }
    }
    public List<PropretyRequest> getByUserId(int userId) throws SQLException {
        String sql = "SELECT sp.id, sp.property_forme, sp.number_rooms, sp.address, sp.property_value, sp.surface, s.date_request, s.status, s.id " +
                "FROM proprety_request sp " +
                "INNER JOIN insurance_request s ON sp.id = s.id " +
                "WHERE s.user_id = ?";
        List<PropretyRequest> properties = new ArrayList<>();
        System.out.println(userId);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PropretyRequest property = new PropretyRequest(
                        resultSet.getInt("id"),
                        LocalDate.parse(resultSet.getString("date_request")),
                        resultSet.getString("status"),
                        resultSet.getString("property_forme"),
                        resultSet.getString("number_rooms"),
                        resultSet.getString("address"),
                        resultSet.getString("property_value"),
                        resultSet.getString("surface")


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