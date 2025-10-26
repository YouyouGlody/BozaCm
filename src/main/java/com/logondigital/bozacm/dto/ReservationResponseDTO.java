package com.logondigital.bozacm.dto;

import lombok.Data;

import java.util.Date;

@Data

public class ReservationResponseDTO {

    private Integer id;
    private String nomClient;
    private String emailClient;
    private Date dateReservation;
    private String statut;
    private OffreResponseDTO offre;

    public ReservationResponseDTO() {
    }

    public ReservationResponseDTO(Integer id, String nomClient, String emailClient, Date dateReservation, String statut, OffreResponseDTO offre) {
        this.id = id;
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.offre = offre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public OffreResponseDTO getOffre() {
        return offre;
    }

    public void setOffre(OffreResponseDTO offre) {
        this.offre = offre;
    }
}
