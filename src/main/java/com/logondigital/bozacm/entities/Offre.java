package com.logondigital.bozacm.entities;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "offres")
public class Offre {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titre;
    private String description;
    private Double prix;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Temporal(TemporalType.DATE)
    private Date dateDepart;

    @ManyToOne
    @JsonIgnoreProperties({"offres"})  // ignore la liste d'offres dans Agence
    private Agence agence;
    @OneToMany(mappedBy = "offre")
    private List<Reservation> reservations = new ArrayList<>();
    @ManyToOne
    @JsonIgnoreProperties({"offres"})  // ignore la liste d'offres dans Trajet
    private Trajet trajet;

    public String getName() {
        return titre;
    }

    public void setName(String name) {
        this.titre= name;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;


    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


}

