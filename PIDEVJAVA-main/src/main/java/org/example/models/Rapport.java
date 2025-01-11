package org.example.models;

public class Rapport {
    private Integer id;
    private String decision;
    private Sinister sinisterRapport;
    private String justification;
    public Rapport(String decision){
        this.decision = decision;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Sinister getSinisterRapport() {
        return sinisterRapport;
    }

    public Rapport(Integer id, String decision, String justification) {
        this.id = id;
        this.decision = decision;

        this.justification = justification;
    }

    public void setSinisterRapport(Sinister sinisterRapport) {
        this.sinisterRapport = sinisterRapport;
    }

    public Rapport(String decision, String justification) {
        this.decision = decision;
        this.justification = justification;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }
}