package org.example.models;

public class Commentaire {
    private int id;
    private String description;
    private int rate;
    private int article_id;

    public Commentaire(int id, String description, int rate, int article_id) {
        this.id = id;
        this.description = description;
        this.rate = rate;
        this.article_id = article_id;
    }

    public Commentaire() {
    }

    public Commentaire(String s) {
        this.description = s;
        this.rate = 0;

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", rate=" + rate +
                ", article_id=" + article_id +
                '}';
    }

    public void setRating(double rating) {
        //set rating to 2.5/5 stars
        this.rate = (int) rating;
        //2.5 stars
        if (rating - this.rate >= 0.5) {
            this.rate++;
        }
    }
}
