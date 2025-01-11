package org.example.services;

import org.example.models.ContratHabitat;
import org.example.models.ContratVie;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContratHabitatService implements IService<ContratHabitat> {

    private Connection connection;

    public ContratHabitatService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(ContratHabitat contratHabitat) throws SQLException {
        String contratAssuranceSql = "INSERT INTO contrat_assurance (request_id,type) " +
                "VALUES (  '" + contratHabitat.getRequest().getId() + "', 'ContartHabitat')";

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

        String contratHabitatSql = "INSERT INTO contrat_habitat (id,date_debut,date_fin,description,matricule_agent,prix) " +
                "VALUES (" + contratAssuanceId + ", '" + contratHabitat.getDateDebut() + "', '" + contratHabitat.getDateFin() + "', '" + contratHabitat.getDescription() + "', '" +
                contratHabitat.getMatriculeAgent() + "', '" + contratHabitat.getPrix() + "')";

        statement.executeUpdate(contratHabitatSql);
        System.out.println("Contrat habitat SQL: " + contratHabitatSql);
        statement.close();

    }


    @Override
    public void update(ContratHabitat contratHabitat) throws SQLException {
        String sql = "UPDATE contrat_habitat SET  date_debut = ?, date_fin = ?, description = ?, matricule_agent = ?, prix = ?  WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(contratHabitat.getDateDebut()));
        preparedStatement.setString(2, String.valueOf(contratHabitat.getDateFin()));
        preparedStatement.setString(3, contratHabitat.getDescription());
        preparedStatement.setString(4, contratHabitat.getMatriculeAgent());
        preparedStatement.setString(5, contratHabitat.getPrix());
        preparedStatement.setInt(6, contratHabitat.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    @Override
    public void delete(int id) throws SQLException {
        String contratHabitatSql = "DELETE FROM contrat_assurance WHERE id = ?";
        String contratAssuranceSql = "DELETE FROM contrat_habitat WHERE id = ?";

        PreparedStatement contratHabitatStatement = connection.prepareStatement(contratHabitatSql);
        PreparedStatement contratAssuranceStatement = connection.prepareStatement(contratAssuranceSql);
        contratHabitatStatement.setInt(1, id);
        contratHabitatStatement.executeUpdate();

        contratAssuranceStatement.setInt(1, id);
        contratAssuranceStatement.executeUpdate();

    }

    @Override
    public List<ContratHabitat> getAll() throws SQLException {
        String sql = "select * from contrat_habitat ";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<ContratHabitat> contratHabitats = new ArrayList<>();
        while (rs.next()) {
            ContratHabitat lr = new ContratHabitat();
            lr.setId(rs.getInt("id"));
            lr.setDateDebut(LocalDate.parse(rs.getString("date_debut")));
            lr.setDateFin(LocalDate.parse(rs.getString("date_fin")));
            lr.setDescription(rs.getString("description"));
            lr.setMatriculeAgent(rs.getString("matricule_agent"));
            lr.setPrix(rs.getString("prix"));

            contratHabitats.add(lr);
        }
        return contratHabitats;    }

    @Override
    public ContratHabitat getById(int id) throws SQLException {
        return null;
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
        System.out.println(insuranceIds.size());
        return insuranceIds;
    }
    public List<ContratHabitat> getAllContratsVieByInsuranceIds(List<Integer> insuranceIds) throws SQLException {
        // Construction de la clause IN dynamique en fonction du nombre d'identifiants
        StringBuilder sqlBuilder = new StringBuilder("SELECT cv.* FROM contrat_assurance ca " +
                "INNER JOIN contrat_habitat cv ON ca.id = cv.id " +
                "WHERE ca.request_id IN (");
        for (int i = 0; i < insuranceIds.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("?");
        }
        sqlBuilder.append(")");

        List<ContratHabitat> contratsVie = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString())) {
            // Remplissage des paramètres IN avec les identifiants
            for (int i = 0; i < insuranceIds.size(); i++) {
                statement.setInt(i + 1, insuranceIds.get(i)); // Les paramètres commencent à l'index 1
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ContratHabitat contratVie = new ContratHabitat();
                    contratVie.setId(rs.getInt("id")); // Remplacez "id" par le nom de l'identifiant dans la table contrat_vie
                    contratVie.setDateDebut(LocalDate.parse(rs.getString("date_debut")));
                    contratVie.setDateFin(LocalDate.parse(rs.getString("date_fin")));
                    contratVie.setDescription(rs.getString("description"));
                    contratVie.setMatriculeAgent(rs.getString("matricule_agent"));
                    contratVie.setPrix(rs.getString("prix"));

                    // Complétez ici la récupération des autres attributs de ContratVie

                    contratsVie.add(contratVie);
                }
            }
        }
        return contratsVie;
    }



}