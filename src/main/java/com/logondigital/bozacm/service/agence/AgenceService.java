package com.logondigital.bozacm.service.agence;

import com.logondigital.bozacm.entities.Agence;

import java.util.List;

public interface AgenceService {


        String createAgence(Agence agence);

        List<Agence> getAgences();

        Agence getAgenceById(Integer agenceId);

        String updateAgence(Integer agenceId, Agence agence);

        String deleteAgence(Integer agenceId);

        void CreateAgence(Agence agence);
    }

