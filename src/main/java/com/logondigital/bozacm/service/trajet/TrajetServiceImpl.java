package com.logondigital.bozacm.service.trajet;


import com.logondigital.bozacm.DTO.PageResp;
import com.logondigital.bozacm.DTO.TrajetReq;
import com.logondigital.bozacm.DTO.TrajetRespDto;
import com.logondigital.bozacm.DTO.TrajetSearchDTO;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.enums.TypeTransport;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.EtapeRepo;
import com.logondigital.bozacm.repository.EvaluationRepo;
import com.logondigital.bozacm.repository.TrajetRepo;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@Service
public class TrajetServiceImpl implements TrajetService{

    private final TrajetRepo trajetRepo;
    private final EvaluationRepo evaluationRepo;
    private final EtapeRepo etapeRepo;

    public TrajetServiceImpl (TrajetRepo trajetRepo, EvaluationRepo evaluationRepo, EtapeRepo etapeRepo){
        this.trajetRepo = trajetRepo;
        this.evaluationRepo = evaluationRepo;
        this.etapeRepo = etapeRepo;
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

        trajet.setDateCreation(new Date());
        trajet.setDateModification(new Date());
        trajet.setDateHeureDepart(LocalDateTime.now());
        trajet.setDateHeureArrivee(LocalDateTime.now().plusHours(2));
        trajet.setDistance(trajetReq.getDistance());
        trajet.setDuree(trajetReq.getDuree());
        trajet.setTypeTransport(trajetReq.getTypeTransport());
        trajet.setNomCompagnie(trajetReq.getNomCompagnie());
        trajet.setNumVol_bus(trajetReq.getNumVol_bus());
        trajet.setOrdreTrajet(trajetReq.getOrdreTrajet());


        trajet.setDateCreation(new Date());
        trajet.setDateModification(new Date());



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
                        trajet.getDuree(),
                        trajet.getDistance(),
                        trajet.getTypeTransport(),
                        trajet.getNumVol_bus(),
                        trajet.getNomCompagnie(),
                        trajet.getOrdreTrajet()


                ))
                .toList();
    }

    @Override
    public TrajetRespDto getTrajetById(Integer idTrajet) {
        Trajet trajet = trajetRepo.findById(idTrajet)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet with id " + idTrajet + " not found !"));
                return new TrajetRespDto(
                trajet.getIdTrajet(),
                trajet.getVilleDepart(),
                trajet.getVilleArrivee(),
                trajet.getPaysDepart(),
                trajet.getPaysArrivee(),
                trajet.getDuree(),
                trajet.getDistance(),
                trajet.getTypeTransport(),
                trajet.getNumVol_bus(),
                trajet.getNomCompagnie(),
                trajet.getOrdreTrajet()
        );
    }


    @Override
    public void updateTrajet(Integer idTrajet, @Valid TrajetReq trajet) {
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

        Trajet trajet = trajetRepo.findById(idTrajet)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable"));

        etapeRepo.deleteAll(trajet.getEtapes());

        trajetRepo.delete(trajet);
    }

    @Override
    public PageResp<TrajetRespDto> getAllTrajetsPaginated(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Trajet> trajetPage = trajetRepo.findAll(pageable);

        List<TrajetRespDto> content = trajetPage.getContent()
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageResp<>(
                content,
                trajetPage.getNumber(),
                trajetPage.getSize(),
                trajetPage.getTotalElements(),
                trajetPage.getTotalPages(),
                trajetPage.isLast()
        );

    }

    private TrajetRespDto toDTO(Trajet trajet) {
        return new TrajetRespDto(
                trajet.getIdTrajet(),
                trajet.getVilleDepart(),
                trajet.getVilleArrivee(),
                trajet.getPaysDepart(),
                trajet.getPaysArrivee(),
                trajet.getDuree(),
                trajet.getDistance(),
                trajet.getTypeTransport(),
                trajet.getNumVol_bus(),
                trajet.getNomCompagnie(),
                trajet.getOrdreTrajet()
        );
    }


    @Override
    public List<TrajetRespDto> getByPaysDepart(String pays) {
        return trajetRepo.findByPaysDepart(pays)
                .stream().map(this::toDTO).toList();
    }


    @Override
    public List<TrajetRespDto> getByPaysArrivee(String pays) {
        return trajetRepo.findByPaysArrivee(pays)
                .stream().map(this::toDTO).toList();
    }


    @Override
    public List<TrajetRespDto> getByTypeTransport(TypeTransport type) {
        return trajetRepo.findByTypeTransport(type)
                .stream().map(this::toDTO).toList();
    }


    @Override
    public List<TrajetRespDto> getByRoute(String depart, String arrivee) {
        return trajetRepo.findByRoute(depart, arrivee)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<TrajetRespDto> getMultiCritere(TrajetSearchDTO criteria) {

        return trajetRepo.findAll()
                .stream()

                .filter(t -> criteria.getVilleDepart() == null
                        || t.getVilleDepart().toLowerCase().contains(criteria.getVilleDepart().toLowerCase()))

                .filter(t -> criteria.getVilleArrivee() == null
                        || t.getVilleArrivee().toLowerCase().contains(criteria.getVilleArrivee().toLowerCase()))

                .filter(t -> criteria.getPaysDepart() == null
                        || t.getPaysDepart().toLowerCase().contains(criteria.getPaysDepart().toLowerCase()))

                .filter(t -> criteria.getPaysArrivee() == null
                        || t.getPaysArrivee().toLowerCase().contains(criteria.getPaysArrivee().toLowerCase()))

                .filter(t -> criteria.getDureeMax() == null
                        || t.getDuree() <= criteria.getDureeMax())

                .filter(t -> criteria.getDistanceMax() == null
                        || t.getDistance() <= criteria.getDistanceMax())

                .filter(t -> criteria.getTypeTransport() == null
                        || t.getTypeTransport() == criteria.getTypeTransport())

                .map(this::toDTO)
                .toList();
    }

}







