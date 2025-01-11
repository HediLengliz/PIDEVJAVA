package org.example.services;

import org.example.models.SinisterProperty;
import org.example.models.SinisterVehicle;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SinisterVehicleService implements IService<SinisterVehicle> {
    private Connection connection;
    private PreparedStatement pst;
    public Connection getConnection() {
        return connection;
    }


    public SinisterVehicleService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    public List<SinisterVehicle> getByUserId(int userId) throws SQLException {
        String query = "SELECT s.id,s.status_sinister, s.date_sinister" +
                "FROM sinister_vehicule sp " +
                "INNER JOIN sinister s ON sp.id = s.id " +
                "WHERE s.sinister_user_id = ?";
        List<SinisterVehicle> properties = new ArrayList<>();
        try {
            pst = connection.prepareStatement(query);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                SinisterVehicle property = new SinisterVehicle(
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
    @Override
    public void add(SinisterVehicle sinisterVehicle) throws SQLException {
        String query = "INSERT INTO sinister (date_sinister, location, sinister_user_id, status_sinister) VALUES (?, ?, ?, ?)";
        try {

            pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setDate(1, java.sql.Date.valueOf(sinisterVehicle.getDate_sinister()));
            pst.setString(2, sinisterVehicle.getLocation());
            pst.setInt(3, 1); // Assuming 1 is the ID of the user
            pst.setString(4, sinisterVehicle.getStatus_sinister());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            long id;
            if (rs.next()) {
                id = rs.getLong(1);
            } else {
                throw new SQLException("Failed to retrieve generated ID for Sinister");
            }


            query = "INSERT INTO sinister_vehicle (id, nom_conducteur_a, nom_conducteur_b, prenom_conducteur_b, prenom_conducteur_a, adresse_conducteur_a, adresse_conducteur_b, num_permis_a, num_permis_b, delivre_a, delivre_b, num_contrat_a, num_contrat_b, marque_vehicule_a, marque_vehicule_b, immatriculation_a, immatriculation_b, bvehicule_assure_par, agence,image_name) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setLong(1, id);  // Using the generated sinister ID
            pst.setString(2, sinisterVehicle.getNom_conducteur_a());
            pst.setString(3, sinisterVehicle.getNom_conducteur_b());
            pst.setString(4, sinisterVehicle.getPrenom_conducteur_b());
            pst.setString(5, sinisterVehicle.getPrenom_conducteur_a());
            pst.setString(6, sinisterVehicle.getAdresse_conducteur_a());
            pst.setString(7, sinisterVehicle.getAdresse_conducteur_b());
            pst.setString(8, sinisterVehicle.getNum_permis_a());
            pst.setString(9, sinisterVehicle.getNum_permis_b());
            pst.setDate(10, java.sql.Date.valueOf(sinisterVehicle.getDelivre_a()));
            pst.setDate(11, java.sql.Date.valueOf(sinisterVehicle.getDelivre_b()));
            pst.setString(12, sinisterVehicle.getNum_contrat_a());
            pst.setString(13, sinisterVehicle.getNum_contrat_b());
            pst.setString(14, sinisterVehicle.getMarque_vehicule_a());
            pst.setString(15, sinisterVehicle.getMarque_vehicule_b());
            pst.setString(16, sinisterVehicle.getImmatriculation_a());
            pst.setString(17, sinisterVehicle.getImmatriculation_b());
            pst.setString(18, sinisterVehicle.getBvehicule_assure_par());
            pst.setString(19, sinisterVehicle.getAgence());
            pst.setString(20, sinisterVehicle.getImage_name());
            pst.executeUpdate();


            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                sinisterVehicle.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(SinisterVehicle sinisterVehicle) throws SQLException {
    }

    @Override
    public void delete(int id) throws SQLException {
        String vsSql = "DELETE FROM sinister_vehicle WHERE id = ?";
        String stSql = "DELETE FROM sinister WHERE id = ?";

        PreparedStatement vs = connection.prepareStatement(vsSql);
        PreparedStatement ss = connection.prepareStatement(stSql);
        vs.setInt(1, id);
        vs.executeUpdate();
        ss.setInt(1, id);
        ss.executeUpdate();
    }


    @Override
    public List<SinisterVehicle> getAll() throws SQLException {
        List<SinisterVehicle> sinisterVehicles = new ArrayList<>();
        String query = "SELECT sv.id, s.date_sinister, s.location, s.status_sinister, sv.nom_conducteur_a, sv.nom_conducteur_b, " +
                "sv.prenom_conducteur_b, sv.prenom_conducteur_a, sv.adresse_conducteur_a, sv.adresse_conducteur_b, " +
                "sv.num_permis_a, sv.num_permis_b, sv.delivre_a, sv.delivre_b, sv.num_contrat_a, sv.num_contrat_b, " +
                "sv.marque_vehicule_a, sv.marque_vehicule_b, sv.immatriculation_a, sv.immatriculation_b, " +
                "sv.bvehicule_assure_par, sv.agence, sv.image_name " +
                "FROM sinister_vehicle sv " +
                "INNER JOIN sinister s ON sv.id = s.id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                SinisterVehicle sinisterVehicle = new SinisterVehicle(
                        resultSet.getLong("id"),
                        resultSet.getDate("date_sinister").toLocalDate(),
                        resultSet.getString("location"),
                        resultSet.getString("status_sinister"),
                        resultSet.getString("nom_conducteur_a"),
                        resultSet.getString("nom_conducteur_b"),
                        resultSet.getString("prenom_conducteur_b"),
                        resultSet.getString("prenom_conducteur_a"),
                        resultSet.getString("adresse_conducteur_a"),
                        resultSet.getString("adresse_conducteur_b"),
                        resultSet.getString("num_permis_a"),
                        resultSet.getString("num_permis_b"),
                        resultSet.getDate("delivre_a").toLocalDate(),
                        resultSet.getDate("delivre_b").toLocalDate(),
                        resultSet.getString("num_contrat_a"),
                        resultSet.getString("num_contrat_b"),
                        resultSet.getString("marque_vehicule_a"),
                        resultSet.getString("marque_vehicule_b"),
                        resultSet.getString("immatriculation_a"),
                        resultSet.getString("immatriculation_b"),
                        resultSet.getString("bvehicule_assure_par"),
                        resultSet.getString("agence"),
                        resultSet.getString("image_name")
                );
                sinisterVehicles.add(sinisterVehicle);
            }
        }
        return sinisterVehicles;
    }

    public void updateStatus(long id, String status_sinister) throws SQLException {
        String sql = "UPDATE sinister SET status_sinister = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status_sinister);
            statement.setLong(2, id);
            statement.executeUpdate();
        }
    }
    public List<SinisterVehicle> getStatus_sinister(String status) throws SQLException {
        List<SinisterVehicle> sinisterVehicles = new ArrayList<>();
        String query = "SELECT sv.id, s.date_sinister, s.location, s.status_sinister, sv.nom_conducteur_a, sv.nom_conducteur_b, " +
                "sv.prenom_conducteur_b, sv.prenom_conducteur_a, sv.adresse_conducteur_a, sv.adresse_conducteur_b, " +
                "sv.num_permis_a, sv.num_permis_b, sv.delivre_a, sv.delivre_b, sv.num_contrat_a, sv.num_contrat_b, " +
                "sv.marque_vehicule_a, sv.marque_vehicule_b, sv.immatriculation_a, sv.immatriculation_b, " +
                "sv.bvehicule_assure_par, sv.agence, sv.image_name " +
                "FROM sinister_vehicle sv " +
                "INNER JOIN sinister s ON sv.id = s.id " +
                "WHERE s.status_sinister = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    SinisterVehicle sinisterVehicle = new SinisterVehicle(
                            resultSet.getLong("id"),
                            resultSet.getDate("date_sinister").toLocalDate(),
                            resultSet.getString("location"),
                            resultSet.getString("status_sinister"),
                            resultSet.getString("nom_conducteur_a"),
                            resultSet.getString("nom_conducteur_b"),
                            resultSet.getString("prenom_conducteur_b"),
                            resultSet.getString("prenom_conducteur_a"),
                            resultSet.getString("adresse_conducteur_a"),
                            resultSet.getString("adresse_conducteur_b"),
                            resultSet.getString("num_permis_a"),
                            resultSet.getString("num_permis_b"),
                            resultSet.getDate("delivre_a").toLocalDate(),
                            resultSet.getDate("delivre_b").toLocalDate(),
                            resultSet.getString("num_contrat_a"),
                            resultSet.getString("num_contrat_b"),
                            resultSet.getString("marque_vehicule_a"),
                            resultSet.getString("marque_vehicule_b"),
                            resultSet.getString("immatriculation_a"),
                            resultSet.getString("immatriculation_b"),
                            resultSet.getString("bvehicule_assure_par"),
                            resultSet.getString("agence"),
                            resultSet.getString("image_name")
                    );
                    sinisterVehicles.add(sinisterVehicle);
                }
            }
        }
        return sinisterVehicles;
    }

    @Override
    public SinisterVehicle getById(int id) throws SQLException {
        String query = "SELECT sv.id, s.date_sinister, s.location, s.status_sinister, sv.nom_conducteur_a, sv.nom_conducteur_b, " +
                "sv.prenom_conducteur_b, sv.prenom_conducteur_a, sv.adresse_conducteur_a, sv.adresse_conducteur_b, " +
                "sv.num_permis_a, sv.num_permis_b, sv.delivre_a, sv.delivre_b, sv.num_contrat_a, sv.num_contrat_b, " +
                "sv.marque_vehicule_a, sv.marque_vehicule_b, sv.immatriculation_a, sv.immatriculation_b, " +
                "sv.bvehicule_assure_par, sv.agence, sv.image_name " +
                "FROM sinister_vehicle sv " +
                "INNER JOIN sinister s ON sv.id = s.id " +
                "WHERE sv.id = ?";
        try {
            pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new SinisterVehicle(
                        rs.getLong("id"),
                        rs.getDate("date_sinister").toLocalDate(),
                        rs.getString("location"),
                        rs.getString("status_sinister"),
                        rs.getString("nom_conducteur_a"),
                        rs.getString("nom_conducteur_b"),
                        rs.getString("prenom_conducteur_b"),
                        rs.getString("prenom_conducteur_a"),
                        rs.getString("adresse_conducteur_a"),
                        rs.getString("adresse_conducteur_b"),
                        rs.getString("num_permis_a"),
                        rs.getString("num_permis_b"),
                        rs.getDate("delivre_a").toLocalDate(),
                        rs.getDate("delivre_b").toLocalDate(),
                        rs.getString("num_contrat_a"),
                        rs.getString("num_contrat_b"),
                        rs.getString("marque_vehicule_a"),
                        rs.getString("marque_vehicule_b"),
                        rs.getString("immatriculation_a"),
                        rs.getString("immatriculation_b"),
                        rs.getString("bvehicule_assure_par"),
                        rs.getString("agence"),
                        rs.getString("image_name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching sinister vehicle by ID: " + id, e);
        }
        return null;
    }

}
