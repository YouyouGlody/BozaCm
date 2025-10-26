package com.logondigital.bozacm.dto;

import lombok.Data;

import java.util.Date;

@Data

public class OffreResponseDTO {
    private Integer id;
    private String titre;
    private String description;
    private Double prix;
    private Date dateDepart;


    private Integer agenceId;
    private String agenceNom;
    private String agenceEmail;
    private String agenceAdresse;
    private String agenceTelephone;


    private Integer trajetId;
    private String trajetDepart;
    private String trajetArrivee;
    private String trajetDuree;

    public OffreResponseDTO() {
    }

    public OffreResponseDTO(Integer id, String titre, String description, Double prix, Date dateDepart,
                            Integer agenceId, String agenceNom, String agenceEmail, String agenceAdresse, String agenceTelephone,
                            Integer trajetId, String trajetDepart, String trajetArrivee, String trajetDuree) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.dateDepart = dateDepart;
        this.agenceId = agenceId;
        this.agenceNom = agenceNom;
        this.agenceEmail = agenceEmail;
        this.agenceAdresse = agenceAdresse;
        this.agenceTelephone = agenceTelephone;
        this.trajetId = trajetId;
        this.trajetDepart = trajetDepart;
        this.trajetArrivee = trajetArrivee;
        this.trajetDuree = trajetDuree;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Integer getAgenceId() {
        return agenceId;
    }

    public void setAgenceId(Integer agenceId) {
        this.agenceId = agenceId;
    }

    public String getAgenceNom() {
        return agenceNom;
    }

    public void setAgenceNom(String agenceNom) {
        this.agenceNom = agenceNom;
    }

    public String getAgenceEmail() {
        return agenceEmail;
    }

    public void setAgenceEmail(String agenceEmail) {
        this.agenceEmail = agenceEmail;
    }

    public String getAgenceAdresse() {
        return agenceAdresse;
    }

    public void setAgenceAdresse(String agenceAdresse) {
        this.agenceAdresse = agenceAdresse;
    }

    public String getAgenceTelephone() {
        return agenceTelephone;
    }

    public void setAgenceTelephone(String agenceTelephone) {
        this.agenceTelephone = agenceTelephone;
    }

    public Integer getTrajetId() {
        return trajetId;
    }

    public void setTrajetId(Integer trajetId) {
        this.trajetId = trajetId;
    }

    public String getTrajetDepart() {
        return trajetDepart;
    }

    public void setTrajetDepart(String trajetDepart) {
        this.trajetDepart = trajetDepart;
    }

    public String getTrajetArrivee() {
        return trajetArrivee;
    }

    public void setTrajetArrivee(String trajetArrivee) {
        this.trajetArrivee = trajetArrivee;
    }

    public String getTrajetDuree() {
        return trajetDuree;
    }

    public void setTrajetDuree(String trajetDuree) {
        this.trajetDuree = trajetDuree;
    }
}
