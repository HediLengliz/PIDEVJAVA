package org.example.models;

import java.time.LocalDate;

public class SinisterProperty extends Sinister {
    private String type_degat;
    private String description_degat;
    private Rapport rapport;

    public SinisterProperty(Long id, LocalDate date_sinister, String status_sinister) {
        super(id, date_sinister, status_sinister);
    }

    public void setRapport(Rapport rapport) {
        this.rapport = rapport;
    }

    public Rapport getRapport() {
        return rapport;
    }
    public SinisterProperty(long id, LocalDate date_sinister, String location, User sinister_user_id, String status_sinister, String type_degat, String description_degat) {
        super(id, date_sinister, location, sinister_user_id, status_sinister);
        this.type_degat = type_degat;
        this.description_degat = description_degat;
    }



    public SinisterProperty(LocalDate date_sinister, String location, String type_degat, String description_degat) {
        super(date_sinister, location);
        this.type_degat = type_degat;
        this.description_degat = description_degat;
    }


    public SinisterProperty() {
        super();
        // No-argument constructor
    }

    public SinisterProperty(long id, LocalDate date_sinister, String location, String type_degat, String description_degat) {
        super(id,date_sinister, location);
        this.type_degat = type_degat;
        this.description_degat = description_degat;
    }
    public SinisterProperty(long id, LocalDate date_sinister,String status_sinister, String location, String type_degat, String description_degat) {
        super(id,date_sinister,status_sinister, location);
        this.type_degat = type_degat;
        this.description_degat = description_degat;
    }




    public String getType_degat() {
        return type_degat;
    }

    public void setType_degat(String type_degat) {
        this.type_degat = type_degat;
    }

    public String getDescription_degat() {
        return description_degat;
    }

    public void setDescription_degat(String description_degat) {
        this.description_degat = description_degat;
    }
}