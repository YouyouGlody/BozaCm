package com.logondigital.bozacm.service.rapport;

import com.logondigital.bozacm.DTO.RapportGlobalDTO;
import com.logondigital.bozacm.entities.ReservationOffre.StatutReservation;
import com.logondigital.bozacm.repository.AgenceRepository;
import com.logondigital.bozacm.repository.OffreRepository;
import com.logondigital.bozacm.repository.ReservationOffreRepository;
import com.logondigital.bozacm.repository.TrajetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RapportServiceImpl implements RapportService {

    private final ReservationOffreRepository reservationOffreRepository;
    private final AgenceRepository           agenceRepository;
    private final OffreRepository            offreRepository;
    private final TrajetRepository           trajetRepository;

    public RapportServiceImpl(ReservationOffreRepository reservationOffreRepository,
                              AgenceRepository agenceRepository,
                              OffreRepository offreRepository,
                              TrajetRepository trajetRepository) {
        this.reservationOffreRepository = reservationOffreRepository;
        this.agenceRepository           = agenceRepository;
        this.offreRepository            = offreRepository;
        this.trajetRepository           = trajetRepository;
    }

    @Override
    public RapportGlobalDTO getRapportGlobal() {

        Long total      = reservationOffreRepository.countTotal();
        Long confirmees = reservationOffreRepository.countByStatut(StatutReservation.CONFIRMEE);
        Long enAttente  = reservationOffreRepository.countByStatut(StatutReservation.EN_ATTENTE);
        Long annulees   = reservationOffreRepository.countByStatut(StatutReservation.ANNULEE);

        Double taux = (total != null && total > 0)
                ? Math.round((confirmees * 100.0 / total) * 100.0) / 100.0
                : 0.0;

        Double ca = reservationOffreRepository.getTotalChiffreAffaires();

        String offreLaPlusReservee  = reservationOffreRepository.findOffreLaPlusReservee();
        String agenceLaPlusActive   = reservationOffreRepository.findAgenceLaPlusActive();
        String trajetLePlusEmprunte = reservationOffreRepository.findTrajetLePlusEmprunte();

        Long totalOffres  = offreRepository.count();
        Long totalAgences = agenceRepository.count();
        Long totalTrajets = trajetRepository.count();

        return new RapportGlobalDTO(
                total, confirmees, enAttente, annulees, taux,
                ca != null ? ca : 0.0,
                offreLaPlusReservee  != null ? offreLaPlusReservee  : "Aucune",
                agenceLaPlusActive   != null ? agenceLaPlusActive   : "Aucune",
                trajetLePlusEmprunte != null ? trajetLePlusEmprunte : "Aucun",
                totalOffres, totalAgences, totalTrajets
        );
    }
}