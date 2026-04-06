package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.ReservationRequestDTO;
import com.logondigital.bozacm.dto.ReservationResponseDTO;
import com.logondigital.bozacm.entities.ReservationOffre.StatutReservation;

import java.util.List;

public interface ReservationOffreService {

    ReservationResponseDTO createReservation(ReservationRequestDTO dto);

    List<ReservationResponseDTO> getAllReservations();

    PageResponseDTO<ReservationResponseDTO> getAllReservationsPaginated(int page, int size, String sortBy);

    ReservationResponseDTO getReservationById(Integer id);

    ReservationResponseDTO updateReservation(Integer id, ReservationRequestDTO dto);

    void deleteReservation(Integer id);

    List<ReservationResponseDTO> getReservationsByStatut(StatutReservation statut);

    List<ReservationResponseDTO> getReservationsByClient(String email);
}