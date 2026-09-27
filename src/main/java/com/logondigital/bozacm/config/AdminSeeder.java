package com.logondigital.bozacm.config;

import com.logondigital.bozacm.entities.Admin;
import com.logondigital.bozacm.enums.Role;
import com.logondigital.bozacm.repository.AdminRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final AdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(AdminRepo adminRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminRepo.count() == 0) {
            Admin admin = new Admin();
            admin.setNom("Administrateur BozaCM");
            admin.setEmail("admin@bozacm.cm");
            admin.setPassword(passwordEncoder.encode("Admin@BozaCM2026"));
            admin.setRole(Role.ADMIN);
            adminRepo.save(admin);
            System.out.println("Compte administrateur unique créé : admin@bozacm.cm");
        }
    }
}