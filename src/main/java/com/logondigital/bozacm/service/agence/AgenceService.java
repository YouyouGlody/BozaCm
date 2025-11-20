package com.logondigital.bozacm.service.agence;

import com.logondigital.bozacm.dto.AgenceRequestDTO;
import com.logondigital.bozacm.dto.AgenceResponseDTO;
import com.logondigital.bozacm.dto.StatistiquesAgenceDetailDTO;

import java.util.List;

public interface AgenceService {


        void createAgence(AgenceRequestDTO dto);

        List<AgenceResponseDTO> getAllAgences();


        AgenceResponseDTO getAgenceById(Integer id);

        void updateAgence(Integer id, AgenceRequestDTO dto);

        void deleteAgence(Integer id);
        AgenceResponseDTO getAgenceByEmail(String email);
        List<AgenceResponseDTO> getAgencesByVille(String ville);

        List<StatistiquesAgenceDetailDTO> getClassementAgences();
        StatistiquesAgenceDetailDTO getStatistiquesAgence(Integer agenceId);

    }

