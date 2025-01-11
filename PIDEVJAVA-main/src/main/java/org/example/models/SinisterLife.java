package org.example.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
@DiscriminatorValue(value = "sinisterLife")
public class SinisterLife extends Sinistre{
    @Column(name = "description", length = 255)
    private String description;
    @Column(name = "beneficiary_name", length = 20)
    private String beneficiaryName;
    @OneToMany(mappedBy = "sinisterLife", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalSheet> medicalSheets = new ArrayList<>();

    public SinisterLife() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public List<MedicalSheet> getMedicalSheets() {
        return medicalSheets;
    }

    public void setMedicalSheets(List<MedicalSheet> medicalSheets) {
        this.medicalSheets = medicalSheets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SinisterLife that)) return false;
        return Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getBeneficiaryName(), that.getBeneficiaryName()) && Objects.equals(getMedicalSheets(), that.getMedicalSheets());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getBeneficiaryName(), getMedicalSheets());
    }

    @Override
    public String toString() {
        return "SinisterLife{" +
                "description='" + description + '\'' +
                ", beneficiaryName='" + beneficiaryName + '\'' +
                ", medicalSheets=" + medicalSheets +
                '}';
    }


}
