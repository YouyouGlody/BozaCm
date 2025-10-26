package com.logondigital.bozacm.service.Offre;

import com.logondigital.bozacm.dto.OffreRequestDTO;
import com.logondigital.bozacm.dto.OffreResponseDTO;
import com.logondigital.bozacm.entities.Offre;

import java.util.List;

public interface OffreService {




        void createOffre(OffreRequestDTO dto);

        List<OffreResponseDTO> getAllOffres();

        OffreResponseDTO getOffreById(Integer id);

        void updateOffre(Integer id, OffreRequestDTO dto);

        void deleteOffre(Integer id);


    }

