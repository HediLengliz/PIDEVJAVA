package org.example.models;

import java.time.LocalDate;

public class LifeRequest extends InsuranceRequest {
    private int id_life;
    private String age;
    private String chronicDisease;
    private String surgery;
    private String civilStatus;
    private String occupation;
    private String chronicDiseaseDescription;

    public LifeRequest(){};

    public LifeRequest (LocalDate dateRequest, String typeInsurance, String status, User user, int id_life, String age, String chronicDisease, String surgery, String civilStatus, String occupation, String chronicDiseaseDescription) {
        super(dateRequest, typeInsurance, status,  user);
        this.age = age;
        this.chronicDisease = chronicDisease;
        this.surgery = surgery;
        this.civilStatus = civilStatus;
        this.occupation = occupation;
        this.chronicDiseaseDescription = chronicDiseaseDescription;
    }
    public LifeRequest (int id, LocalDate dateRequest, String typeInsurance, String status, String requestUser, User user, int id_life, String age, String chronicDisease, String surgery, String civilStatus, String occupation, String chronicDiseaseDescription) {
        super(id, dateRequest, typeInsurance, status, requestUser, user);
        this.id_life=id_life;
        this.age = age;
        this.chronicDisease = chronicDisease;
        this.surgery = surgery;
        this.civilStatus = civilStatus;
        this.occupation = occupation;
        this.chronicDiseaseDescription = chronicDiseaseDescription;
    }
    public LifeRequest (int  id,  String age, String chronicDisease, String surgery, String civilStatus, String occupation, String chronicDiseaseDescription) {
        this.id_life=id;
        this.age = age;
        this.chronicDisease = chronicDisease;
        this.surgery = surgery;
        this.civilStatus = civilStatus;
        this.occupation = occupation;
        this.chronicDiseaseDescription = chronicDiseaseDescription;
    }
    public LifeRequest (int  id_life, LocalDate dateRequest, String status, String age, String chronicDisease, String surgery, String civilStatus, String occupation, String chronicDiseaseDescription) {
        super(dateRequest, status);
        this.id_life=id_life;
        this.age = age;
        this.chronicDisease = chronicDisease;
        this.surgery = surgery;
        this.civilStatus = civilStatus;
        this.occupation = occupation;
        this.chronicDiseaseDescription = chronicDiseaseDescription;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getChronicDisease() {
        return chronicDisease;
    }

    public void setChronicDisease(String chronicDisease) {
        this.chronicDisease = chronicDisease;
    }

    public String getSurgery() {
        return surgery;
    }

    public void setSurgery(String surgery) {
        this.surgery = surgery;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getChronicDiseaseDescription() {
        return chronicDiseaseDescription;
    }

    public void setChronicDiseaseDescription(String chronicDiseaseDescription) {
        this.chronicDiseaseDescription = chronicDiseaseDescription;
    }

    public int getId_life() {
        return id_life;
    }

    public void setId_life(int id_life) {
        this.id_life = id_life;
    }
}

