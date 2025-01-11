package org.example.models;

public class ContratAssurance {


    private int id;

    private InsuranceRequest  request;
    public ContratAssurance(){};
    public ContratAssurance (int id, InsuranceRequest request){
        this.id=id;
        this.request=request;
    }
    public ContratAssurance ( InsuranceRequest request){
        this.request=request;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InsuranceRequest getRequest() {
        return request;
    }

    public void setRequest(InsuranceRequest request) {
        this.request = request;
    }
}
