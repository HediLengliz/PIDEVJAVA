package org.example.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "medical_sheet")
public class MedicalSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_cin_id", nullable = false)
    private User userCIN;

    @ManyToOne
    @JoinColumn(name = "sinister_life_id", nullable = false)
    private SinisterLife sinisterLife;
    @Column(name = "medical_diagnosis", nullable = false)
    private String medicalDiagnosis;

    @Column(name = "treatment_plan")
    private String treatmentPlan;

    @Column(name = "medical_reports")
    private String medicalReports;

    @Column(name = "duration_of_incapacity")
    private Integer durationOfIncapacity;

    @Column(name = "procedure_performed")
    private String procedurePerformed;

    @Column(name = "sick_leave_duration")
    private Integer sickLeaveDuration;

    @Column(name = "hospitalization_period")
    private Integer hospitalizationPeriod;

    @Column(name = "rehabilitation_period")
    private Integer rehabilitationPeriod;

    @Column(name = "medical_information")
    private String medicalInformation;
    public MedicalSheet() {
    }

    public MedicalSheet(Long id, User userCIN, SinisterLife sinisterLife, String medicalDiagnosis, String treatmentPlan, String medicalReports, Integer durationOfIncapacity, String procedurePerformed, Integer sickLeaveDuration, Integer hospitalizationPeriod, Integer rehabilitationPeriod, String medicalInformation) {
        this.id = id;
        this.userCIN = userCIN;
        this.sinisterLife = sinisterLife;
        this.medicalDiagnosis = medicalDiagnosis;
        this.treatmentPlan = treatmentPlan;
        this.medicalReports = medicalReports;
        this.durationOfIncapacity = durationOfIncapacity;
        this.procedurePerformed = procedurePerformed;
        this.sickLeaveDuration = sickLeaveDuration;
        this.hospitalizationPeriod = hospitalizationPeriod;
        this.rehabilitationPeriod = rehabilitationPeriod;
        this.medicalInformation = medicalInformation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserCIN() {
        return userCIN;
    }

    public void setUserCIN(User userCIN) {
        this.userCIN = userCIN;
    }

    public SinisterLife getSinisterLife() {
        return sinisterLife;
    }

    public void setSinisterLife(SinisterLife sinisterLife) {
        this.sinisterLife = sinisterLife;
    }

    public String getMedicalDiagnosis() {
        return medicalDiagnosis;
    }

    public void setMedicalDiagnosis(String medicalDiagnosis) {
        this.medicalDiagnosis = medicalDiagnosis;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public String getMedicalReports() {
        return medicalReports;
    }

    public void setMedicalReports(String medicalReports) {
        this.medicalReports = medicalReports;
    }

    public Integer getDurationOfIncapacity() {
        return durationOfIncapacity;
    }

    public void setDurationOfIncapacity(Integer durationOfIncapacity) {
        this.durationOfIncapacity = durationOfIncapacity;
    }

    public String getProcedurePerformed() {
        return procedurePerformed;
    }

    public void setProcedurePerformed(String procedurePerformed) {
        this.procedurePerformed = procedurePerformed;
    }

    public Integer getSickLeaveDuration() {
        return sickLeaveDuration;
    }

    public void setSickLeaveDuration(Integer sickLeaveDuration) {
        this.sickLeaveDuration = sickLeaveDuration;
    }

    public Integer getHospitalizationPeriod() {
        return hospitalizationPeriod;
    }

    public void setHospitalizationPeriod(Integer hospitalizationPeriod) {
        this.hospitalizationPeriod = hospitalizationPeriod;
    }

    public Integer getRehabilitationPeriod() {
        return rehabilitationPeriod;
    }

    public void setRehabilitationPeriod(Integer rehabilitationPeriod) {
        this.rehabilitationPeriod = rehabilitationPeriod;
    }

    public String getMedicalInformation() {
        return medicalInformation;
    }

    public void setMedicalInformation(String medicalInformation) {
        this.medicalInformation = medicalInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicalSheet that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getUserCIN(), that.getUserCIN()) && Objects.equals(getSinisterLife(), that.getSinisterLife()) && Objects.equals(getMedicalDiagnosis(), that.getMedicalDiagnosis()) && Objects.equals(getTreatmentPlan(), that.getTreatmentPlan()) && Objects.equals(getMedicalReports(), that.getMedicalReports()) && Objects.equals(getDurationOfIncapacity(), that.getDurationOfIncapacity()) && Objects.equals(getProcedurePerformed(), that.getProcedurePerformed()) && Objects.equals(getSickLeaveDuration(), that.getSickLeaveDuration()) && Objects.equals(getHospitalizationPeriod(), that.getHospitalizationPeriod()) && Objects.equals(getRehabilitationPeriod(), that.getRehabilitationPeriod()) && Objects.equals(getMedicalInformation(), that.getMedicalInformation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserCIN(), getSinisterLife(), getMedicalDiagnosis(), getTreatmentPlan(), getMedicalReports(), getDurationOfIncapacity(), getProcedurePerformed(), getSickLeaveDuration(), getHospitalizationPeriod(), getRehabilitationPeriod(), getMedicalInformation());
    }

    @Override
    public String toString() {
        return "MedicalSheet{" +
                "id=" + id +
                ", userCIN=" + userCIN +
                ", sinisterLife=" + sinisterLife +
                ", medicalDiagnosis='" + medicalDiagnosis + '\'' +
                ", treatmentPlan='" + treatmentPlan + '\'' +
                ", medicalReports='" + medicalReports + '\'' +
                ", durationOfIncapacity=" + durationOfIncapacity +
                ", procedurePerformed='" + procedurePerformed + '\'' +
                ", sickLeaveDuration=" + sickLeaveDuration +
                ", hospitalizationPeriod=" + hospitalizationPeriod +
                ", rehabilitationPeriod=" + rehabilitationPeriod +
                ", medicalInformation='" + medicalInformation + '\'' +
                '}';
    }
}
