package org.example.models;

import java.time.LocalDate;

public class InsuranceRequest {


    private int id=0;

    private LocalDate dateRequest;

    private String typeInsurance;

    private String status = "en_cours";

    private String requestUser;

    private User user;
    public InsuranceRequest(){};
    public InsuranceRequest(LocalDate dateRequest, String typeInsurance, String status, User user) {
        this.dateRequest = dateRequest;
        this.typeInsurance = typeInsurance;
        this.status = status;
        this.user = user;
    }
    public InsuranceRequest(int id, LocalDate dateRequest, String typeInsurance, String status, String requestUser, User user) {
        this.id = id;
        this.dateRequest = dateRequest;
        this.typeInsurance = typeInsurance;
        this.status = status;
        this.requestUser = requestUser;
        this.user = user;
    }
    public InsuranceRequest( LocalDate dateRequest,  String status) {
        this.dateRequest = dateRequest;
        this.status = status;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(LocalDate dateRequest) {
        this.dateRequest = dateRequest;
    }

    public String getTypeInsurance() {
        return typeInsurance;
    }

    public void setTypeInsurance(String typeInsurance) {
        this.typeInsurance = typeInsurance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
