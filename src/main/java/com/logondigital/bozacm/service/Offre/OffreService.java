package com.logondigital.bozacm.service.Offre;

import com.logondigital.bozacm.dto.OffreRequestDTO;
import com.logondigital.bozacm.dto.OffreResponseDTO;
import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.RechercheOffreDTO;

import java.util.List;

public interface OffreService {

    OffreResponseDTO createOffre(OffreRequestDTO dto);

    List<OffreResponseDTO> getAllOffres();

    PageResponseDTO<OffreResponseDTO> getAllOffresPaginated(int page, int size, String sortBy);

    OffreResponseDTO getOffreById(Integer id);

    OffreResponseDTO updateOffre(Integer id, OffreRequestDTO dto);

    void deleteOffre(Integer id);

    PageResponseDTO<OffreResponseDTO> rechercherOffres(RechercheOffreDTO criteres, int page, int size);

    List<OffreResponseDTO> getOffresByPrixRange(Double prixMin, Double prixMax);

    PageResponseDTO<OffreResponseDTO> getOffresByAgence(Integer agenceId, int page, int size);
}