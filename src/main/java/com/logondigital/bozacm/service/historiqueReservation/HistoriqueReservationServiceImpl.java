package com.logondigital.bozacm.service.historiqueReservation;

import com.logondigital.bozacm.entities.HistoriqueReservation;
import com.logondigital.bozacm.repository.HistoriqueReservationRepo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HistoriqueReservationServiceImpl implements HistoriqueReservationService {

    private final HistoriqueReservationRepo historiqueReservationRepo;

    public HistoriqueReservationServiceImpl(HistoriqueReservationRepo historiqueReservationRepo) {
        this.historiqueReservationRepo = historiqueReservationRepo;
    }

    // 1. Ajouter une nouvelle entrée dans l’historique
    @Override
    public void addHistorique(HistoriqueReservation historiqueReservation) {
        historiqueReservationRepo.save(historiqueReservation) ;
    }


    // 2. Récupérer toutes les entrées de l’historique
    @Override
    public List<HistoriqueReservation> getAllHistoriques() {
        return this.historiqueReservationRepo.findAll();
    }


    // 3. Récupérer l’historique d’un client spécifique
    @Override
    public List<HistoriqueReservation> getHistoriquesByClient(Integer idClient) {
        return historiqueReservationRepo.findByClientId(idClient);
    }

    //4. Supprimer une entrée d’historique par son ID
    @Override
    public void deleteHistorique(Integer idHistorique) {
        historiqueReservationRepo.deleteById(idHistorique);
    }
}
