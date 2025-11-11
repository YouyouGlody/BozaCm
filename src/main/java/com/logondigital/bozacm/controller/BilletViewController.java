package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.billet.BilletResponseDTO;
import com.logondigital.bozacm.dto.mapper.BilletMapper;
import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.service.billet.BilletService;
import com.logondigital.bozacm.service.pdf.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller pour les VUES HTML (Thymeleaf).
 * Séparé du BilletController pour ne pas casser les endpoints REST.
 */
@Controller  // ← IMPORTANT : @Controller, pas @RestController !
@RequestMapping("/api/v1/billets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BilletViewController {

    private final BilletService billetService;
    private final BilletMapper billetMapper;
    private final PdfService pdfService;

    /**
     * Affiche la page HTML avec toutes les infos du billet.
     * C'est cette page qui s'ouvre quand on scanne le QR Code.

     * GET /api/v1/billets/view/BZC-123...
     */
    @GetMapping("/view/{numeroBillet}")
    public String viewBillet(@PathVariable String numeroBillet, Model model) {
        System.out.println("📄 Affichage de la page pour le billet : " + numeroBillet);

        // Récupérer le billet
        Billet billet = billetService.findByNumeroBillet(numeroBillet);

        // Convertir en DTO
        BilletResponseDTO responseDTO = billetMapper.toResponseDTO(billet);

        // Passer au template Thymeleaf
        model.addAttribute("billet", responseDTO);

        System.out.println("✅ Page générée pour : " + responseDTO.getClientNomComplet());

        // Retourner le nom du template (billet-view.html)
        return "billet-view";
    }

    /**
     * Génère et télécharge le PDF du billet.

     * GET /api/v1/billets/pdf/BZC-123...
     */
    @GetMapping("/pdf/{numeroBillet}")
    public ResponseEntity<byte[]> downloadBilletPdf(@PathVariable String numeroBillet) {
        System.out.println("📥 Téléchargement PDF pour le billet : " + numeroBillet);

        try {
            // Récupérer le billet
            Billet billet = billetService.findByNumeroBillet(numeroBillet);
            BilletResponseDTO responseDTO = billetMapper.toResponseDTO(billet);

            // Générer le PDF
            byte[] pdfBytes = pdfService.generateBilletPdf(responseDTO);

            // Préparer les headers pour le téléchargement
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename("Billet_" + numeroBillet + ".pdf")
                            .build()
            );

            System.out.println("✅ PDF généré (" + pdfBytes.length + " bytes)");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            System.err.println("❌ Erreur génération PDF : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}