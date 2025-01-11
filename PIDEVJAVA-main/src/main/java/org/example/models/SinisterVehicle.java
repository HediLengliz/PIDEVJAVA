package org.example.models;

import java.io.File;
import java.time.LocalDate;

public class SinisterVehicle extends Sinister {
    private File image_file;
    private String image_name;
    private String nom_conducteur_a;
    private String nom_conducteur_b;
    private String prenom_conducteur_b;
    private String prenom_conducteur_a;
    private String adresse_conducteur_a;
    private String adresse_conducteur_b;
    private String num_permis_a;
    private String num_permis_b;
    private LocalDate delivre_a;
    private LocalDate delivre_b;
    private String num_contrat_a;
    private String num_contrat_b;
    private String marque_vehicule_a;
    public SinisterVehicle(LocalDate date_sinister, String location, String nom_conducteur_a, String nom_conducteur_b, String prenom_conducteur_b, String prenom_conducteur_a, String adresse_conducteur_a, String adresse_conducteur_b, String num_permis_a, String num_permis_b, LocalDate delivre_a, LocalDate delivre_b, String num_contrat_a, String num_contrat_b, String marque_vehicule_a, String marque_vehicule_b, String immatriculation_a, String immatriculation_b, String bvehicule_assure_par, String agence) {
        super(date_sinister, location);
//        this.image_file = image_file;
        // this.image_name = image_name;
        this.nom_conducteur_a = nom_conducteur_a;
        this.nom_conducteur_b = nom_conducteur_b;
        this.prenom_conducteur_b = prenom_conducteur_b;
        this.prenom_conducteur_a = prenom_conducteur_a;
        this.adresse_conducteur_a = adresse_conducteur_a;
        this.adresse_conducteur_b = adresse_conducteur_b;
        this.num_permis_a = num_permis_a;
        this.num_permis_b = num_permis_b;
        this.delivre_a = delivre_a;
        this.delivre_b = delivre_b;
        this.num_contrat_a = num_contrat_a;
        this.num_contrat_b = num_contrat_b;
        this.marque_vehicule_a = marque_vehicule_a;
        this.marque_vehicule_b = marque_vehicule_b;
        this.immatriculation_a = immatriculation_a;
        this.immatriculation_b = immatriculation_b;
        this.bvehicule_assure_par = bvehicule_assure_par;
        this.agence = agence;

    }

    public SinisterVehicle(LocalDate date_sinister, String location, String nom_conducteur_a, String nom_conducteur_b, String prenom_conducteur_b, String prenom_conducteur_a, String adresse_conducteur_a, String adresse_conducteur_b, String num_permis_a, String num_permis_b, LocalDate delivre_a, LocalDate delivre_b, String num_contrat_a, String num_contrat_b, String marque_vehicule_a, String marque_vehicule_b, String immatriculation_a, String immatriculation_b, String bvehicule_assure_par, String agence, String image_name) {
        super(date_sinister, location);
//        this.image_file = image_file;
        // this.image_name = image_name;
        this.nom_conducteur_a = nom_conducteur_a;
        this.nom_conducteur_b = nom_conducteur_b;
        this.prenom_conducteur_b = prenom_conducteur_b;
        this.prenom_conducteur_a = prenom_conducteur_a;
        this.adresse_conducteur_a = adresse_conducteur_a;
        this.adresse_conducteur_b = adresse_conducteur_b;
        this.num_permis_a = num_permis_a;
        this.num_permis_b = num_permis_b;
        this.delivre_a = delivre_a;
        this.delivre_b = delivre_b;
        this.num_contrat_a = num_contrat_a;
        this.num_contrat_b = num_contrat_b;
        this.marque_vehicule_a = marque_vehicule_a;
        this.marque_vehicule_b = marque_vehicule_b;
        this.immatriculation_a = immatriculation_a;
        this.immatriculation_b = immatriculation_b;
        this.bvehicule_assure_par = bvehicule_assure_par;
        this.agence = agence;
        this.image_name=image_name;
    }

    private String marque_vehicule_b;
    private String immatriculation_a;
    private String immatriculation_b;
    private String bvehicule_assure_par;
    private String agence;
    public SinisterVehicle(long id, LocalDate date_sinister, String location, User sinister_user_id, String status_sinister) {
        super(id, date_sinister, location, sinister_user_id, status_sinister);
    }

    public File getImage_file() {
        return image_file;
    }

