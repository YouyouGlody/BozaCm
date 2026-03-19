package com.logondigital.bozacm.service.etape;


import com.example.gestion_trajets.entities.Etape;
import com.example.gestion_trajets.exception.ResourceNotFoundException;
import com.example.gestion_trajets.repositories.EtapeRepo;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EtapeServiceImpl implements EtapeService {

    private final EtapeRepo etapeRepo;

    public EtapeServiceImpl(EtapeRepo etapeRepo) {
        this.etapeRepo = etapeRepo;
    }

    public void createEtape(Etape etape) {
        etape.setId(null);
        etape.setDateCreation(new Date());
        etape.setDateModification(new Date());
        this.etapeRepo.save(etape);
    }

    @Override
    public List<Etape> getEtapes() {
        return this.etapeRepo.findAll();
    }

    @Override
    public Etape getEtapeById(Integer id) {
        Optional<Etape> etapeOptional = this.etapeRepo.findById(id);
        if (etapeOptional.isEmpty())
            throw new ResourceNotFoundException("L'etape n'existe pas !");
        return etapeOptional.get();
    }

    @Override
    public void updateEtape(Integer id, Etape etape) {
        Etape etapeToUpdate = this.etapeRepo.findById(id).orElseThrow(
                () ->new ResourceNotFoundException("Le trajet n'existe pas !")
        );

        etapeToUpdate.setNomEtape(etape.getNomEtape());
        etapeToUpdate.setDescription(etape.getDescription());
        etapeToUpdate.setDureeArret(etape.getDureeArret());
        etapeToUpdate.setDateModification(new Date());
        this.etapeRepo.saveAndFlush(etapeToUpdate);
    }

    @Override
    public void deleteEtape(Integer id) {
this.etapeRepo.deleteById(id);
    }

    @Override
    public @Nullable Etape getEtapeByNomEtape(String nomEtape) {
        return this.etapeRepo.findByNomEtape(nomEtape).orElseThrow(()->new ResourceNotFoundException("L'etape n'existe pas"));
    }


}
