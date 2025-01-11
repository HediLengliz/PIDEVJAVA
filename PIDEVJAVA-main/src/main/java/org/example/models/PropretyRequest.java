package org.example.models;

import java.time.LocalDate;

public class PropretyRequest extends InsuranceRequest {
    private int id_property;
    private String propertyForm;
    private String numberRooms;
    private String address;
    private String propertyValue;
    private String surface;
    public  PropretyRequest(){}
    public PropretyRequest (LocalDate dateRequest, String typeInsurance, String status, User user, int id_property, String propertyForm, String numberRooms, String address, String propertyValue, String surface) {
        super(dateRequest, typeInsurance, status,  user);
        this.propertyForm = propertyForm;
        this.numberRooms = numberRooms;
        this.address = address;
        this.propertyValue = propertyValue;
        this.surface = surface;
    }
    public PropretyRequest (int id, LocalDate dateRequest, String typeInsurance, String status, String requestUser, User user, int id_property,  String propertyForm, String numberRooms, String address, String propertyValue, String surface) {
        super(id, dateRequest, typeInsurance, status, requestUser, user);
        this.id_property=id_property;
        this.propertyForm = propertyForm;
        this.numberRooms = numberRooms;
        this.address = address;
        this.propertyValue = propertyValue;
        this.surface = surface;
    }
    public PropretyRequest (int id,  String propertyForm, String numberRooms, String address, String propertyValue, String surface) {
        this.id_property=id;
        this.propertyForm = propertyForm;
        this.numberRooms = numberRooms;
        this.address = address;
        this.propertyValue = propertyValue;
        this.surface = surface;
    }
    public PropretyRequest (int id_property, LocalDate dateRequest, String status, String propertyForm, String numberRooms, String address, String propertyValue, String surface) {
        super(dateRequest,  status);
        this.id_property=id_property;
        this.propertyForm = propertyForm;
        this.numberRooms = numberRooms;
        this.address = address;
        this.propertyValue = propertyValue;
        this.surface = surface;
    }

    public int getId_property() {
        return id_property;
    }

    public void setId_property(int id_property) {
        this.id_property = id_property;
    }

    public String getPropertyForm() {
        return propertyForm;
    }

    public void setPropertyForm(String propertyForm) {
        this.propertyForm = propertyForm;
    }

    public String getNumberRooms() {
        return numberRooms;
    }

    public void setNumberRooms(String numberRooms) {
        this.numberRooms = numberRooms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }
}
