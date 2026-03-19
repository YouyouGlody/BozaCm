package com.logondigital.bozacm.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "offres")
    public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offre_id")
        private Integer offreId;
        private String nomOffre;

        public Offre(Integer offreId, String nomOffre) {
            this.offreId = offreId;
            this.nomOffre = nomOffre;
        }

        public Offre() {
        }

        public Integer getOffreId() {
            return offreId;
        }

        public void setOffreId(Integer offreId) {
            this.offreId = offreId;
        }

        public String getNomOffre() {
            return nomOffre;
        }

        public void setNomOffre(String nomOffre) {
            this.nomOffre = nomOffre;
        }
    }

