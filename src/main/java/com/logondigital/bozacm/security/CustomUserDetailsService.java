package com.logondigital.bozacm.security;

import com.logondigital.bozacm.entities.Admin;
import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.repository.AdminRepo;
import com.logondigital.bozacm.repository.AgenceRepository;
import com.logondigital.bozacm.repository.ClientRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepo adminRepo;
    private final ClientRepo clientRepo;
    private final AgenceRepository agenceRepository;

    public CustomUserDetailsService(AdminRepo adminRepo, ClientRepo clientRepo, AgenceRepository agenceRepository) {
        this.adminRepo = adminRepo;
        this.clientRepo = clientRepo;
        this.agenceRepository = agenceRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var adminOpt = adminRepo.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return new CustomUserDetails(admin.getEmail(), admin.getPassword(), admin.getRole().name());
        }

        var clientOpt = clientRepo.findByEmail(email);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            return new CustomUserDetails(client.getEmail(), client.getPassword(), client.getRole().name());
        }

        var agenceOpt = agenceRepository.findByEmail(email);
        if (agenceOpt.isPresent()) {
            Agence agence = agenceOpt.get();
            return new CustomUserDetails(agence.getEmail(), agence.getPassword(), agence.getRole().name());
        }

        throw new UsernameNotFoundException("Aucun utilisateur trouvé avec l'email : " + email);
    }
}