package com.logondigital.bozacm.dto;

import java.util.Date;


    public class RechercheOffreDTO {
        private String villeDepart;
        private String villeArrivee;
        private Double prixMin;
        private Double prixMax;
        private Date dateDepart;
        private Integer agenceId;

        public RechercheOffreDTO() {
        }

        public RechercheOffreDTO(String villeDepart, String villeArrivee, Double prixMin, Double prixMax, Date dateDepart, Integer agenceId) {
            this.villeDepart = villeDepart;
            this.villeArrivee = villeArrivee;
            this.prixMin = prixMin;
            this.prixMax = prixMax;
            this.dateDepart = dateDepart;
            this.agenceId = agenceId;
        }

        // Getters et Setters
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
    }

