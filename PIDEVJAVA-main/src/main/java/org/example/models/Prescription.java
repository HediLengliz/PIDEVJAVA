package org.example.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date_prescription")
    private LocalDate datePrescription = LocalDate.now();

    @Column(name = "medications", nullable = false, columnDefinition = "TEXT")
    private String medications;

    @Column(name = "status_prescription", nullable = false, length = 20)
    private String statusPrescription;

    @Column(name = "additional_notes", columnDefinition = "TEXT")
    private String additionalNotes;

    @Column(name = "validity_duration")
    private Integer validityDuration;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_cin_id", nullable = false)
    private User userCIN;


    public Prescription() {
    }

    public Prescription(Long id, LocalDate datePrescription, String medications, String statusPrescription, String additionalNotes, Integer validityDuration, User userCIN) {
        this.id = id;
        this.datePrescription = datePrescription;
        this.medications = medications;
        this.statusPrescription = statusPrescription;
        this.additionalNotes = additionalNotes;
        this.validityDuration = validityDuration;
        this.userCIN = userCIN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatePrescription() {
        return datePrescription;
    }

    public void setDatePrescription(LocalDate datePrescription) {
        this.datePrescription = datePrescription;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getStatusPrescription() {
        return statusPrescription;
    }

    public void setStatusPrescription(String statusPrescription) {
        this.statusPrescription = statusPrescription;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public Integer getValidityDuration() {
        return validityDuration;
    }

    public void setValidityDuration(Integer validityDuration) {
        this.validityDuration = validityDuration;
    }

    public User getUserCIN() {
        return userCIN;
    }

    public void setUserCIN(User userCIN) {
        this.userCIN = userCIN;
    }
    public String getDatePrescriptionFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return datePrescription.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prescription that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getDatePrescription(), that.getDatePrescription()) && Objects.equals(getMedications(), that.getMedications()) && Objects.equals(getStatusPrescription(), that.getStatusPrescription()) && Objects.equals(getAdditionalNotes(), that.getAdditionalNotes()) && Objects.equals(getValidityDuration(), that.getValidityDuration()) && Objects.equals(getUserCIN(), that.getUserCIN());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDatePrescription(), getMedications(), getStatusPrescription(), getAdditionalNotes(), getValidityDuration(), getUserCIN());
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", datePrescription=" + datePrescription +
                ", medications='" + medications + '\'' +
                ", statusPrescription='" + statusPrescription + '\'' +
                ", additionalNotes='" + additionalNotes + '\'' +
                ", validityDuration=" + validityDuration +
                ", userCIN=" + userCIN +
                '}';
    }
}
