package org.example.services;

import org.example.models.ContratHabitat;
import org.example.models.ContratVehicule;
import org.example.models.ContratVie;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContratVehiculeService implements IService<ContratVehicule> {

    private Connection connection;

    public ContratVehiculeService() {
        connection = MyDatabase.getInstance().getConnection();
    }





    public List<Integer> getAllInsuranceIdsByUserIdAndType(int userId, String typeInsurance) throws SQLException {
        String sql = "SELECT id FROM insurance_request WHERE user_id = ? AND type_insurance = ?";
        List<Integer> insuranceIds = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, typeInsurance);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    insuranceIds.add(rs.getInt("id"));
                }
            }
        }
        System.out.println("vehicule  "+insuranceIds.size());

        return insuranceIds;
    }
    public List<ContratVehicule> getAllContratsVieByInsuranceIds(List<Integer> insuranceIds) throws SQLException {
        // Construction de la clause IN dynamique en fonction du nombre d'identifiants
        StringBuilder sqlBuilder = new StringBuilder("SELECT cv.* FROM contrat_assurance ca " +
                "INNER JOIN contrat_vehicule cv ON ca.id = cv.id " +
                "WHERE ca.request_id IN (");
        for (int i = 0; i < insuranceIds.size(); i++)
        {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("?");
        }
        sqlBuilder.append(")");

        List<ContratVehicule> contratsVehicule = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString())) {
            // Remplissage des paramètres IN avec les identifiants
            for (int i = 0; i < insuranceIds.size(); i++) {
                statement.setInt(i + 1, insuranceIds.get(i)); // Les paramètres commencent à l'index 1
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ContratVehicule contratVie = new ContratVehicule();
                    contratVie.setId(rs.getInt("id")); // Remplacez "id" par le nom de l'identifiant dans la table contrat_vie
                    contratVie.setDateDebut(LocalDate.parse(rs.getString("date_debut")));
                    contratVie.setDateFin(LocalDate.parse(rs.getString("date_fin")));
                    contratVie.setDescription(rs.getString("description"));
                    contratVie.setMatriculeAgent(rs.getString("matricule_agent"));
                    contratVie.setPrix(rs.getString("prix"));

                    // Complétez ici la récupération des autres attributs de ContratVie

                    contratsVehicule.add(contratVie);
                }
            }
        }
        return contratsVehicule;
    }



    @Override
    public void add(ContratVehicule contratVehicule) throws SQLException {
        String contratAssuranceSql = "INSERT INTO contrat_assurance (request_id,type) " +
                "VALUES (  '" + contratVehicule.getRequest().getId() + "', 'ContartVehicule')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(contratAssuranceSql, Statement.RETURN_GENERATED_KEYS);
        System.out.println("blalall" + contratAssuranceSql);
        ResultSet generatedKeys = statement.getGeneratedKeys();
        int contratAssuanceId = -1;
        if (generatedKeys.next()) {
            contratAssuanceId = generatedKeys.getInt(1);
        } else {
            throw new SQLException("Failed to retrieve the generated id for contrat_assurance");
        }

        String contratVehiculeSql = "INSERT INTO contrat_vehicule (id,date_debut,date_fin,description,matricule_agent,prix) " +
                "VALUES (" + contratAssuanceId + ", '" + contratVehicule.getDateDebut() + "', '" + contratVehicule.getDateFin() + "', '" + contratVehicule.getDescription() + "', '" +
                contratVehicule.getMatriculeAgent() + "', '" + contratVehicule.getPrix() + "')";

        statement.executeUpdate(contratVehiculeSql);
        System.out.println("Contrat vehicule SQL: " + contratVehiculeSql);
        statement.close();

    }


    @Override
    public void update(ContratVehicule contratVehicule) throws SQLException {
        String sql = "UPDATE contrat_vehicule SET  date_debut = ?, date_fin = ?, description = ?, matricule_agent = ?, prix = ?  WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(contratVehicule.getDateDebut()));
        preparedStatement.setString(2, String.valueOf(contratVehicule.getDateFin()));
        preparedStatement.setString(3, contratVehicule.getDescription());
        preparedStatement.setString(4, contratVehicule.getMatriculeAgent());
        preparedStatement.setString(5, contratVehicule.getPrix());
        preparedStatement.setInt(6, contratVehicule.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    @Override
    public void delete(int id) throws SQLException {
        String contratVehiculeSql = "DELETE FROM contrat_assurance WHERE id = ?";
        String contratAssuranceSql = "DELETE FROM contrat_vehicule WHERE id = ?";

        PreparedStatement contratVehiculeStatement = connection.prepareStatement(contratVehiculeSql);
        PreparedStatement contratAssuranceStatement = connection.prepareStatement(contratAssuranceSql);
        contratVehiculeStatement.setInt(1, id);
        contratVehiculeStatement.executeUpdate();

        contratAssuranceStatement.setInt(1, id);
        contratAssuranceStatement.executeUpdate();

    }

    @Override
    public List<ContratVehicule> getAll() throws SQLException {
        String sql = "select * from contrat_vehicule";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<ContratVehicule> contratVehicules = new ArrayList<>();
        while (rs.next()) {
            ContratVehicule lr = new ContratVehicule();
            lr.setId(rs.getInt("id"));
            lr.setDateDebut(LocalDate.parse(rs.getString("date_debut")));
            lr.setDateFin(LocalDate.parse(rs.getString("date_fin")));
            lr.setDescription(rs.getString("description"));
            lr.setMatriculeAgent(rs.getString("matricule_agent"));
            lr.setPrix(rs.getString("prix"));

            contratVehicules.add(lr);
        }
        return contratVehicules;    }

    @Override
    public ContratVehicule getById(int id) throws SQLException {
        return null;
    }

}