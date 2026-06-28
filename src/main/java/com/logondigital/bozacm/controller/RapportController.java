package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.DTO.RapportGlobalDTO;
import com.logondigital.bozacm.service.rapport.RapportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/rapport")
public class RapportController {

    private final RapportService rapportService;

    public RapportController(RapportService rapportService) {
        this.rapportService = rapportService;
    }

    // GET /api/v1/rapport/global
    // Retourne le tableau de bord complet du système en un seul appel
    @GetMapping(path = "/global")
    public ResponseEntity<RapportGlobalDTO> getRapportGlobal() {
        return ResponseEntity.status(200).body(this.rapportService.getRapportGlobal());
    }
}