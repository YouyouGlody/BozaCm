package com.logondigital.bozacm.dto;

public class StatistiquesAgenceDetailDTO {
    private Integer agenceId;
    private String agenceNom;
    private String agenceEmail;
    private String agenceTelephone;
    private String agenceAdresse;

    // Statistiques offres
    private Long nombreOffres;
    private Double prixMoyen;
    private Double prixMin;
    private Double prixMax;

    // Statistiques réservations
    private Long nombreReservationsTotal;
    private Long nombreReservationsConfirmees;
    private Double tauxConfirmation;

    // Statistiques financières
    private Double chiffreAffaire;

    // Classement
    private Integer rang;

    public StatistiquesAgenceDetailDTO() {
    }

    public StatistiquesAgenceDetailDTO(Integer agenceId, String agenceNom, String agenceEmail,
                                       String agenceTelephone, String agenceAdresse,
                                       Long nombreOffres, Double prixMoyen, Double prixMin, Double prixMax,
                                       Long nombreReservationsTotal, Long nombreReservationsConfirmees,
                                       Double tauxConfirmation, Double chiffreAffaire, Integer rang) {
        this.agenceId = agenceId;
        this.agenceNom = agenceNom;
        this.agenceEmail = agenceEmail;
        this.agenceTelephone = agenceTelephone;
        this.agenceAdresse = agenceAdresse;
        this.nombreOffres = nombreOffres;
        this.prixMoyen = prixMoyen;
        this.prixMin = prixMin;
        this.prixMax = prixMax;
        this.nombreReservationsTotal = nombreReservationsTotal;
        this.nombreReservationsConfirmees = nombreReservationsConfirmees;
        this.tauxConfirmation = tauxConfirmation;
        this.chiffreAffaire = chiffreAffaire;
        this.rang = rang;
    }

    // Getters et Setters
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

    public String getAgenceTelephone() {
        return agenceTelephone;
    }

    public void setAgenceTelephone(String agenceTelephone) {
        this.agenceTelephone = agenceTelephone;
    }

    public String getAgenceAdresse() {
        return agenceAdresse;
    }

    public void setAgenceAdresse(String agenceAdresse) {
        this.agenceAdresse = agenceAdresse;
    }

    public Long getNombreOffres() {
        return nombreOffres;
    }

    public void setNombreOffres(Long nombreOffres) {
        this.nombreOffres = nombreOffres;
    }

    public Double getPrixMoyen() {
        return prixMoyen;
    }

    public void setPrixMoyen(Double prixMoyen) {
        this.prixMoyen = prixMoyen;
    }

    public Double getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(Double prixMin) {
        this.prixMin = prixMin;
    }

    public Double getPrixMax() {
        return prixMax;
    }

    public void setPrixMax(Double prixMax) {
        this.prixMax = prixMax;
    }

    public Long getNombreReservationsTotal() {
        return nombreReservationsTotal;
    }

    public void setNombreReservationsTotal(Long nombreReservationsTotal) {
        this.nombreReservationsTotal = nombreReservationsTotal;
    }

    public Long getNombreReservationsConfirmees() {
        return nombreReservationsConfirmees;
    }

    public void setNombreReservationsConfirmees(Long nombreReservationsConfirmees) {
        this.nombreReservationsConfirmees = nombreReservationsConfirmees;
    }

    public Double getTauxConfirmation() {
        return tauxConfirmation;
    }

    public void setTauxConfirmation(Double tauxConfirmation) {
        this.tauxConfirmation = tauxConfirmation;
    }

    public Double getChiffreAffaire() {
        return chiffreAffaire;
    }

    public void setChiffreAffaire(Double chiffreAffaire) {
        this.chiffreAffaire = chiffreAffaire;
    }

    public Integer getRang() {
        return rang;
    }

    public void setRang(Integer rang) {
        this.rang = rang;
    }
}