package org.example.models;

import java.time.LocalDate;

public class VehicleRequest extends InsuranceRequest {
    private int id_vehicle;
    private String marque;
    private String modele;
    private LocalDate fabYear;
    private String serialNumber;
    private String matriculNumber;
    private String vehiclePrice;
    public VehicleRequest(){};
    public VehicleRequest(LocalDate dateRequest, String typeInsurance, String status, User user, int id_vehicle, String marque, String modele, LocalDate fabYear, String serialNumber, String matriculNumber, String vehiclePrice) {
        super(dateRequest, typeInsurance, status,  user);
        this.marque = marque;
        this.modele = modele;
        this.fabYear = fabYear;
        this.serialNumber = serialNumber;
        this.matriculNumber = matriculNumber;
        this.vehiclePrice = vehiclePrice;

    }
    public VehicleRequest (int id, LocalDate dateRequest, String typeInsurance, String status, String requestUser, User user, int id_vehicle,  String marque, String modele, LocalDate fabYear, String serialNumber, String matriculNumber, String vehiclePrice) {
        super(id, dateRequest, typeInsurance, status, requestUser, user);
        this.id_vehicle=id_vehicle;
        this.marque = marque;
        this.modele = modele;
        this.fabYear = fabYear;
        this.serialNumber = serialNumber;
        this.matriculNumber = matriculNumber;
        this.vehiclePrice = vehiclePrice;
    }
    public VehicleRequest (int id,  String marque, String modele, LocalDate fabYear, String serialNumber, String matriculNumber, String vehiclePrice) {
        this.id_vehicle=id;
        this.marque = marque;
        this.modele = modele;
        this.fabYear = fabYear;
        this.serialNumber = serialNumber;
        this.matriculNumber = matriculNumber;
        this.vehiclePrice = vehiclePrice;
    }
    public VehicleRequest (int id_vehicle,  LocalDate dateRequest, String status, String marque, String modele, LocalDate fabYear, String serialNumber, String matriculNumber, String vehiclePrice) {
        super(dateRequest, status);
        this.id_vehicle=id_vehicle;
        this.marque = marque;
        this.modele = modele;
        this.fabYear = fabYear;
        this.serialNumber = serialNumber;
        this.matriculNumber = matriculNumber;
        this.vehiclePrice = vehiclePrice;
    }
    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public LocalDate getFabYear() {
        return fabYear;
    }

    public void setFabYear(LocalDate fabYear) {
        this.fabYear = fabYear;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMatriculNumber() {
        return matriculNumber;
    }

    public void setMatriculNumber(String matriculNumber) {
        this.matriculNumber = matriculNumber;
    }

    public String getVehiclePrice() {
        return vehiclePrice;
    }

    public void setVehiclePrice(String vehiclePrice) {
        this.vehiclePrice = vehiclePrice;
    }

    public int getId_vehicle() {
        return id_vehicle;
    }

    public void setId_vehicle(int id_vehicle) {
        this.id_vehicle = id_vehicle;
    }
}
