package org.example.models;
import java.time.LocalDateTime;

public class Reclamation {
    private Integer id;

    private String title;

    private String description;

    private User user;
    private LocalDateTime dateReclamation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateReclamation() {
        return dateReclamation;
    }

    public void setDateReclamation(LocalDateTime dateReclamation)
    {
        this.dateReclamation = dateReclamation;
    }
    public Reclamation() {
        // Default constructor
    }
    public Reclamation(String title, String description, User user) {

        this.title = title;
        this.description = description;
        this.user = user;
        this.dateReclamation = LocalDateTime.now(); // Set to current date and time
    }
}