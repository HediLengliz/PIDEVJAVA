package org.example.services;

import java.sql.SQLException;
import java.util.List;

public interface IARService<Article> {
        void add(Article article) throws SQLException;

        void update(Article articleArticle) throws SQLException;

        void delete(int id) throws SQLException;

        List<Article> getAll() throws SQLException;

        Article getById(int id) throws  SQLException;
        List<Article> searchArticle(String  title) throws  Exception;
    }

