package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.DTO.admin.AdminResponseDTO;
import com.logondigital.bozacm.DTO.admin.AdminUpdateDTO;
import com.logondigital.bozacm.DTO.common.ApiResponse;
import com.logondigital.bozacm.DTO.mapper.AdminMapper;
import com.logondigital.bozacm.entities.Admin;
import com.logondigital.bozacm.security.CustomUserDetails;
import com.logondigital.bozacm.service.admin.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * Profil de l'admin authentifié : lecture, mise à jour nom/email, photo de profil.
 *
 * Toutes les actions portent sur l'admin du token JWT (jamais un {id} dans l'URL) :
 * ça évite qu'un admin modifie le profil d'un autre en changeant un ID dans la requête.
 *
 * Base URL : /api/v1/admins
 */
@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    private static final List<String> CONTENT_TYPES_AUTORISES = List.of("image/jpeg", "image/png", "image/webp");
    private static final long TAILLE_MAX_OCTETS = 5L * 1024 * 1024; // 5 Mo

    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @Value("${app.avatar.upload-dir:uploads/avatars}")
    private String uploadDir;

    @Value("${app.avatar.base-url:http://localhost:8080/avatars}")
    private String baseUrl;

    public AdminController(AdminService adminService, AdminMapper adminMapper) {
        this.adminService = adminService;
        this.adminMapper = adminMapper;
    }

    // GET /api/v1/admins/me
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getProfil(@AuthenticationPrincipal CustomUserDetails principal) {
        Admin admin = adminService.findByEmail(principal.getUsername());
        AdminResponseDTO dto = adminMapper.toResponseDTO(admin);

        return ResponseEntity.ok(ApiResponse.success("Profil récupéré avec succès", dto));
    }

    // PUT /api/v1/admins/update-profile
    @PutMapping("/update-profile")
    public ResponseEntity<ApiResponse> updateProfil(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody AdminUpdateDTO updateDTO
    ) {
        Admin admin = adminService.updateProfile(principal.getUsername(), updateDTO);
        AdminResponseDTO dto = adminMapper.toResponseDTO(admin);

        return ResponseEntity.ok(ApiResponse.success("Profil mis à jour avec succès", dto));
    }

    // POST /api/v1/admins/photo
    @PostMapping("/photo")
    public ResponseEntity<ApiResponse> uploadPhoto(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Aucun fichier reçu");
        }
        if (!CONTENT_TYPES_AUTORISES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Format non supporté (JPEG, PNG ou WebP uniquement)");
        }
        if (file.getSize() > TAILLE_MAX_OCTETS) {
            throw new IllegalArgumentException("Image trop lourde (5 Mo max)");
        }

        Admin admin = adminService.findByEmail(principal.getUsername());

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String extension = extraireExtension(file.getOriginalFilename());
        String fileName = "admin-" + admin.getId() + "-" + UUID.randomUUID() + extension;
        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath);

        String photoUrl = baseUrl + "/" + fileName;
        Admin adminMisAJour = adminService.updatePhoto(principal.getUsername(), photoUrl);
        AdminResponseDTO dto = adminMapper.toResponseDTO(adminMisAJour);

        return ResponseEntity.ok(ApiResponse.success("Photo de profil mise à jour avec succès", dto));
    }

    private String extraireExtension(String nomFichierOriginal) {
        if (nomFichierOriginal == null || !nomFichierOriginal.contains(".")) {
            return "";
        }
        return nomFichierOriginal.substring(nomFichierOriginal.lastIndexOf("."));
    }
}
