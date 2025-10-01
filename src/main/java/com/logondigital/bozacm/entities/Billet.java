package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "billets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Billet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBillet;
    private String numeroBillet;
    private  String qrcodeUrl;
    private LocalDateTime dateEmission;
    private LocalDateTime dateExpiration;
    private String statutBillet;
    private Date createdAt;
    private Date updatedAt;

    public void getCreatedAt(Date date) {
    }
}
