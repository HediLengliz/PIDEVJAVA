package org.example.models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "sinister_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "sinisterLife")
public class Sinistre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "date_sinister", length = 20)
    private Date dateSinister = new Date();

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "amount_sinister")
    private Float amountSinister;

    @Column(name = "status_sinister", length = 20)
    private String statusSinister = "ongoing";
    @ManyToOne
    @JoinColumn(name = "sinister_user_id", nullable = false)
    private User sinisterUser;

    public Sinistre() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateSinister() {
        return dateSinister;
    }

    public void setDateSinister(Date dateSinister) {
        this.dateSinister = dateSinister;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Float getAmountSinister() {
        return amountSinister;
    }

    public void setAmountSinister(Float amountSinister) {
        this.amountSinister = amountSinister;
    }

    public String getStatusSinister() {
        return statusSinister;
    }

    public void setStatusSinister(String statusSinister) {
        this.statusSinister = statusSinister;
    }

    public User getSinisterUser() {
        return sinisterUser;
    }

    public void setSinisterUser(User sinisterUser) {
        this.sinisterUser = sinisterUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sinistre sinister)) return false;
        return Objects.equals(getId(), sinister.getId()) && Objects.equals(getDateSinister(), sinister.getDateSinister()) && Objects.equals(getLocation(), sinister.getLocation()) && Objects.equals(getAmountSinister(), sinister.getAmountSinister()) && Objects.equals(getStatusSinister(), sinister.getStatusSinister()) && Objects.equals(getSinisterUser(), sinister.getSinisterUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateSinister(), getLocation(), getAmountSinister(), getStatusSinister(), getSinisterUser());
    }

    @Override
    public String toString() {
        return "Sinister{" +
                "id=" + id +
                ", dateSinister=" + dateSinister +
                ", location='" + location + '\'' +
                ", amountSinister=" + amountSinister +
                ", statusSinister='" + statusSinister + '\'' +
                ", sinisterUser=" + sinisterUser +
                '}';
    }
    public String getDateSinisterFormatted() {
        if (dateSinister != null) {
            return new SimpleDateFormat("dd/MM/yyyy").format(dateSinister);
        } else {
            return "";
        }
    }

}
