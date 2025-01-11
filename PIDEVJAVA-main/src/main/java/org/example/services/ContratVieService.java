package org.example.services;

import org.example.models.ContratAssurance;
import org.example.models.ContratVie;

import org.example.models.InsuranceRequest;
import org.example.models.User;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContratVieService implements IService<ContratVie> {

    private Connection connection;

    public ContratVieService() {
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void add(ContratVie contratVie) throws SQLException {
        String contratAssuranceSql = "INSERT INTO contrat_assurance (request_id,type) " +
                "VALUES (  '" + contratVie.getRequest().getId() + "', 'ContartVie')";

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

        String contratVieSql = "INSERT INTO contrat_vie (id,date_debut,date_fin,description,matricule_agent,prix) " +
                "VALUES (" + contratAssuanceId + ", '" + contratVie.getDateDebut() + "', '" + contratVie.getDateFin() + "', '" + contratVie.getDescription() + "', '" +
                contratVie.getMatriculeAgent() + "', '" + contratVie.getPrix() + "')";

        statement.executeUpdate(contratVieSql);
        System.out.println("Contrat vie SQL: " + contratVieSql);
        statement.close();

    }


    @Override
    public void update(ContratVie contratVie) throws SQLException {
        String sql = "UPDATE contrat_vie SET  date_debut = ?, date_fin = ?, description = ?, matricule_agent = ?, prix = ?  WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(contratVie.getDateDebut()));
        preparedStatement.setString(2, String.valueOf(contratVie.getDateFin()));
        preparedStatement.setString(3, contratVie.getDescription());
        preparedStatement.setString(4, contratVie.getMatriculeAgent());
        preparedStatement.setString(5, contratVie.getPrix());
        preparedStatement.setInt(6, contratVie.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    public List<ContratVie> displayContratsByRequest(int userId, String typeInsurance) throws SQLException {
        // Récupérer tous les identifiants d'assurance associés à l'utilisateur et au type d'assurance
        List<Integer> insuranceIds = getAllInsuranceIdsByUserIdAndType(userId, typeInsurance);

        // Récupérer les contrats de vie associés à ces identifiants
        List<ContratVie> contratsVie = getAllContratsVieByInsuranceIds(insuranceIds);

        return contratsVie;
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
        return insuranceIds;
    }
    public List<ContratVie> getAllContratsVieByInsuranceIds(List<Integer> insuranceIds) throws SQLException {
        // Construction de la clause IN dynamique en fonction du nombre d'identifiants
        StringBuilder sqlBuilder = new StringBuilder("SELECT cv.* FROM contrat_assurance ca " +
                "INNER JOIN contrat_vie cv ON ca.id = cv.id " +
                "WHERE ca.request_id IN (");
        for (int i = 0; i < insuranceIds.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("?");
        }
        sqlBuilder.append(")");

        List<ContratVie> contratsVie = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString())) {
            // Remplissage des paramètres IN avec les identifiants
            System.out.println("aaaaaaaaaaa -->"+ insuranceIds.size());
            for (int i = 0; i < insuranceIds.size(); i++) {
                System.out.println(insuranceIds.get(i));
                statement.setInt(i + 1, insuranceIds.get(i)); // Les paramètres commencent à l'index 1
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ContratVie contratVie = new ContratVie();
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




    @Override
    public void delete(int id) throws SQLException {
        String contratVieSql = "DELETE FROM contrat_assurance WHERE id = ?";
        String contratAssuranceSql = "DELETE FROM contrat_vie WHERE id = ?";

        PreparedStatement contratVieStatement = connection.prepareStatement(contratVieSql);
        PreparedStatement contratAssuranceStatement = connection.prepareStatement(contratAssuranceSql);
        contratVieStatement.setInt(1, id);
        contratVieStatement.executeUpdate();

        contratAssuranceStatement.setInt(1, id);
        contratAssuranceStatement.executeUpdate();

    }

    @Override
    public List<ContratVie> getAll() throws SQLException {
        String sql = "select * from contrat_vie ";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<ContratVie> contratVies = new ArrayList<>();
        while (rs.next()) {
            ContratVie lr = new ContratVie();
            lr.setId(rs.getInt("id"));
            lr.setDateDebut(LocalDate.parse(rs.getString("date_debut")));
            lr.setDateFin(LocalDate.parse(rs.getString("date_fin")));
            lr.setDescription(rs.getString("description"));
            lr.setMatriculeAgent(rs.getString("matricule_agent"));
            lr.setPrix(rs.getString("prix"));

            contratVies.add(lr);
        }
        return contratVies;
    }

    @Override
    public ContratVie getById(int id) throws SQLException {
        return null;
    }

}