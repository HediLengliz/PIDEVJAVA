package org.example.models;

import java.time.LocalDate;

public class ContratVehicule extends ContratAssurance {

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;
    private String matriculeAgent;
    private String prix;
    public ContratVehicule(){};

    public ContratVehicule(int id, InsuranceRequest request,LocalDate dateDebut, LocalDate dateFin, String description, String matriculeAgent, String prix){
        super(id, request);
        this.dateDebut=dateDebut;
        this.dateFin=dateFin;
        this.description=description;
        this.matriculeAgent=matriculeAgent;
        this.prix=prix;
    }
    public ContratVehicule(InsuranceRequest request,LocalDate dateDebut, LocalDate dateFin, String description, String matriculeAgent, String prix){
        super(request);
        this.dateDebut=dateDebut;
        this.dateFin=dateFin;
        this.description=description;
        this.matriculeAgent=matriculeAgent;
        this.prix=prix;
    }
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMatriculeAgent() {
        return matriculeAgent;
    }

    public void setMatriculeAgent(String matriculeAgent) {
        this.matriculeAgent = matriculeAgent;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }
}

