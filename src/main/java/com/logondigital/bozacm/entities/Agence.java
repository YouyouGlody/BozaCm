package com.logondigital.bozacm.entities;



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
@Table(name = "agences")
public class Agence {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    private String adresse;
    private String email;
    private String telephone;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Temporal(TemporalType.DATE)
    private Date updatedAt;
    @OneToMany(mappedBy = "agence")
    private List<Offre> offres = new ArrayList<>();
    public String getName() {
        return nom;
    }
    public void setName(String name) {
        this.nom= name;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }



}

