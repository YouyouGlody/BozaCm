package com.logondigital.bozacm.controller;


import com.logondigital.bozacm.DTO.PageResp;
import com.logondigital.bozacm.DTO.TrajetReq;
import com.logondigital.bozacm.DTO.TrajetRespDto;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.service.trajet.TrajetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/trajets")
public class TrajetController {
    private final TrajetService trajetService;

    public TrajetController(TrajetService trajetService) {
        this.trajetService = trajetService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createTrajet(@RequestBody @Valid TrajetReq trajetReq){
        this.trajetService.createTrajet(trajetReq);
        return ResponseEntity.status(201).body("Trajet created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<TrajetRespDto>> getAllTrajets(){
        return ResponseEntity.status(200).body(this.trajetService.getTrajets());
    }

    @GetMapping("/get_by_id/{idTrajet}")
    public ResponseEntity<TrajetRespDto> getTrajetById(@PathVariable Integer idTrajet){
        return ResponseEntity.status(200).body(this.trajetService.getTrajetById(idTrajet));
    }


    @PutMapping("/update/{idTrajet}")
    public ResponseEntity<String> updateTrajet(
            @PathVariable Integer idTrajet,
            @RequestBody @Valid TrajetReq trajetReq) {

        trajetService.updateTrajet(idTrajet, trajetReq);

        return ResponseEntity.status(202).body("Updated successfully !");
    }

    @GetMapping("/page")
    public PageResp<TrajetRespDto> getPaginatedTrajets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idTrajet") String sortBy
    ) {
        return trajetService.getAllTrajetsPaginated(page, size, sortBy);
    }

    @DeleteMapping("/delete/{idTrajet}")
    public ResponseEntity<String> deletedSuccessfully(@PathVariable Integer idTrajet){
        this.trajetService.deleteTrajet(idTrajet);
        return ResponseEntity.status(202).body("Deleted successfully");
    }



}
