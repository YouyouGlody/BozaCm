package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.DTO.LoginRequestDTO;
import com.logondigital.bozacm.DTO.LoginResponseDTO;
import com.logondigital.bozacm.DTO.RegisterAgenceDTO;
import com.logondigital.bozacm.DTO.RegisterClientDTO;
import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.enums.Role;
import com.logondigital.bozacm.repository.AgenceRepository;
import com.logondigital.bozacm.repository.ClientRepo;
import com.logondigital.bozacm.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ClientRepo clientRepo;
    private final AgenceRepository agenceRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(
            ClientRepo clientRepo,
            AgenceRepository agenceRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.clientRepo = clientRepo;
        this.agenceRepository = agenceRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody RegisterClientDTO dto) {

        if (clientRepo.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Un compte existe déjà avec cet email");
        }

        Client client = new Client();
        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setNumeroTelephone(dto.getNumeroTelephone());
        client.setEmail(dto.getEmail());
        client.setAdresse(dto.getAdresse());
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setRole(Role.CLIENT);

        clientRepo.save(client);

        return ResponseEntity.ok("Compte client créé avec succès");
    }

    @PostMapping("/register/agence")
    public ResponseEntity<?> registerAgence(@Valid @RequestBody RegisterAgenceDTO dto) {

        if (agenceRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Un compte existe déjà avec cet email");
        }

        Agence agence = new Agence();
        agence.setNom(dto.getNom());
        agence.setAdresse(dto.getAdresse());
        agence.setEmail(dto.getEmail());
        agence.setTelephone(dto.getTelephone());
        agence.setPassword(passwordEncoder.encode(dto.getPassword()));
        agence.setRole(Role.AGENCE);

        agenceRepository.save(agence);

        return ResponseEntity.ok("Compte agence créé avec succès");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            String role = authentication.getAuthorities().iterator().next()
                    .getAuthority().replace("ROLE_", "");

            String token = jwtUtil.generateToken(dto.getEmail(), role);

            return ResponseEntity.ok(new LoginResponseDTO(token, dto.getEmail(), Role.valueOf(role)));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
        }
    }
}