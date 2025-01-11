package org.example.services;

import org.example.models.Commentaire;

import java.sql.SQLException;
import java.util.List;

public interface IComment<Comment> {
    void add(Comment comment)throws SQLException;
    void addC(Comment comment,String replyDescription)throws SQLException;

    void update(Comment Commentaire)throws SQLException;


    Commentaire getById(int id)throws SQLException;

    void delete(int id)throws SQLException;

    List<Comment> getAll()throws SQLException;

}
