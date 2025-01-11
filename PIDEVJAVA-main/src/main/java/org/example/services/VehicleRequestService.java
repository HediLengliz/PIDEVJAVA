package org.example.services;

import org.example.models.VehicleRequest;
import org.example.services.IService;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VehicleRequestService implements IService<VehicleRequest> {

    private Connection connection;

    public VehicleRequestService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(VehicleRequest vehicleRequest) throws SQLException {
        String insuranceRequestSql = "INSERT INTO insurance_request ( user_id, date_request, type_insurance, status, type) " +
                "VALUES ( " + vehicleRequest.getUser().getId() + ", '" + vehicleRequest.getDateRequest() + "', '" +
                vehicleRequest.getTypeInsurance() + "', '" + vehicleRequest.getStatus() + "', 'VehicleRequest')";

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

        String vehicleRequestSql = "INSERT INTO vehicle_request (id, marque, modele, fab_year, serial_number, matricul_number, vehicle_price) " +
                "VALUES (" + insuranceRequestId + ", '" + vehicleRequest.getMarque() + "', '" + vehicleRequest.getModele() + "', '" + vehicleRequest.getFabYear() + "', '" +
                vehicleRequest.getSerialNumber() + "', '" + vehicleRequest.getMatriculNumber() + "', '" + vehicleRequest.getVehiclePrice() + "')";

        statement.executeUpdate(vehicleRequestSql);
        System.out.println("Life Request SQL: " + vehicleRequestSql);
        statement.close();
    }

    @Override
    public void update(VehicleRequest vehicleRequest) throws SQLException {

        String sql = "UPDATE vehicle_request SET marque = ?, modele = ?, fab_year = ?, serial_number = ?, matricul_number = ?, vehicle_price = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, vehicleRequest.getMarque());
        preparedStatement.setString(2, vehicleRequest.getModele());
        preparedStatement.setDate(3, Date.valueOf(vehicleRequest.getFabYear()));
        preparedStatement.setString(4, vehicleRequest.getSerialNumber());
        preparedStatement.setString(5, vehicleRequest.getMatriculNumber());
        preparedStatement.setString(6, vehicleRequest.getVehiclePrice());
        preparedStatement.setInt(7, vehicleRequest.getId_vehicle());
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    @Override
    public void delete(int id) throws SQLException {
        String vehicleRequestSql = "DELETE FROM vehicle_request WHERE id = ?";
        String insuranceRequestSql = "DELETE FROM insurance_request WHERE id = ?";

        PreparedStatement vehicleRequestStatement = connection.prepareStatement(vehicleRequestSql);
        PreparedStatement insuranceRequestStatement = connection.prepareStatement(insuranceRequestSql);
        vehicleRequestStatement.setInt(1, id);
        vehicleRequestStatement.executeUpdate();
        insuranceRequestStatement.setInt(1, id);
        insuranceRequestStatement.executeUpdate();
    }

    @Override
    public List<VehicleRequest> getAll() throws SQLException {
        String sql = "SELECT sp.id, sp.marque, sp.modele, sp.fab_year, sp.serial_number, sp.matricul_number, sp.vehicle_price, s.date_request, s.status, s.id " +
                "FROM vehicle_request sp " +
                "INNER JOIN insurance_request s ON sp.id = s.id ";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<VehicleRequest> vehicleRequests = new ArrayList<>();
        while (rs.next()) {
            VehicleRequest lr = new VehicleRequest();
            lr.setId(rs.getInt("id"));
            lr.setDateRequest(LocalDate.parse(rs.getString("date_request")));
            lr.setStatus(rs.getString("status"));
            lr.setMarque(rs.getString("marque"));
            lr.setModele(rs.getString("modele"));
            lr.setMatriculNumber(rs.getString("matricul_number"));
            lr.setSerialNumber(rs.getString("serial_number"));
            lr.setVehiclePrice(rs.getString("vehicle_price"));
            lr.setFabYear(LocalDate.parse(rs.getString("fab_year")));


            vehicleRequests.add(lr);
        }
        return vehicleRequests;    }

    @Override
    public VehicleRequest getById(int id) throws SQLException {
        String sql = "SELECT id, marque, modele, fab_year, serial_number, matricul_number, vehicle_price FROM vehicle_request WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println(id);

        if (resultSet.next()) {
            int id_1 = Integer.parseInt(resultSet.getString("id"));
            String marque = resultSet.getString("marque");
            String modele = resultSet.getString("modele");
            LocalDate fabYear =  LocalDate.parse(resultSet.getString("date_request"));
            String serialNumber = resultSet.getString("serial_number");
            String matriculNumber = resultSet.getString("matricul_number");
            String vehiclePrice = resultSet.getString("vehicle_price");

            return new VehicleRequest(id_1 ,marque, modele, fabYear, serialNumber, matriculNumber, vehiclePrice);
        } else {
            return null;
        }
    }
    public List<VehicleRequest> getByUserId(int userId) throws SQLException {
        String sql = "SELECT sp.id, sp.marque, sp.modele, sp.fab_year, sp.serial_number, sp.matricul_number, sp.vehicle_price, s.date_request, s.status, s.id " +
                "FROM vehicle_request sp " +
                "INNER JOIN insurance_request s ON sp.id = s.id " +
                "WHERE s.user_id = ?";
        List<VehicleRequest> properties = new ArrayList<>();
        System.out.println(userId);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                VehicleRequest property = new VehicleRequest(
                        resultSet.getInt("id"),
                        LocalDate.parse(resultSet.getString("date_request")),
                        resultSet.getString("status"),
                        resultSet.getString("marque"),
                        resultSet.getString("modele"),
                        LocalDate.parse(resultSet.getString("fab_year")),
                        resultSet.getString("serial_number"),
                        resultSet.getString("matricul_number"),
                        resultSet.getString("vehicle_price")
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