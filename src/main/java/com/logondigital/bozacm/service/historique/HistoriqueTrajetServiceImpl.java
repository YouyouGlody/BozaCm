package com.logondigital.bozacm.service.historique;



import com.logondigital.bozacm.DTO.HistoriqueTrajetDto;
import com.logondigital.bozacm.entities.Trajet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HistoriqueTrajetServiceImpl implements HistoriqueTrajetService {

    private final List<HistoriqueTrajetDto> historique = new ArrayList<>();

    public void addToHistorique(Trajet trajet) {

        HistoriqueTrajetDto dto = new HistoriqueTrajetDto(
                trajet.getIdTrajet(),
                trajet.getVilleDepart(),
                trajet.getVilleArrivee(),
                new Date()
        );

        historique.add(0, dto);

        if (historique.size() > 20) {
            historique.remove(historique.size() - 1);
        }
    }

    public List<HistoriqueTrajetDto> getHistorique() {
        return historique;
    }

    public void clearHistorique() {
        historique.clear();
    }
}

