package org.example.services;

import org.example.models.Commentaire;
import org.example.utils.MyDatabase;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public  class CommentServices implements IComment<Commentaire>{
    private final Connection connection;
    private List<Commentaire> comments;

    public Connection getConnection() {
        return connection;
    }
    //initilize connection
    @Override
    public Commentaire getById(int id) throws SQLException {
        String query = "SELECT * FROM comment WHERE article_id = ?"; // Selecting comment by its ID
        ResultSet rs = null;
        Commentaire comment = null;
        try (PreparedStatement ps = MyDatabase.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                comment = new Commentaire();
                comment.setId(rs.getInt("id"));
                comment.setDescription(rs.getString("description"));
                comment.setRate(rs.getInt("rate"));
                // Assuming there might be other attributes to set
                // Add additional setters if needed
            }
        } catch (SQLException ex) {
            System.err.println("Error executing get by id query: " + ex.getMessage());
            throw ex; // Rethrow the exception for handling at a higher level
        } finally {
            // Close ResultSet if not null
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Error closing ResultSet: " + e.getMessage());
                }
            }
        }

        return comment;
    }

    @Override
    public void delete(int id) throws  SQLException{
        String sql = "delete from comment where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();

    }

    @Override
    public List<Commentaire> getAll() throws SQLException {
        List<Commentaire> comments = new ArrayList<>();
        String sql = "SELECT * FROM comment";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Commentaire comment = new Commentaire();
            comment.setId(rs.getInt("id"));
            System.out.println("id: " + rs.getInt("id"));
            comment.setDescription(rs.getString("description"));
            comment.setRate(rs.getInt("rate"));
            comments.add(comment);
        }
        return comments;
    }




    public CommentServices() {
        connection = MyDatabase.getInstance().getConnection();
    }



    @Override
    public void add(Commentaire commentaire ) throws SQLException {
        String sql = "INSERT INTO comment (id,description, rate) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, commentaire.getId());
        statement.setString(2, commentaire.getDescription());
        statement.setInt(3, commentaire.getRate());
        statement.executeUpdate();

    }

    @Override
    public void addC(Commentaire commentaire, String replyDescription) throws SQLException {
        String sql = "INSERT INTO comment (description, rate, article_id) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, replyDescription);    // use replyDescription here
        statement.setInt(2, commentaire.getRate());
        statement.setInt(3, commentaire.getArticle_id());
        statement.executeUpdate();
    }

    public void update(Commentaire comment) {
        String sql = "UPDATE comment SET description = ?, rate = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, comment.getDescription());
            statement.setInt(2, comment.getRate());
            statement.setInt(3, comment.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public List<Commentaire> getAllCommentsByArticleId(int selectedArticleId) {
        List<Commentaire> comments = new ArrayList<>();
        String sql = "SELECT * FROM comment WHERE article_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, selectedArticleId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Commentaire comment = new Commentaire();
                comment.setId(rs.getInt("id"));
                comment.setDescription(rs.getString("description"));
                comment.setRate(rs.getInt("rate"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
}
