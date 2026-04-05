package com.logondigital.bozacm.controller;


import com.logondigital.bozacm.DTO.EtapeReq;
import com.logondigital.bozacm.DTO.EtapeResp;
import com.logondigital.bozacm.DTO.PageResp;
import com.logondigital.bozacm.entities.Etape;
import com.logondigital.bozacm.erreur.ErrorMessage;
import com.logondigital.bozacm.repository.TrajetRepo;
import com.logondigital.bozacm.service.etape.EtapeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "api/v1/etapes")
public class EtapeController {

        private final EtapeService etapeService;
        private final TrajetRepo trajetRepo;
    public EtapeController(EtapeService etapeService, TrajetRepo trajetRepo) {
        this.etapeService = etapeService;
        this.trajetRepo = trajetRepo;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@Valid @RequestBody EtapeReq etapeReq){
        this.etapeService.createEtape(etapeReq);
        return ResponseEntity.status(201).body("Etape created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<EtapeResp>> getAllEtapes(){
        return ResponseEntity.status(200).body(this.etapeService.getEtapes());
    }

    @GetMapping("/get_by_id/{id}")
    public ResponseEntity<EtapeResp> getEtape(@PathVariable Integer id){
        return ResponseEntity.status(200).body(this.etapeService.getEtapeById(id));
    }

    @GetMapping("/get_by_name/{nomEtape}")
    public ResponseEntity<EtapeResp> getEtapeByNomEtape(@RequestParam @PathVariable String nomEtape){
        return ResponseEntity.status(200).body(this.etapeService.getEtapeByNomEtape(nomEtape));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEtape(@RequestBody EtapeReq etapeReq, @PathVariable Integer id){
        this.etapeService.updateEtape(id, etapeReq);
        return ResponseEntity.status(202).body("Updated successfully !");

    }

    @GetMapping("/paginated")
    public ResponseEntity<PageResp<EtapeResp>> getPaginatedEtapes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.status(202).body(etapeService.getEtapesPaginated(page, size));
    }



    @GetMapping("/trajet/{trajetId}/all")
    public ResponseEntity<List<EtapeResp>> getAllEtapesByTrajet(@PathVariable Integer trajetId) {

        List<EtapeResp> etapes = etapeService.getEscalesByTrajet(trajetId);

        return ResponseEntity.status(200).body(etapes);
    }


    @GetMapping("/ville/{ville}")
    public ResponseEntity<List<EtapeResp>> getByVille(@PathVariable String ville) {
        return ResponseEntity.status(200).body(etapeService.getEscalesByVille(ville));
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletedSuccessfully(@PathVariable Integer id){
        this.etapeService.deleteEtape(id);
        return ResponseEntity.status(202).body("Deleted successfully");
    }

}
