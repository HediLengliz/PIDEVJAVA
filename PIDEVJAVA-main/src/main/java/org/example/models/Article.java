package org.example.models;

import java.time.LocalDate;

public class Article {
    private  int id;
    private  String title;
    private  String description;
    private  String authorname;
    private  LocalDate datepub;
    private  String image;
    private  String categorie;

//    private final StringProperty title = new SimpleStringProperty();
//    private final StringProperty description = new SimpleStringProperty();
//    private final StringProperty authorname = new SimpleStringProperty();
//    private final ObjectProperty<LocalDate> datepub = new SimpleObjectProperty<>();
//    private final StringProperty categorie = new SimpleStringProperty();


    public Article() {
    }
    

    public Article(Article article) {
        this.id = article.id;
        this.title = article.title;
        this.description = article.description;
        this.authorname = article.authorname;
        this.datepub = article.datepub;
        this.image = article.image;
        this.categorie = article.categorie;
    }

    public Article(String title, String description, String authorname, LocalDate datepub, String categorie, String image) {
        this.title = title;
        this.description = description;
        this.authorname = authorname;
        this.datepub = datepub;
        this.categorie = categorie;
        this.image = image;
    }

    public Article(String s, String s1, String s2) {
        this.title = s;
        this.description = s1;
        this.authorname = s2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Article(int id, String title, String description, String authorname, LocalDate datepub, String categorie,String image) {
        this.id= id;
        this.title = title;
        this.description = description;
        this.authorname = authorname;
        this.datepub = datepub;
        this.categorie = categorie;
        this.image = image;
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

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public LocalDate getDatepub() {
        return datepub;
    }

    public void setDatepub(LocalDate datepub) {
        this.datepub = datepub;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

}
