package com.logondigital.bozacm.DTO;


import java.util.Date;

public class HistoriqueTrajetDto {

    private Integer idTrajet;
    private String villeDepart;
    private String villeArrivee;
    private Date dateConsultation;

    public HistoriqueTrajetDto(
            Integer idTrajet,
            String villeDepart,
            String villeArrivee,
            Date dateConsultation
    ) {
        this.idTrajet = idTrajet;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateConsultation = dateConsultation;
    }

    public Integer getIdTrajet() {
        return idTrajet;
    }

    public void setIdTrajet(Integer idTrajet) {
        this.idTrajet = idTrajet;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public Date getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(Date dateConsultation) {
        this.dateConsultation = dateConsultation;
    }
}
