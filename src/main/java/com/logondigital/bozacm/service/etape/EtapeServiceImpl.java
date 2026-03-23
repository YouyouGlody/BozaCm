package com.logondigital.bozacm.service.etape;



import com.logondigital.bozacm.DTO.EtapeReq;
import com.logondigital.bozacm.DTO.EtapeResp;
import com.logondigital.bozacm.entities.Etape;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.EtapeRepo;
import com.logondigital.bozacm.repository.TrajetRepo;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class EtapeServiceImpl implements EtapeService {

    private final EtapeRepo etapeRepo;
    private final TrajetRepo trajetRepo;

    public EtapeServiceImpl(EtapeRepo etapeRepo, TrajetRepo trajetRepo) {
        this.etapeRepo = etapeRepo;
        this.trajetRepo = trajetRepo;
    }

    @Override
    public void createEtape(@Valid EtapeReq etapeReq) {


        Trajet trajet = trajetRepo.findById(etapeReq.getIdTrajet())
                .orElseThrow(() -> new ResourceNotFoundException("Trajet not found"));

        Etape etape = new Etape();
        etape.setNomEtape(etapeReq.getNomEtape());
        etape.setVille(etapeReq.getVille());
        etape.setPays(etapeReq.getPays());
        etape.setDureeArret(etapeReq.getDureeArret());
        etape.setTypeEtape(etapeReq.getTypeEtape());
        etape.setOrdre(etapeReq.getOrdre());

        etape.setTrajet(trajet);

        etape.setDateCreation(new Date());
        etape.setDateModification(new Date());


        etapeRepo.save(etape);
    }



    @Override
    public List<EtapeResp> getEtapes() {
        return this.etapeRepo.findAll()
                .stream()
                .map(etape -> new EtapeResp(

                        etape.getId(),
                        etape.getNomEtape(),
                        etape.getDureeArret(),
                        etape.getVille(),
                        etape.getPays(),
                        etape.getTypeEtape(),
                        etape.getOrdre()

                ))
                .toList();
    }
    @Override
    public EtapeResp getEtapeById(Integer id) {
        Etape etape = this.etapeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'étape n'existe pas !"));

        return new EtapeResp(
                etape.getId(),
                etape.getNomEtape(),
                etape.getDureeArret()

        );
    }

    @Override
    public void updateEtape(Integer id, EtapeReq etapeReq) {
        Etape etapeToUpdate = this.etapeRepo.findById(id).orElseThrow(
                () ->new ResourceNotFoundException("Le trajet n'existe pas !")
        );
    if (etapeReq.getIdTrajet() != null) {
        Trajet trajet = trajetRepo.findById(etapeReq.getIdTrajet()).orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable"));

        etapeToUpdate.setTrajet(trajet);
    }


        etapeToUpdate.setNomEtape(etapeReq.getNomEtape());
        etapeToUpdate.setDureeArret(etapeReq.getDureeArret());
        etapeToUpdate.setDateModification(new Date());
        this.etapeRepo.saveAndFlush(etapeToUpdate);
    }

    @Override
    public void deleteEtape(Integer id) {
this.etapeRepo.deleteById(id);
    }

    @Override
    @Nullable
    public EtapeResp getEtapeByNomEtape(String nomEtape) {
        Etape etape = this.etapeRepo.findByNomEtape(nomEtape)
                .orElseThrow(() -> new ResourceNotFoundException("L'étape n'existe pas !"));

        return new EtapeResp(
                etape.getId(),
                etape.getNomEtape(),
                etape.getDureeArret()
        );
    }


}
