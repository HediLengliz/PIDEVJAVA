package org.example.services;

import org.example.models.Article;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class articleservices implements IARService<Article> {
    private Connection connection;
    private Statement ste;
    private PreparedStatement pst;
    public List<Article> getStatus_sinister(String categorie) throws SQLException {
        String query = "SELECT * FROM Article a WHERE a.categorie = ?";
        List<Article> properties = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, categorie);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
               Article property= new Article(
                       rs.getInt("id"),
                       rs.getString("title"),
                       rs.getString("description"),
                       rs.getString("authorname"),
                       rs.getDate("datepub").toLocalDate(),
                       rs.getString("categorie"),
                       rs.getString("image")
                );
                properties.add(property);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching sinister properties by status: " + categorie, e);
        }
        return properties;
    }
    @Override
    public Article getById(int id) throws SQLException {
        String query = "SELECT * FROM article WHERE id = ?";
        ResultSet rs = null;
        Article article = null;

        try (PreparedStatement ps = MyDatabase.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                article = new Article();
                article.setId(rs.getInt("id"));
                article.setTitle(rs.getString("title"));
                article.setDescription(rs.getString("description"));
                article.setAuthorname(rs.getString("authorname"));
                article.setDatepub(rs.getDate("datepub").toLocalDate());
                article.setCategorie(rs.getString("categorie"));
                article.setImage(rs.getString("image"));
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

        return article;
    }



    public articleservices() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Article article) throws SQLException {
        String sql = "INSERT INTO article (title, description, datepub, authorname, categorie,image) VALUES (?, ?, ?, ?, ?,?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, article.getTitle());
        statement.setString(2, article.getDescription());
        // Assuming datepub is of type LocalDate, convert it to java.sql.Date
        statement.setDate(3, java.sql.Date.valueOf(article.getDatepub()));
        statement.setString(4, article.getAuthorname());
        statement.setString(5, article.getCategorie());
        statement.setString(6, article.getImage());

        statement.executeUpdate();
    }


    @Override
    public void update(Article article) throws SQLException {
        System.out.println(article.getCategorie());
        String sql = "UPDATE article SET title = ?, description = ?, datepub = ?, authorname = ? , categorie = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, article.getTitle());
        ps.setString(2, article.getDescription());
        ps.setObject(3, article.getDatepub()); // Assuming birth is a LocalDateTime
        ps.setString(4, article.getAuthorname());
        ps.setString(5, article.getCategorie());
        ps.setInt(6, article.getId());

        ps.executeUpdate();
    }



    public void delete(int id) throws SQLException {
//        // Enable ON DELETE CASCADE for the foreign key constraint
//        String alterConstraintSql = "ALTER TABLE comment " +
//                "ADD CONSTRAINT article_id " +
//                "FOREIGN KEY (article_id) " +
//                "REFERENCES article(id) " +
//                "ON DELETE CASCADE";
//        PreparedStatement alterPs = connection.prepareStatement(alterConstraintSql);
//        alterPs.executeUpdate();
//        alterPs.close();

        // Delete the row from the 'article' table
        String deleteArticleSql = "DELETE FROM article WHERE id = ?";
        PreparedStatement deletePs = connection.prepareStatement(deleteArticleSql);
        deletePs.setInt(1, id);
        deletePs.executeUpdate();
        deletePs.close();
    }

    @Override
    public List<Article> getAll() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article"; // Adjust table name as needed
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Create Article objects and add them to the list
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("authorname"),
                        rs.getDate("datepub").toLocalDate(),
                        rs.getString("categorie"),
                        rs.getString("image")
                );
                articles.add(article);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return articles;
    }



    public List<Article> searchArticle(String title) {
        //search article
        String requete = "select * from article where title=?";
        ResultSet rs = null;
        List<Article> list = new ArrayList();
        try {
            PreparedStatement ps = MyDatabase.getInstance().getConnection()
                    .prepareStatement(requete);
            ps.setString(1, title);
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

        }
        Article a = new Article();
        try {
            while (rs.next()) {
                a.setTitle(rs.getString("title"));
                a.setDescription(rs.getString("description"));
                a.setAuthorname(rs.getString("authorname"));
                a.setDatepub(rs.getDate("datepub").toLocalDate());
                a.setCategorie(rs.getString("categorie"));
                a.setImage(rs.getString("image"));
                list.add(new Article(rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5).toLocalDate(), rs.getString(6), rs.getString(7)));
            }
        } catch (SQLException exc) {
            System.err.println(exc.getMessage());
        }
        return list;
    }
    public List<Article> getSortedArticlesByID() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article ORDER BY id DESC ";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("authorname"),
                        rs.getDate("datepub").toLocalDate(),
                        rs.getString("categorie"),
                        rs.getString("image")
                );
                articles.add(article);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return articles;
    }
    public List<Article> getSortedArticlesByTitle() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article ORDER BY title DESC ";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("authorname"),
                        rs.getDate("datepub").toLocalDate(),
                        rs.getString("categorie"),
                        rs.getString("image")
                );
                articles.add(article);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return articles;

    }
    public List<Article> getSortedArticlesByCategorie() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article ORDER BY categorie DESC ";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("authorname"),
                        rs.getDate("datepub").toLocalDate(),
                        rs.getString("categorie"),
                        rs.getString("image")
                );
                articles.add(article);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return articles;
    }

//    public List<Blog>sortBlog() {
//        List<Blog> list=new ArrayList<>();
//        try{
//            String requete="select * from article order by date_time desc";
//            PreparedStatement ps = MaConnexion.getInstance().getCnx()
//                    .prepareStatement(requete);
//            ResultSet rs=ps.executeQuery();
//            while(rs.next()){
//                list.add(new Blog(rs.getInt(1), rs.getString(2),rs.getDate(3),rs.getString(4),rs.getString(5)));
//            }
//        }
//        catch(SQLException ex){
//            System.err.println(ex.getMessage());
//        }
//        return list;
//    }

}
