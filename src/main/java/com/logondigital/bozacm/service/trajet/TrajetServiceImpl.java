package com.logondigital.bozacm.service.trajet;


import com.logondigital.bozacm.DTO.OffreResp;
import com.logondigital.bozacm.DTO.TrajetReq;
import com.logondigital.bozacm.DTO.TrajetRespDto;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.OffreRepo;
import com.logondigital.bozacm.repository.TrajetRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@Service
public class TrajetServiceImpl implements TrajetService{

    private final TrajetRepo trajetRepo;
    private final OffreRepo offreRepo;

    public TrajetServiceImpl (TrajetRepo trajetRepo, OffreRepo offreRepo){
        this.trajetRepo = trajetRepo;

        this.offreRepo = offreRepo;
    }


    @Override
    public void createTrajet(TrajetReq trajetReq) {

        Trajet trajet = new Trajet(
                trajetReq.getVilleDepart(),
                trajetReq.getVilleArrivee(),
                trajetReq.getPaysDepart(),
                trajetReq.getPaysArrivee(),
                trajetReq.getDuree(),
                trajetReq.getDistance()
        );

        Offre offre = null;

        if (trajetReq.getOffreId() != null) {
            offre = offreRepo.findById(trajetReq.getOffreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Offre not found"));
        }
        else if (trajetReq.getOffreReq() != null) {
            offre = new Offre();
            offre.setNomOffre(trajetReq.getOffreReq().getNomOffre());
            offre.setOffreId(trajetReq.getOffreReq().getOffreId());
            offre = offreRepo.save(offre);
        }

        trajet.setOffre(offre);

        trajet.setDateCreation(new Date());
        trajet.setDateModification(new Date());
        trajet.setDateHeureDepart(LocalDateTime.now());
        trajet.setDateHeureArrivee(LocalDateTime.now().plusHours(2));

        this.trajetRepo.save(trajet);
    }

    @Override
    public List<TrajetRespDto> getTrajets() {
        return this.trajetRepo.findAll()
                .stream()
                .map(trajet -> new TrajetRespDto(
                        trajet.getIdTrajet(),
                        trajet.getVilleDepart(),
                        trajet.getVilleArrivee(),
                        trajet.getPaysDepart(),
                        trajet.getPaysArrivee(),
                        trajet.getOrdreTrajet(),
                        null
                ))
                .toList();
    }

    @Override
    public TrajetRespDto getTrajetById(Integer idTrajet) {
        Trajet trajet = this.trajetRepo.findById(idTrajet).orElseThrow(()->new ResourceNotFoundException("Trajet with id "+idTrajet+" not found !"));
        OffreResp offreResp = null;

        if (trajet.getOffre() != null) {
            offreResp = new OffreResp(
                    trajet.getOffre().getOffreId(),
                    trajet.getOffre().getNomOffre()
            );
        }
        return new TrajetRespDto(trajet.getIdTrajet(), trajet.getVilleDepart(),
                trajet.getVilleArrivee(),trajet.getPaysDepart(),trajet.getPaysArrivee(), trajet.getDuree(), trajet.getDistance(), trajet.getTypeTransport(), trajet.getNumVol_bus(), trajet.getNomCompagnie(), 
                trajet.getOrdreTrajet(), offreResp);
    }


    @Override
    public void updateTrajet(Integer idTrajet, Trajet trajet) {
        Optional<Trajet> oldTrajet = this.trajetRepo.findById(Integer.valueOf(idTrajet));

        if (oldTrajet.isEmpty())
            throw new ResourceNotFoundException(
                    "Trajet with id " + idTrajet + "    not found !"
            );

        oldTrajet.get().setVilleDepart(trajet.getVilleDepart());
        oldTrajet.get().setVilleArrivee(trajet.getVilleArrivee());
        oldTrajet.get().setPaysDepart(trajet.getPaysDepart());
        oldTrajet.get().setPaysArrivee(trajet.getPaysArrivee());
        oldTrajet.get().setDuree(trajet.getDuree());
        oldTrajet.get().setDistance(trajet.getDistance());
        oldTrajet.get().setTypeTransport(trajet.getTypeTransport());
        oldTrajet.get().setNumVol_bus(trajet.getNumVol_bus());
        oldTrajet.get().setNomCompagnie(trajet.getNomCompagnie());
        oldTrajet.get().setDateModification(new Date ());

        this.trajetRepo.saveAndFlush(oldTrajet.get());
    }

    @Override
    public void deleteTrajet(Integer idTrajet) {
        this.trajetRepo.deleteById(idTrajet);
    }
}






