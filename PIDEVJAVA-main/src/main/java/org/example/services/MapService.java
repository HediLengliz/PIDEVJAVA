package org.example.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.example.models.Remorqueur;
import org.example.utils.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MapService implements IService<Remorqueur> {
    private Connection connection;
    private Statement ste;
    private PreparedStatement pst;
    public MapService(){
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Remorqueur remorqueur) throws SQLException {
        // Implement add method if needed
    }

    @Override
    public void update(Remorqueur remorqueur) throws SQLException {
        // Implement update method if needed
    }

    @Override
    public void delete(int id) throws SQLException {
        // Implement delete method if needed
    }

    @Override
    public List<Remorqueur> getAll() throws SQLException {
        return fetchRemorqueurs();
    }

    @Override
    public Remorqueur getById(int id) throws SQLException {
        // Implement getById method if needed
        return null;
    }

   public List<Remorqueur> fetchRemorqueurs() {
        List<Remorqueur> remorqueurs = new ArrayList<>();
        String query = "SELECT id, first_name, last_name, phone_number, lattitude, longuitude FROM remorqueur";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String phoneNumber = resultSet.getString("phone_number");
                double latitude = resultSet.getDouble("lattitude");
                double longitude = resultSet.getDouble("longuitude");
                remorqueurs.add(new Remorqueur(id, firstName, lastName, phoneNumber, latitude, longitude));
                System.out.println(remorqueurs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return remorqueurs;
    }


}
