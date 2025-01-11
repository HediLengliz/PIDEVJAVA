package org.example.models;

import java.time.LocalDate;

public class Sinister {

    private  Long id;
    private LocalDate date_sinister;
    private String location;

    public Sinister(Long id, LocalDate date_sinister, String status_sinister) {
        this.id = id;
        this.date_sinister = date_sinister;
        this.status_sinister = status_sinister;
    }

    private User sinister_user_id;
    private String status_sinister = "en_cours";

    public Sinister(Long id, LocalDate date_sinister, String location, User sinister_user_id, String status_sinister) {
        this.id = id;
        this.date_sinister = date_sinister;
        this.location = location;
        this.sinister_user_id = sinister_user_id;
        this.status_sinister = status_sinister;
    }
    public Sinister(Long id, LocalDate date_sinister, String location,  String status_sinister) {
        this.id = id;
        this.date_sinister = date_sinister;
        this.location = location;

        this.status_sinister = status_sinister;
    }
    public Sinister( LocalDate date_sinister, String location) {

        this.date_sinister = date_sinister;
        this.location = location;

    }


    public Sinister() {

    }

    public Sinister(long id, LocalDate date_sinister, String location) {
        this.id=id;
        this.date_sinister = date_sinister;
        this.location = location;
    }



    public Long getId() {
        return id != null ? id : 0L; // Return 0L if id is null
    }


    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate_sinister() {
        return date_sinister;
    }

    public void setDate_sinister(LocalDate date_sinister) {
        this.date_sinister = date_sinister;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getSinister_user_id() {
        return sinister_user_id;
    }

    public void setSinister_user_id(User sinister_user_id) {
        this.sinister_user_id = sinister_user_id;
    }

    public String getStatus_sinister() {
        return status_sinister;
    }

    public void setStatus_sinister(String status_sinister) {
        this.status_sinister = status_sinister;
    }

    @Override
    public String toString() {
        // Customize this method based on how you want to represent Sinister as a string
        return id != null ? id + " - " + location + " - " + (date_sinister != null ? date_sinister.toString() : "") : "New Sinister";
    }
}
