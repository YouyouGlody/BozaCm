package com.logondigital.bozacm.service.rapport;

import com.logondigital.bozacm.DTO.RapportGlobalDTO;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.repository.AgenceRepository;
import com.logondigital.bozacm.repository.OffreRepository;
import com.logondigital.bozacm.repository.RapportReservationRepository;
import com.logondigital.bozacm.repository.TrajetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RapportServiceImpl implements RapportService {

    private final RapportReservationRepository reservationRepository;
    private final AgenceRepository             agenceRepository;
    private final OffreRepository              offreRepository;
    private final TrajetRepository             trajetRepository;

    public RapportServiceImpl(RapportReservationRepository reservationRepository,
                              AgenceRepository agenceRepository,
                              OffreRepository offreRepository,
                              TrajetRepository trajetRepository) {
        this.reservationRepository = reservationRepository;
        this.agenceRepository      = agenceRepository;
        this.offreRepository       = offreRepository;
        this.trajetRepository      = trajetRepository;
    }

    @Override
    public RapportGlobalDTO getRapportGlobal() {
        Long total      = reservationRepository.countTotal();
        Long confirmees = reservationRepository.countByStatut(StatutReservation.CONFIRMEE);
        Long enAttente  = reservationRepository.countByStatut(StatutReservation.EN_ATTENTE);
        Long annulees   = reservationRepository.countByStatut(StatutReservation.ANNULEE);

        Double taux = (total != null && total > 0)
                ? Math.round((confirmees * 100.0 / total) * 100.0) / 100.0
                : 0.0;

        Double ca = reservationRepository.getTotalChiffreAffaires();

        String offreLaPlusReservee  = reservationRepository.findOffreLaPlusReservee();
        String agenceLaPlusActive   = reservationRepository.findAgenceLaPlusActive();
        String trajetLePlusEmprunte = reservationRepository.findTrajetLePlusEmprunte();

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