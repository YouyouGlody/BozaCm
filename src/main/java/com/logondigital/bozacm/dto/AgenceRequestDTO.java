package com.logondigital.bozacm.dto;

import lombok.Data;

@Data

public class AgenceRequestDTO {

        private String nom;
        private String email;
        private String telephone;
        private String adresse;

        public AgenceRequestDTO() {
        }

        public AgenceRequestDTO(String nom, String email, String telephone, String adresse) {
                this.nom = nom;
                this.email = email;
                this.telephone = telephone;
                this.adresse = adresse;

        }

        public String getNom() {
                return nom;
        }

        public void setNom(String nom) {
                this.nom = nom;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getTelephone() {
                return telephone;
        }

        public void setTelephone(String telephone) {
                this.telephone = telephone;
        }

        public String getAdresse() {
                return adresse;
        }

        public void setAdresse(String adresse) {
                this.adresse = adresse;
        }
}
