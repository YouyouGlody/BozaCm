package com.logondigital.bozacm.service.agence;

import com.logondigital.bozacm.dto.AgenceRequestDTO;
import com.logondigital.bozacm.dto.AgenceResponseDTO;
import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.StatistiquesAgenceDetailDTO;

import java.util.List;

public interface AgenceService {

        AgenceResponseDTO createAgence(AgenceRequestDTO dto);

        List<AgenceResponseDTO> getAllAgences();

        PageResponseDTO<AgenceResponseDTO> getAllAgencesPaginated(int page, int size, String sortBy);

        AgenceResponseDTO getAgenceById(Integer id);

        AgenceResponseDTO updateAgence(Integer id, AgenceRequestDTO dto);

        void deleteAgence(Integer id);

        AgenceResponseDTO getAgenceByEmail(String email);

        List<AgenceResponseDTO> getAgencesByVille(String ville);
        List<AgenceResponseDTO> rechercher(String terme);

        List<StatistiquesAgenceDetailDTO> getClassementAgences();

        StatistiquesAgenceDetailDTO getStatistiquesAgence(Integer agenceId);
}