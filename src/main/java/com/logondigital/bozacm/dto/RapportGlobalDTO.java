package com.logondigital.bozacm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportGlobalDTO {

    private Long  totalReservations;
    private Long   totalConfirmees;
    private Long   totalEnAttente;
    private Long   totalAnnulees;
    private Double tauxConfirmation;

    private Double chiffreAffairesTotal;

    private String offreLaPlusReservee;
    private String agenceLaPlusActive;
    private String trajetLePlusEmprunte;

    private Long totalOffres;
    private Long totalAgences;
    private Long totalTrajets;
}