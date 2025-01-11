package org.example.models;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.*;
public class User {
    private Integer id;
    private String email;
    private List<String> roles;
    private String password;
    private String firstName;
    private String lastName;
    private String cin;
    private String address;
    private String phoneNumber;
    private String claims; // Added field for JSON claims

    private String image;

    public String getClaims() {
        return claims;
    }
    public User(String email, List<String> roles, String password, String firstName, String lastName, String cin, String address, String phoneNumber, String claims) {
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cin = cin;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.claims = claims;
    }

    public User(int id, String email, List<String> roles, String password, String firstName, String lastName, String cin, String address, String phoneNumber,String image,String claims) {
        this.id=id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cin = cin;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.image=image;
        this.claims = claims;

    }



    public User(String email, List<String> roles, String firstName, String lastName, String cin, String address, String phoneNumber) {
        this.email = email;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cin = cin;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }


    public void setClaims(String claims) {
        this.claims = claims;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @OneToMany(mappedBy = "sinisterUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sinistre> theSinisters;

    // Assuming MedicalSheet is another entity
    @OneToMany(mappedBy = "userCIN", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalSheet> medicalSheets;

    // Assuming Prescription is another entity
    @OneToMany(mappedBy = "userCIN", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prescription> prescriptions;


    public User() {}

    public User(String lastName){
        this.lastName = lastName;
    }

    public User(String email, List<String> roles, String password, String firstName, String lastName, String cin, String address, String phoneNumber) {
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cin = cin;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public User(int id, String email, List<String> roles, String password, String firstName, String lastName, String cin, String address, String phoneNumber) {
        this.id=id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cin = cin;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public User(Integer id, String email, List<String> roles, String password, String firstName, String lastName, String cin, String address, String phoneNumber, List<Sinistre> theSinisters, List<MedicalSheet> medicalSheets, List<Prescription> prescriptions) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cin = cin;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.theSinisters = theSinisters;
        this.medicalSheets = medicalSheets;
        this.prescriptions = prescriptions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public List<Sinistre> getTheSinisters() {
        return theSinisters;
    }

    public void setTheSinisters(List<Sinistre> theSinisters) {
        this.theSinisters = theSinisters;
    }

    public List<MedicalSheet> getMedicalSheets() {
        return medicalSheets;
    }

    public void setMedicalSheets(List<MedicalSheet> medicalSheets) {
        this.medicalSheets = medicalSheets;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cin='" + cin + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}