    public void setImage_file(File image_file) {
        this.image_file = image_file;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getNom_conducteur_a() {
        return nom_conducteur_a;
    }

    public void setNom_conducteur_a(String nom_conducteur_a) {
        this.nom_conducteur_a = nom_conducteur_a;
    }

    public String getNom_conducteur_b() {
        return nom_conducteur_b;
    }

    public void setNom_conducteur_b(String nom_conducteur_b) {
        this.nom_conducteur_b = nom_conducteur_b;
    }

    public String getPrenom_conducteur_b() {
        return prenom_conducteur_b;
    }

    public void setPrenom_conducteur_b(String prenom_conducteur_b) {
        this.prenom_conducteur_b = prenom_conducteur_b;
    }

    public String getPrenom_conducteur_a() {
        return prenom_conducteur_a;
    }

    public void setPrenom_conducteur_a(String prenom_conducteur_a) {
        this.prenom_conducteur_a = prenom_conducteur_a;
    }

    public String getAdresse_conducteur_a() {
        return adresse_conducteur_a;
    }

    public void setAdresse_conducteur_a(String adresse_conducteur_a) {
        this.adresse_conducteur_a = adresse_conducteur_a;
    }

    public String getAdresse_conducteur_b() {
        return adresse_conducteur_b;
    }

    public void setAdresse_conducteur_b(String adresse_conducteur_b) {
        this.adresse_conducteur_b = adresse_conducteur_b;
    }

    public SinisterVehicle(Long id, LocalDate date_sinister, String status_sinister) {
        super(id, date_sinister, status_sinister);
    }
    public String getNum_permis_a() {
        return num_permis_a;
    }

    public SinisterVehicle(Long id, LocalDate date_sinister, String location, String status_sinister, String nom_conducteur_a, String nom_conducteur_b, String prenom_conducteur_b, String prenom_conducteur_a, String adresse_conducteur_a, String adresse_conducteur_b, String num_permis_a, String num_permis_b, LocalDate delivre_a, LocalDate delivre_b, String num_contrat_a, String num_contrat_b, String marque_vehicule_a, String marque_vehicule_b, String immatriculation_a, String immatriculation_b, String bvehicule_assure_par, String agence, String image_name) {
        super(id, date_sinister, location, status_sinister);
        this.nom_conducteur_a = nom_conducteur_a;
        this.nom_conducteur_b = nom_conducteur_b;
        this.prenom_conducteur_b = prenom_conducteur_b;
        this.prenom_conducteur_a = prenom_conducteur_a;
        this.adresse_conducteur_a = adresse_conducteur_a;
        this.adresse_conducteur_b = adresse_conducteur_b;
        this.num_permis_a = num_permis_a;
        this.num_permis_b = num_permis_b;
        this.delivre_a = delivre_a;
        this.delivre_b = delivre_b;
        this.num_contrat_a = num_contrat_a;
        this.num_contrat_b = num_contrat_b;
        this.marque_vehicule_a = marque_vehicule_a;
        this.marque_vehicule_b = marque_vehicule_b;
        this.immatriculation_a = immatriculation_a;
        this.immatriculation_b = immatriculation_b;
        this.bvehicule_assure_par = bvehicule_assure_par;
        this.agence = agence;
        this.image_name=image_name;
    }

    @Override
    public String toString() {
        return "SinisterVehicle{" +
                ", nom_conducteur_a='" + nom_conducteur_a + '\'' +
                ", nom_conducteur_b='" + nom_conducteur_b + '\'' +
                ", prenom_conducteur_b='" + prenom_conducteur_b + '\'' +
                ", prenom_conducteur_a='" + prenom_conducteur_a + '\'' +
                ", adresse_conducteur_a='" + adresse_conducteur_a + '\'' +
                ", adresse_conducteur_b='" + adresse_conducteur_b + '\'' +
                ", num_permis_a='" + num_permis_a + '\'' +
                ", num_permis_b='" + num_permis_b + '\'' +
                ", delivre_a=" + delivre_a +
                ", delivre_b=" + delivre_b +
                ", num_contrat_a='" + num_contrat_a + '\'' +
                ", num_contrat_b='" + num_contrat_b + '\'' +
                ", marque_vehicule_a='" + marque_vehicule_a + '\'' +
                ", marque_vehicule_b='" + marque_vehicule_b + '\'' +
                ", immatriculation_a='" + immatriculation_a + '\'' +
                ", immatriculation_b='" + immatriculation_b + '\'' +
                ", bvehicule_assure_par='" + bvehicule_assure_par + '\'' +
                ", agence='" + agence + '\'' +
                '}';
    }

    public void setNum_permis_a(String num_permis_a) {
        this.num_permis_a = num_permis_a;
    }

    public String getNum_permis_b() {
        return num_permis_b;
    }

    public void setNum_permis_b(String num_permis_b) {
        this.num_permis_b = num_permis_b;
    }

    public LocalDate getDelivre_a() {
        return delivre_a;
    }

    public void setDelivre_a(LocalDate delivre_a) {
        this.delivre_a = delivre_a;
    }

    public LocalDate getDelivre_b() {
        return delivre_b;
    }

    public void setDelivre_b(LocalDate delivre_b) {
        this.delivre_b = delivre_b;
    }

    public String getNum_contrat_a() {
        return num_contrat_a;
    }

    public void setNum_contrat_a(String num_contrat_a) {
        this.num_contrat_a = num_contrat_a;
    }

    public String getNum_contrat_b() {
        return num_contrat_b;
    }

    public void setNum_contrat_b(String num_contrat_b) {
        this.num_contrat_b = num_contrat_b;
    }

    public String getMarque_vehicule_a() {
        return marque_vehicule_a;
    }

    public void setMarque_vehicule_a(String marque_vehicule_a) {
        this.marque_vehicule_a = marque_vehicule_a;
    }

    public String getMarque_vehicule_b() {
        return marque_vehicule_b;
    }

    public void setMarque_vehicule_b(String marque_vehicule_b) {
        this.marque_vehicule_b = marque_vehicule_b;
    }

    public String getImmatriculation_a() {
        return immatriculation_a;
    }

    public void setImmatriculation_a(String immatriculation_a) {
        this.immatriculation_a = immatriculation_a;
    }

    public String getImmatriculation_b() {
        return immatriculation_b;
    }

    public void setImmatriculation_b(String immatriculation_b) {
        this.immatriculation_b = immatriculation_b;
    }

    public String getBvehicule_assure_par() {
        return bvehicule_assure_par;
    }

    public void setBvehicule_assure_par(String bvehicule_assure_par) {
        this.bvehicule_assure_par = bvehicule_assure_par;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }



}
