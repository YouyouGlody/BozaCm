package com.logondigital.bozacm.service.historiqueReservation;

import com.logondigital.bozacm.entities.HistoriqueReservation;

import java.util.List;

public interface HistoriqueReservationService {

    // 1. Ajouter une nouvelle entrée dans l’historique
    HistoriqueReservation addHistorique(HistoriqueReservation historiqueReservation);

    // 2. Récupérer toutes les entrées de l’historique
    List<HistoriqueReservation> getAllHistoriques();

    // 3. Récupérer l’historique d’un client spécifique
//    List<HistoriqueReservation> getHistoriquesByClient(Integer idClient);

    // 4. Supprimer une entrée d’historique par son ID
    void deleteHistorique(Integer idHistorique);
}
