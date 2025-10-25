package com.logondigital.bozacm.service.agence;

import com.logondigital.bozacm.dto.AgenceRequestDTO;
import com.logondigital.bozacm.dto.AgenceResponseDTO;

import java.util.List;

public interface AgenceService {


        void createAgence(AgenceRequestDTO dto);

        List<AgenceResponseDTO> getAllAgences();


        AgenceResponseDTO getAgenceById(Integer id);

        void updateAgence(Integer id, AgenceRequestDTO dto);

        void deleteAgence(Integer id);


    }

