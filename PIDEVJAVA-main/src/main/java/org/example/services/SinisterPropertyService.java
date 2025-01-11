package org.example.services;

import org.example.models.SinisterProperty;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SinisterPropertyService implements IService<SinisterProperty> {

    private Connection connection;
    private Statement ste;
    private PreparedStatement pst;

    public SinisterPropertyService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    public List<SinisterProperty> getStatus_sinister(String status) throws SQLException {
        String query = "SELECT sp.id, sp.type_degat, sp.description_degat, s.date_sinister, s.location, s.status_sinister " +
                "FROM sinister_property sp " +
                "INNER JOIN sinister s ON sp.id = s.id " +
                "WHERE s.status_sinister = ?";
        List<SinisterProperty> properties = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, status);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                SinisterProperty property = new SinisterProperty(
                        rs.getLong("id"),
                        rs.getDate("date_sinister").toLocalDate(),
                        rs.getString("location"),
                        rs.getString("status_sinister"),
                        rs.getString("type_degat"),
                        rs.getString("description_degat")
                );
                properties.add(property);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching sinister properties by status: " + status, e);
        }
        return properties;
    }

    @Override
    public void add(SinisterProperty sinister_property) throws SQLException {
        String query = "INSERT INTO sinister (date_sinister, location, sinister_user_id, status_sinister) VALUES (?, ?, ?, ?)";
        try {
            pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setDate(1, java.sql.Date.valueOf(sinister_property.getDate_sinister()));
            pst.setString(2, sinister_property.getLocation());
            pst.setInt(3, 1); // Assuming 1 is the ID of the user
            pst.setString(4, sinister_property.getStatus_sinister());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            long id;
            if (rs.next()) {
                id = rs.getLong(1);
            } else {
                throw new SQLException("Failed to retrieve generated ID for Sinister");
            }

            query = "INSERT INTO sinister_property (id, type_degat, description_degat) VALUES (?, ?, ?)";
            pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setLong(1, id);  // Using the generated sinister ID
            pst.setString(2, sinister_property.getType_degat());
            pst.setString(3, sinister_property.getDescription_degat());
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                sinister_property.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(SinisterProperty sinisterProperty) throws SQLException {
//        String updateSinisterQuery = "UPDATE sinister SET date_sinister = ?, location = ?, status_sinister = ? WHERE id = ?";
//        String updatePropertyQuery = "UPDATE sinister_property SET type_degat = ?, description_degat = ? WHERE id = ?";
//
//        try {
//            pst = connection.prepareStatement(updateSinisterQuery);
//            pst.setDate(1, java.sql.Date.valueOf(sinisterProperty.getDate_sinister()));
//            pst.setString(2, sinisterProperty.getLocation());
//            pst.setString(3, sinisterProperty.getStatus_sinister());
//            pst.setLong(4, sinisterProperty.getId());
//            pst.executeUpdate();
//
//            pst = connection.prepareStatement(updatePropertyQuery);
//            pst.setString(1, sinisterProperty.getType_degat());
//            pst.setString(2, sinisterProperty.getDescription_degat());
//            pst.setLong(3, sinisterProperty.getId());
//            pst.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException("Error updating sinister property: " + sinisterProperty.getId(), e);
//        }
    }
    public void updateStatus(long id, String status_sinister) throws SQLException {
        String sql = "UPDATE sinister SET status_sinister = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status_sinister);
            statement.setLong(2, id);
            statement.executeUpdate();
        }
    }

    public List<SinisterProperty> getByUserId(int userId) throws SQLException {
        String query = "SELECT sp.id, s.status_sinister, s.date_sinister " + // Added space here
                "FROM sinister_property sp " +
                "INNER JOIN sinister s ON sp.id = s.id " +
                "WHERE s.sinister_user_id = ?";

        List<SinisterProperty> properties = new ArrayList<>();
        try {
            pst = connection.prepareStatement(query);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                SinisterProperty property = new SinisterProperty(
                        rs.getLong("id"),
                        rs.getDate("date_sinister").toLocalDate(),
                        rs.getString("status_sinister")


                );
                properties.add(property);
                System.out.println(property);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching sinister properties by User ID: " + userId, e);
        }
        return properties;
    }
    public SinisterProperty getById(long id) throws SQLException {
        String query = "SELECT * FROM sinister_property WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {

                    LocalDate dateSinister = rs.getDate("date_sinister").toLocalDate();
                    String location = rs.getString("location");
                    String typeDegat = rs.getString("type_degat");
                    String descriptionDegat = rs.getString("description_degat");

                    return new SinisterProperty(id, dateSinister, location, typeDegat, descriptionDegat);
                } else {
                    return null;
                }
            }
        }
    }

    public SinisterProperty getByIdRep(long id) throws SQLException {
        String query = "SELECT sp.id, sp.type_degat, sp.description_degat, s.date_sinister, s.location, r.sinister_rapport_id " +
                "FROM sinister_property sp " +
                "INNER JOIN sinister s ON sp.id = s.id " +
                "INNER JOIN rapport r ON sp.id = r.sinister_rapport_id " +
                "WHERE sp.id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    LocalDate dateSinister = rs.getDate("date_sinister").toLocalDate();
                    String location = rs.getString("location");
                    String typeDegat = rs.getString("type_degat");
                    String descriptionDegat = rs.getString("description_degat");


                    return new SinisterProperty(id, dateSinister, location, typeDegat, descriptionDegat);
                } else {
                    return null;
                }
            }
        }
    }



    @Override
    public List<SinisterProperty> getAll() throws SQLException {
        String query = "SELECT sp.id,sp.type_degat, sp.description_degat, s.date_sinister, s.location ,s.status_sinister " +
                "FROM sinister_property sp " +
                "INNER JOIN sinister s ON sp.id = s.id";
        List<SinisterProperty> properties = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                SinisterProperty property = new SinisterProperty(
                        rs.getLong("id"),

                        rs.getDate("date_sinister").toLocalDate(),

                        rs.getString("location"),
                        rs.getString("status_sinister"),
                        rs.getString("type_degat"),
                        rs.getString("description_degat")
                );
                properties.add(property);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching sinister properties", e);
        }
        return properties;
    }


    @Override
    public void delete(int id) throws SQLException {
        String vehicleRequestSql = "DELETE FROM sinister_property WHERE id = ?";
        String insuranceRequestSql = "DELETE FROM sinister WHERE id = ?";

        PreparedStatement vs = connection.prepareStatement(vehicleRequestSql);
        PreparedStatement ss = connection.prepareStatement(insuranceRequestSql);
        vs.setInt(1, id);
        vs.executeUpdate();
        ss.setInt(1, id);
        ss.executeUpdate();
    }

    @Override
    public SinisterProperty getById(int id) throws SQLException {
        String query = "SELECT sp.id , sp.type_degat, sp.description_degat, s.date_sinister, s.location " +
                "FROM sinister_property sp " +
                "INNER JOIN sinister s ON sp.id = s.id " +
                "WHERE sp.id = ?";
        try {
            pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new SinisterProperty(
                        rs.getLong("id"),
                        rs.getDate("date_sinister").toLocalDate(),
                        rs.getString("location"),
                        rs.getString("type_degat"),
                        rs.getString("description_degat")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching sinister property by ID: " + id, e);
        }
        return null;
    }
}
